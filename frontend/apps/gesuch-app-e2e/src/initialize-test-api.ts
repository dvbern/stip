import { APIRequestContext, expect } from '@playwright/test';

import {
  AusbildungUpdate,
  Ausbildungsstaette,
  FallDashboardItem,
  GesuchFormularUpdate,
} from '@dv/shared/model/gesuch';
import {
  DeepNullable,
  E2eUser,
  SetupFn,
  TestContexts,
  createTest,
  createTestContexts,
  deleteGesuch,
} from '@dv/shared/util-fn/e2e-util';

export const setGesuchApi = async (
  apiContext: APIRequestContext,
  gesuchId: string,
  trancheId: string,
  gesuchFormularUpdate: DeepNullable<GesuchFormularUpdate>,
) => {
  const requestBody = {
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

  const test = createTest(authType).extend<{ apiStatus: number }>({
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    apiStatus: async ({ page }, use, testInfo) => {
      const seed = `${testInfo.title}-${testInfo.workerIndex}`;

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
      const response = await setGesuchApi(
        contexts.api,
        gesuchId,
        trancheId,
        gesuchFormUpdate(seed),
      );

      await use(response.status());
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

export const setupGesuchWithApi: (
  updateFn: (seed: string) => DeepNullable<GesuchFormularUpdate>,
) => SetupFn =
  (update) =>
  async ({ contexts, gesuchId, trancheId, seed }) => {
    await setGesuchApi(contexts.api, gesuchId, trancheId, update(seed));
  };
