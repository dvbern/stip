import { APIRequestContext, expect } from '@playwright/test';
import { addYears, format } from 'date-fns';

import {
  AusbildungUpdate,
  Ausbildungsstaette,
  FallDashboardItem,
  GesuchFormularUpdate,
  GesuchUpdate,
} from '@dv/shared/model/gesuch';
import {
  DeepNullable,
  E2eUser,
  TestContexts,
  createTest,
  createTestContexts,
  deleteGesuch,
} from '@dv/shared/util-fn/e2e-util';

import { AusbildungValues } from './po/ausbildung.po';
import { CockpitPO } from './po/cockpit.po';

export const setGesuchApi = async (
  apiContext: APIRequestContext,
  gesuchId: string,
  trancheId: string,
  gesuchFormularUpdate: GesuchFormularUpdate,
) => {
  const requestBody: GesuchUpdate = {
    gesuchTrancheToWorkWith: {
      gesuchFormular: gesuchFormularUpdate,
      id: trancheId,
    },
  };

  const response = await apiContext.patch(`/api/v1/gesuch/${gesuchId}`, {
    data: requestBody,
  });

  expect(response.ok()).toBeTruthy();
  return response;
};

export const getDashboard = async (apiContext: APIRequestContext) => {
  const response = await apiContext.get(
    '/api/v1/gesuch/benutzer/me/gs-dashboard',
  );
  expect(response.status()).toBe(200);
  const body = await response.json();
  return body as FallDashboardItem[];
};

export const createAusbildungWithApi = async (
  apiContext: APIRequestContext,
  ausbildung: AusbildungUpdate,
): Promise<string> => {
  const response = await apiContext.post('/api/v1/ausbildung', {
    data: ausbildung,
  });
  expect(response.status()).toBe(200);
  const body = await response.json();
  return body.id as string;
};

export const getAusbildungsGangId = async (
  apiContext: APIRequestContext,
  ausbildungsgangName: string,
) => {
  const response = await apiContext.get('/api/v1/ausbildungsstaette');
  expect(response.status()).toBe(200);
  const body = (await response.json()) as Ausbildungsstaette[];

  let ausbildungsgangId = undefined;

  for (const ausbildungsstaette of body) {
    if (!ausbildungsstaette?.ausbildungsgaenge) {
      continue;
    }
    for (const ausbildungsgang of ausbildungsstaette.ausbildungsgaenge) {
      if (
        ausbildungsgang.bezeichnungDe
          ?.toLocaleLowerCase()
          .includes(ausbildungsgangName.toLocaleLowerCase())
      ) {
        ausbildungsgangId = ausbildungsgang.id;
        break;
      }
    }
  }

  if (!ausbildungsgangId) {
    throw new Error(
      `Failed to find ausbildungsgang with name ${ausbildungsgangName}`,
    );
  }

  return ausbildungsgangId;
};

export const initializeTestByApi = (
  authType: E2eUser,
  ausbildung: AusbildungUpdate,
  gesuchFormUpdate: (seed: string) => DeepNullable<GesuchFormularUpdate>,
) => {
  let contexts: TestContexts;
  let gesuchId: string | undefined;
  let trancheId: string | undefined;

  const test = createTest(authType).extend<{ cockpit: CockpitPO }>({
    cockpit: async ({ page }, use, testInfo) => {
      const seed = `${testInfo.title}-${testInfo.workerIndex}`;
      const cockpit = new CockpitPO(page);

      const exitingGesuchId = await getDashboard(contexts.api).then((body) => {
        return body?.[0].ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;
      });

      if (exitingGesuchId) {
        const deleteResponse = await deleteGesuch(
          contexts.api,
          exitingGesuchId,
        );
        expect(deleteResponse.ok()).toBeTruthy();
      }

      // get fall id
      const fallId = await getDashboard(contexts.api).then((body) => {
        return body?.[0].fall.id;
      });
      expect(fallId).toBeTruthy();

      // get ausbildungsgang id
      const ausbildungsgangId = await getAusbildungsGangId(
        contexts.api,
        'master',
      );

      // create ausbildung
      await createAusbildungWithApi(contexts.api, {
        ...ausbildung,
        fallId,
        ausbildungsgangId,
      });

      // get gesuchId and trancheId
      const dashboardItems = await getDashboard(contexts.api);

      gesuchId =
        dashboardItems?.[0].ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;
      trancheId =
        dashboardItems?.[0].ausbildungDashboardItems?.[0]?.gesuchs?.[0]
          .currentTrancheId;

      if (!gesuchId || !trancheId) {
        throw new Error('Failed to create new gesuch');
      }

      // set gesuch
      await setGesuchApi(
        contexts.api,
        gesuchId,
        trancheId,
        gesuchFormUpdate(seed) as GesuchFormularUpdate,
      );

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

/**
 * Initialize test context, reset all gesuche and create a new Gesuch
 *
 * It also registers a beforeAll to initialize the API Contexts and afterAll hook to delete the created gesuch
 */
export const initializeTest = (
  authType: E2eUser,
  ausbildung: AusbildungValues,
) => {
  let contexts: TestContexts;
  let gesuchId: string | undefined;
  let trancheId: string | undefined;
  const test = createTest(authType).extend<{ cockpit: CockpitPO }>({
    cockpit: async ({ page }, use) => {
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

      // extract gesuch new gesuch id
      const requestPromise = page.waitForResponse((response) => {
        const reposenUrl = response.url();
        console.log('reposenUrl', reposenUrl);

        return (
          response.url().includes('/api/v1/gesuch/benutzer/me/gs-dashboard') &&
          response.status() === 200 &&
          response.request().method() === 'GET'
        );
      });

      await cockpit.createNewStipendium(ausbildung);

      const response = await requestPromise;
      const body: FallDashboardItem[] | undefined = await response.json();
      gesuchId = body?.[0].ausbildungDashboardItems?.[0]?.gesuchs?.[0].id;
      trancheId =
        body?.[0].ausbildungDashboardItems?.[0]?.gesuchs?.[0].currentTrancheId;
      if (!gesuchId) {
        throw new Error('Failed to create new gesuch');
      }
      cockpit.getGesuchEdit().click();

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

export const thisYear = format(new Date(), 'yyyy');
export const specificMonth = (month: number) =>
  `${month}.${format(new Date(), 'yyyy')}`;
export const specificMonthPlusYears = (month: number, years: number) =>
  `${month}.${format(addYears(new Date(), years), 'yyyy')}`;
export const specificYearsAgo = (years: number) =>
  format(addYears(new Date(), -years), 'yyyy');
