import { APIRequestContext, expect } from '@playwright/test';

import { GesuchFormularUpdate } from '@dv/shared/model/gesuch';
import { DeepNullable, SetupFn } from '@dv/shared/util-fn/e2e-util';

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

  const body = await response.json();
  //  todo: add function for setting the landId!
  console.log(body);

  expect(response.ok()).toBeTruthy();
  return response;
};

export const setupGesuchWithApi: (
  updateFn: (seed: string) => DeepNullable<GesuchFormularUpdate>,
) => SetupFn =
  (update) =>
  async ({ contexts, gesuchId, trancheId, seed }) => {
    await setGesuchApi(contexts.api, gesuchId, trancheId, update(seed));
  };
