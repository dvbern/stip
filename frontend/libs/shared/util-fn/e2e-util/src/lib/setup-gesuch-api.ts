import { APIRequestContext } from '@playwright/test';

import {
  AbschlussSlim,
  AuszahlungUpdate,
  GesuchFormularUpdate,
  Land,
} from '@dv/shared/model/gesuch';

import { ExplicitNull, SetupFn } from './utils';

export const setupGesuchWithApi: (
  createFomularUpdateFn: (
    seed: string,
    abschluesse: AbschlussSlim[],
    landId: string,
  ) => ExplicitNull<GesuchFormularUpdate>,
  createZahlungsverbindungUpdate: (landId: string) => AuszahlungUpdate,
) => SetupFn =
  (createFomularUpdateFn, createZahlungsverbindungUpdateFn) =>
  async ({ contexts, gesuchId, trancheId, fallId, seed }) => {
    const { api } = contexts;

    const landResponse = await api.get('/api/v1/land');
    const laender: Land[] = await landResponse.json();

    const abschlussResponse = await api.get('/api/v1/abschluss/slim');
    const abschluesse: AbschlussSlim[] = await abschlussResponse.json();

    const schweizId = laender.find((land) => land.laendercodeBfs === '8100')
      ?.id as string;

    if (!schweizId) {
      throw new Error('Schweiz landId not found');
    }
    if (abschluesse.length === 0) {
      throw new Error('No Abschluesse found');
    }

    await setGesuchApi(
      contexts.api,
      gesuchId,
      trancheId,
      fallId,
      createZahlungsverbindungUpdateFn(schweizId),
      createFomularUpdateFn(seed, abschluesse, schweizId),
    );
  };

const setGesuchApi = async (
  apiContext: APIRequestContext,
  gesuchId: string,
  trancheId: string,
  fallId: string,
  zahlungsverbindungUpdate: AuszahlungUpdate,
  gesuchFormularUpdate: ExplicitNull<GesuchFormularUpdate>,
) => {
  const setZahlungsverbindungResponse = await apiContext.post(
    `/api/v1/auszahlung/${fallId}`,
    {
      data: zahlungsverbindungUpdate,
    },
  );

  if (!setZahlungsverbindungResponse.ok()) {
    const text = await setZahlungsverbindungResponse.text();
    console.error(
      `Failed to set zahlungsverbindung for fallId ${fallId}:`,
      text,
    );
    throw new Error(
      `Failed to set zahlungsverbindung for fallId ${fallId}: ${text}`,
    );
  }

  const requestBody = {
    gesuchTrancheToWorkWith: {
      gesuchFormular: gesuchFormularUpdate,
      id: trancheId,
    },
  };

  const response = await apiContext.patch(`/api/v1/gesuch/${gesuchId}/gs`, {
    data: requestBody,
  });

  if (!response.ok()) {
    const responseText = await response.text();
    console.error(`Failed to update gesuch with id ${gesuchId}:`, responseText);
    throw new Error(
      `Failed to update gesuch with id ${gesuchId}: ${responseText}`,
    );
  }
  return response;
};
