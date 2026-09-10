import { Page, TestInfo, test as baseTest } from '@playwright/test';

import {
  AusbildungCreateResponse,
  FallDashboardItem,
} from '@dv/shared/model/gesuch';

import {
  gsStorageStatePath,
  restoreSessionStorage,
  sbStorageStatePath,
  sozStorageStatePath,
} from './authenticate';
import { E2eUser, ExtendedTest, getE2eUrls } from './playwright.config.base';
import { AusbildungValues, CockpitPO } from './po';
import {
  MultiUserTestContexts,
  SetupFn,
  TestContexts,
  createMultiUserTestContexts,
  deleteGesuch,
} from './utils';

// todo: make users configurable as well? => reconsider, since the setup is quite individual
// for gs-app and full livecyle of gesuche with delete functionality
export const initializeMultiUserTest = (
  ausbildung: AusbildungValues,
  setupFn?: SetupFn,
) => {
  let multiContexts: MultiUserTestContexts;
  let gesuchId: string | undefined;
  let trancheId: string | undefined;

  const test = baseTest.extend<{
    gsPage: Page;
    createSbPage: (sbIndex?: number) => Promise<Page>;
  }>({
    gsPage: async ({ browser }, use, testInfo) => {
      const gsContext = gsStorageStatePath(testInfo, testInfo.parallelIndex);
      // Create GS page with GS context
      const gsPage = await browser.newPage({ storageState: gsContext });
      await restoreSessionStorage(
        gsPage,
        gsContext,
        new URL(getE2eUrls().gs).origin,
      );
      const cockpit = new CockpitPO(gsPage);

      // Initialize gesuch as GS user
      const dashboardPromise = gsPage.waitForResponse(
        '**/api/v1/gesuch/benutzer/me/gs-dashboard',
      );
      await cockpit.goToDashBoard();
      const dashboardResponse = await dashboardPromise;

      const dashboardBody: FallDashboardItem | undefined =
        await dashboardResponse.json();
      gesuchId = dashboardBody?.ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;

      if (gesuchId) {
        const response = await deleteGesuch(multiContexts.gs.api, gesuchId);
        if (response.status() >= 400) {
          throw new Error(
            `Failed to delete gesuch ${gesuchId}, see backend logs for more information.`,
          );
        }
        await gsPage.reload();
      }

      await cockpit.createNewStipendium(ausbildung);

      // Get new gesuch ID
      const response = await gsPage.waitForResponse((response) => {
        return (
          response.url().includes('/api/v1/ausbildung') &&
          response.status() === 200 &&
          response.request().method() === 'POST'
        );
      });

      const body: AusbildungCreateResponse | undefined = await response.json();
      const fallId = body?.ausbildung?.fallId;
      gesuchId = body?.gesuchId;
      trancheId = body?.gesuchTrancheId;

      if (!gesuchId || !trancheId || !fallId) {
        throw new Error('Failed to create new gesuch');
      }

      if (setupFn) {
        const seed = `${testInfo.title}-${testInfo.workerIndex}`;
        const contexts: TestContexts = {
          browser: multiContexts.gs.browser,
          api: multiContexts.gs.api,
          dispose: async () => {
            try {
              await multiContexts.gs.api.dispose();
              await multiContexts.gs.browser.close();
            } catch (e) {
              console.warn('Failed to dispose GS contexts', e);
            }
          },
        };

        await setupFn({ contexts, gesuchId, trancheId, fallId, seed });
      }

      await use(gsPage);
    },

    // Lazily create the SB page only when a test needs it, so the SB browser
    // window doesn't open (and steal focus) during the GS part of the flow.
    createSbPage: async ({ browser }, use, testInfo) => {
      const sbPages = new Map<number, Page>();
      await use(async (sbIndex = 0) => {
        const existingPage = sbPages.get(sbIndex);
        if (existingPage) {
          return existingPage;
        }

        const sbContext = sbStorageStatePath(testInfo, sbIndex);
        const sbPage = await browser.newPage({ storageState: sbContext });
        sbPages.set(sbIndex, sbPage);

        await restoreSessionStorage(
          sbPage,
          sbContext,
          new URL(getE2eUrls().sb).origin,
        );
        return sbPage;
      });
      await Promise.all([...sbPages.values()].map((page) => page.close()));
    },
  });

  // to initialize the multi-user test contexts before all tests run
  test.beforeAll(async ({ playwright, baseURL, browser }, testInfo) => {
    multiContexts = await createMultiUserTestContexts({
      browser,
      playwright,
      gsStorageState: gsStorageStatePath(testInfo, testInfo.parallelIndex),
      sbStorageState: sbStorageStatePath(testInfo),
      baseURL,
    });
  });

  test.afterAll(async () => {
    if (multiContexts) {
      if (gesuchId) {
        await deleteGesuch(multiContexts.gs.api, gesuchId);
      }
      await multiContexts.dispose();
    }
  });

  return {
    getGesuchId: () => gesuchId,
    getTrancheId: () => trancheId,
    getContexts: (): MultiUserTestContexts => multiContexts,
    test,
  };
};

/**
 * Resolve the pre-authenticated storage state and app origin for a single-user
 * role.
 */
const singleUserStorage = (authType: E2eUser, testInfo: TestInfo) => {
  const urls = getE2eUrls();

  switch (authType) {
    case 'GESUCHSTELLER':
      return {
        storagePath: gsStorageStatePath(testInfo, testInfo.parallelIndex),
        origin: new URL(urls.gs).origin,
      };
    case 'SACHBEARBEITER':
    case 'SACHBEARBEITER_ADMIN':
      return {
        storagePath: sbStorageStatePath(testInfo),
        origin: new URL(urls.sb).origin,
      };
    case 'SOZIALDIENST':
    case 'SOZIALDIENST_ADMIN':
      return {
        storagePath: sozStorageStatePath(testInfo),
        origin: new URL(urls.soz).origin,
      };
  }
};

/**
 * Initialize a single-user e2e test.
 *
 * The returned `test` provides the
 * built-in `page` already signed in as `testUser` and can be further extended
 * by the caller.
 */
export const initializeSingleUserTest = (test: ExtendedTest) => {
  return test.extend({
    page: async ({ browser, testUser }, use, testInfo) => {
      const { storagePath, origin } = singleUserStorage(testUser, testInfo);

      const page = await browser.newPage({
        storageState: storagePath,
        baseURL: testInfo.project.use.baseURL,
        ignoreHTTPSErrors: true,
      });
      await restoreSessionStorage(page, storagePath, origin);

      await use(page);

      await page.close();
    },
  });
};
