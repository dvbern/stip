import { FallDashboardItem } from '@dv/shared/model/gesuch';
import {
  E2eUser,
  SetupFn,
  TestContexts,
  createTest,
  createTestContexts,
  deleteGesuch,
} from '@dv/shared/util-fn/e2e-util';

import { AusbildungValues } from './po/ausbildung.po';
import { CockpitPO } from './po/cockpit.po';

/**
 * Initialize test context, reset all gesuche and create a new Gesuch
 *
 * It also registers a beforeAll to initialize the API Contexts and afterAll hook to delete the created gesuch
 */
export const initializeTest = (
  authType: E2eUser,
  ausbildung: AusbildungValues,
  setupFn?: SetupFn,
) => {
  let contexts: TestContexts;
  let gesuchId: string | undefined;
  let trancheId: string | undefined;
  const test = createTest(authType).extend<{ cockpit: CockpitPO }>({
    cockpit: async ({ page }, use, testInfo) => {
      const cockpit = new CockpitPO(page);

      // delete if existing gesuch
      const dashboardPromise = page.waitForResponse(
        '**/api/v1/gesuch/benutzer/me/gs-dashboard',
      );
      await cockpit.goToDashBoard();
      const dashboardResponse = await dashboardPromise;

      const dashboardBody: FallDashboardItem[] | undefined =
        await dashboardResponse.json();
      gesuchId =
        dashboardBody?.[0]?.ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;
      trancheId =
        dashboardBody?.[0]?.ausbildungDashboardItems?.[0]?.gesuchs?.[0]
          .currentTrancheId;

      if (gesuchId) {
        const response = await deleteGesuch(contexts.api, gesuchId);
        if (response.status() >= 400) {
          throw new Error(
            `Failed to delete gesuch ${gesuchId}, see backend logs for more information.`,
          );
        }
        await page.reload();
      }

      await cockpit.createNewStipendium(ausbildung);

      // extract gesuch new gesuchid
      const response = await page.waitForResponse((response) => {
        return (
          response.url().includes('/api/v1/gesuch/benutzer/me/gs-dashboard') &&
          response.status() === 200 &&
          response.request().method() === 'GET'
        );
      });

      const body: FallDashboardItem[] | undefined = await response.json();
      gesuchId = body?.[0].ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;
      trancheId =
        body?.[0].ausbildungDashboardItems?.[0]?.gesuchs?.[0].currentTrancheId;

      if (!gesuchId || !trancheId) {
        throw new Error('Failed to create new gesuch');
      }

      if (setupFn) {
        const seed = `${testInfo.title}-${testInfo.workerIndex}`;
        await setupFn({ contexts, gesuchId, trancheId, seed });
      }

      await use(cockpit);
    },
  });

  test.beforeAll(async ({ playwright, baseURL, browser, storageState }) => {
    contexts = await createTestContexts({
      browser,
      playwright,
      storageState,
      baseURL,
    });
  });

  test.afterAll(async () => {
    if (contexts) {
      if (gesuchId) {
        await deleteGesuch(contexts.api, gesuchId);
      }
      await contexts.dispose();
    }
  });

  return {
    getGesuchId: () => gesuchId,
    getTrancheId: () => trancheId,
    getContext: () => contexts,
    test,
  };
};
