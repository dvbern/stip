import { APIRequestContext, APIResponse, expect } from '@playwright/test';

import {
  AuszahlungUpdate,
  GesuchFormularUpdate,
  Land,
} from '@dv/shared/model/gesuch';
import { DeepNullable, SetupFn } from '@dv/shared/util-fn/e2e-util';

export const setupGesuchWithApi: (
  createFomularUpdateFn: (
    seed: string,
    landId: string,
  ) => DeepNullable<GesuchFormularUpdate>,
  createZahlungsverbindungUpdate: (landId: string) => AuszahlungUpdate,
) => SetupFn =
  (createFomularUpdateFn, createZahlungsverbindungUpdateFn) =>
  async ({ contexts, gesuchId, trancheId, fallId, seed }) => {
    const { api } = contexts;

    const response: APIResponse = await api.get('/api/v1/land');
    const res: Land[] = await response.json();

    const schweizId = res.find((land) => land.laendercodeBfs === '8100')
      ?.id as string;

    if (!schweizId) {
      throw new Error('Schweiz landId not found');
    }

    await setGesuchApi(
      contexts.api,
      gesuchId,
      trancheId,
      fallId,
      createZahlungsverbindungUpdateFn(schweizId),
      createFomularUpdateFn(seed, schweizId),
    );
  };

const setGesuchApi = async (
  apiContext: APIRequestContext,
  gesuchId: string,
  trancheId: string,
  fallId: string,
  zahlungsverbindungUpdate: AuszahlungUpdate,
  gesuchFormularUpdate: DeepNullable<GesuchFormularUpdate>,
) => {
  const setZahlungsverbindungResponse = await apiContext.patch(
    `/api/v1/auszahlung/${fallId}`,
    {
      data: zahlungsverbindungUpdate,
    },
  );

  if (!setZahlungsverbindungResponse.ok()) {
    throw new Error('Failed to set zahlungsverbindung');
  }

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
