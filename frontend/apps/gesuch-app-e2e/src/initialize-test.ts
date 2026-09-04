import {
  AusbildungCreateResponse,
  FallDashboardItem,
} from '@dv/shared/model/gesuch';
import {
  MultiUserTestContexts,
  Page,
  SetupFn,
  TestContexts,
  createMultiUserTest,
  createMultiUserTestContexts,
  deleteGesuch,
  getE2eUrls,
  gsStorageStatePath,
  restoreSessionStorage,
  sbStorageStatePath,
} from '@dv/shared/util-fn/e2e-util';

import { AusbildungValues } from './po/ausbildung.po';
import { CockpitPO } from './po/cockpit.po';

/**
 * Initialize test context, reset all gesuche and create a new Gesuch
 *
 * It also registers a beforeAll to initialize the API Contexts and afterAll hook to delete the created gesuch
 */
// not in use
// export const initializeTest = (
//   authType: E2eUser,
//   ausbildung: AusbildungValues,
//   setupFn?: SetupFn,
// ) => {
//   let contexts: TestContexts;
//   let gesuchId: string | undefined;
//   let trancheId: string | undefined;
//   const test = createTest(authType).extend<{ cockpit: CockpitPO }>({
//     cockpit: async ({ page }, use, testInfo) => {
//       const cockpit = new CockpitPO(page);

//       // delete if existing gesuch
//       const dashboardPromise = page.waitForResponse(
//         '**/api/v1/gesuch/benutzer/me/gs-dashboard',
//       );
//       await cockpit.goToDashBoard();
//       const dashboardResponse = await dashboardPromise;

//       const dashboardBody: FallDashboardItem | undefined =
//         await dashboardResponse.json();
//       gesuchId = dashboardBody?.ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;
//       trancheId =
//         dashboardBody?.ausbildungDashboardItems?.[0]?.gesuchs?.[0]
//           .currentTrancheId;

//       if (gesuchId) {
//         const response = await deleteGesuch(contexts.api, gesuchId);
//         if (response.status() >= 400) {
//           throw new Error(
//             `Failed to delete gesuch ${gesuchId}, see backend logs for more information.`,
//           );
//         }
//         await page.reload();
//       }

//       await cockpit.createNewStipendium(ausbildung);

//       // extract gesuch new gesuchid
//       const response = await page.waitForResponse((response) => {
//         return (
//           response.url().includes('/api/v1/gesuch/benutzer/me/gs-dashboard') &&
//           response.status() === 200 &&
//           response.request().method() === 'GET'
//         );
//       });

//       const body: FallDashboardItem | undefined = await response.json();
//       const fallId = body?.fall.id;
//       gesuchId = body?.ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;
//       trancheId =
//         body?.ausbildungDashboardItems?.[0]?.gesuchs?.[0].currentTrancheId;

//       if (!gesuchId || !trancheId || !fallId) {
//         throw new Error('Failed to create new gesuch');
//       }

//       if (setupFn) {
//         const seed = `${testInfo.title}-${testInfo.workerIndex}`;
//         await setupFn({ contexts, gesuchId, trancheId, fallId, seed });
//       }

//       await use(cockpit);
//     },
//   });

//   test.beforeAll(async ({ playwright, baseURL, browser, storageState }) => {
//     contexts = await createTestContexts({
//       browser,
//       playwright,
//       storageState,
//       baseURL,
//     });
//   });

//   test.afterAll(async () => {
//     if (contexts) {
//       if (gesuchId) {
//         await deleteGesuch(contexts.api, gesuchId);
//       }
//       await contexts.dispose();
//     }
//   });

//   return {
//     getGesuchId: () => gesuchId,
//     getTrancheId: () => trancheId,
//     getContext: () => contexts,
//     test,
//   };
// };

export const initializeMultiUserTest = (
  ausbildung: AusbildungValues,
  setupFn?: SetupFn,
) => {
  let multiContexts: MultiUserTestContexts;
  let gesuchId: string | undefined;
  let trancheId: string | undefined;

  const test = createMultiUserTest().extend<{
    gsPage: Page;
    createSbPage: () => Promise<Page>;
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
      const sbContext = sbStorageStatePath(testInfo);
      let sbPage: Page | undefined;
      await use(async () => {
        if (!sbPage) {
          sbPage = await browser.newPage({ storageState: sbContext });
          await restoreSessionStorage(
            sbPage,
            sbContext,
            new URL(getE2eUrls().sb).origin,
          );
        }
        return sbPage;
      });
      await sbPage?.close();
    },
  });

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
