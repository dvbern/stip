import { test as base } from '@playwright/test';

import {
  AuthenticatedTest,
  TestContexts,
  authenticatedTestOptions,
  createTestContexts,
  deleteGesuch,
} from '@dv/shared/util-fn/e2e-util';

import { CockpitPO } from './po/cockpit.po';

/**
 * Initialize test context, reset all gesuche and create a new Gesuch
 *
 * It also registers a beforeAll to initialize the API Contexts and afterAll hook to delete the created gesuch
 */
export const initializeTest = () => {
  let contexts: TestContexts;
  let gesuchId: string;
  const test = base.extend<{ cockpit: CockpitPO } & AuthenticatedTest>({
    ...authenticatedTestOptions,
    cockpit: async ({ page }, use) => {
      const cockpit = new CockpitPO(page);

      // delete if existing gesuch
      const fallPromise = page.waitForResponse('**/api/v1/benutzer/prepare/me');
      await cockpit.goToDashBoard();
      const fallresponse = await fallPromise;

      const fallbody = await fallresponse.json();
      gesuchId = fallbody.length > 0 ? fallbody[0].id : undefined;
      if (gesuchId) {
        const response = await contexts.api.delete(
          `/api/v1/gesuch/${gesuchId}`,
        );
        if (response.status() !== 200) {
          throw new Error(
            `Failed to delete gesuch ${gesuchId}, see backend logs for more information.`,
          );
        }
        await page.reload();
      }

      // extract gesuch new gesuch id
      const requestPromise = page.waitForResponse('**/api/v1/gesuch/*');
      await cockpit.getGesuchNew().click();
      const response = await requestPromise;
      const body = await response.json();
      gesuchId = body.id;

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
      await deleteGesuch(contexts.api, gesuchId);
      await contexts.dispose();
    }
  });

  return {
    getGesuchId: () => gesuchId,
    getContext: () => contexts,
    test,
  };
};
