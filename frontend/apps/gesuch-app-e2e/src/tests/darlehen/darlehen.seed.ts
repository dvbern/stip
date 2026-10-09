import { Page, expect } from '@playwright/test';

import { FreiwilligDarlehen } from '@dv/shared/model/gesuch';
import {
  DarlehenPO,
  SachbearbeiterGesuchHeaderPO,
  StepsNavPO,
  acceptDocuments,
  expectStepTitleToContainText,
  getE2eUrls,
  uploadFiles,
} from '@dv/shared/util-fn/e2e-util';

import { darlehenGs, grundOhneDokument } from './darlehen.data';

const urls = getE2eUrls();

/**
 * GS uploads all required documents and submits (einreicht) the prepared gesuch.
 * Assumes the gesuch formular has already been filled via `setupGesuchWithApi`.
 */
export const gsEinreichen = async (
  gsPage: Page,
  gesuchId: string,
  trancheId: string,
) => {
  await gsPage.bringToFront();

  const requiredDokumenteResponse = gsPage.waitForResponse(
    '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
  );
  await gsPage.goto(
    `${urls.gs}/gesuch/dokumente/${gesuchId}/tranche/${trancheId}`,
  );
  await expectStepTitleToContainText('Dokumente', gsPage);
  await requiredDokumenteResponse;
  await uploadFiles(gsPage);
  await gsPage.getByTestId('step-nav-abschluss').first().click();

  await expectStepTitleToContainText('Freigabe', gsPage);
  await gsPage.getByTestId('button-abschluss').click();
  const freigabeResponse = gsPage.waitForResponse(
    '**/api/v1/gesuch/*/einreichen/gs',
  );
  await gsPage.getByTestId('dialog-confirm').click();
  await freigabeResponse;
};

/**
 * SB takes the submitted gesuch into Bearbeitung, accepts all documents and
 * finishes the Bearbeitung.
 */
export const sbBearbeitenUndAbschliessen = async (
  sbPage: Page,
  gesuchId: string,
  trancheId: string,
) => {
  const sbGesuchHeader = new SachbearbeiterGesuchHeaderPO(sbPage);
  const sbStepsNavPO = new StepsNavPO(sbPage);

  await sbPage.bringToFront();
  await sbPage.goto(`${urls.sb}/gesuch/info/${gesuchId}/tranche/${trancheId}`);

  await sbGesuchHeader.elems.aktionMenu.click();
  await sbGesuchHeader.elems
    .getAktionStatusUebergangItem('SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT')
    .click();
  await sbPage
    .getByTestId('form-kommentar-dialog-kommentar')
    .fill('E2E Darlehen Seed Kommentar');
  await sbPage.getByTestId('dialog-confirm').click();
  await sbGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });

  await sbGesuchHeader.elems.aktionMenu.click();
  await sbGesuchHeader.elems
    .getAktionStatusUebergangItem('SET_TO_BEARBEITUNG')
    .click();

  const requiredDokumenteResp = sbPage.waitForResponse(
    '**/api/v1/gesuchtranche/*/dokumenteToUpload/*',
  );
  await sbStepsNavPO.elems.dokumente.first().click();
  await expectStepTitleToContainText('Dokumente', sbPage);
  await requiredDokumenteResp;
  await acceptDocuments(sbPage);

  const abschliesenPromise = sbPage.waitForResponse(
    '**/api/v1/gesuch/*/bearbeitungAbschliessen',
  );
  await sbGesuchHeader.elems.aktionMenu.click();
  await sbGesuchHeader.elems
    .getAktionStatusUebergangItem('BEARBEITUNG_ABSCHLIESSEN')
    .click();
  await abschliesenPromise;
};

/**
 * Freigabestelle sets the gesuch to VERFUEGT and VERSENDET.
 */
export const sbVerfuegen = async (
  sbPage: Page,
  gesuchId: string,
  trancheId: string,
) => {
  const verfuegtGesuchHeader = new SachbearbeiterGesuchHeaderPO(sbPage);

  await sbPage.bringToFront();
  await sbPage.goto(`${urls.sb}/gesuch/info/${gesuchId}/tranche/${trancheId}`);

  const verfuegtPromise = sbPage.waitForResponse(
    '**/api/v1/gesuch/status/verfuegt/*',
  );
  await verfuegtGesuchHeader.elems.aktionMenu.click();
  await verfuegtGesuchHeader.elems
    .getAktionStatusUebergangItem('VERFUEGT')
    .click();
  const verfuegtResponse = await verfuegtPromise;
  if (!verfuegtResponse.ok()) {
    throw new Error(
      `Verfuegt response failed with status ${await verfuegtResponse.text()}`,
    );
  }
  await verfuegtGesuchHeader.elems.actionLoading.waitFor({ state: 'hidden' });

  const versendetPromise = sbPage.waitForResponse(
    '**/api/v1/gesuch/status/versendet/*',
  );
  await verfuegtGesuchHeader.elems.aktionMenu.click();
  await verfuegtGesuchHeader.elems
    .getAktionStatusUebergangItem('VERSENDET')
    .click();
  const versendetResponse = await versendetPromise;
  if (!versendetResponse.ok()) {
    throw new Error(
      `Versendet response failed with status ${await versendetResponse.text()}`,
    );
  }
};

export const bringGesuchToVerfuegt = async (
  gsPage: Page,
  sbPage1: Page,
  sbPage2: Page,
  gesuchId: string,
  trancheId: string,
) => {
  await gsEinreichen(gsPage, gesuchId, trancheId);
  await sbBearbeitenUndAbschliessen(sbPage1, gesuchId, trancheId);
  await sbVerfuegen(sbPage2, gesuchId, trancheId);
};

export const bringGesuchToEingereicht = async (
  gsPage: Page,
  gesuchId: string,
  trancheId: string,
) => {
  await gsEinreichen(gsPage, gesuchId, trancheId);
};

export interface CreatedDarlehen {
  darlehenId: string;
  fallId: string;
}

/**
 * GS creates a new Freiwilligdarlehen from the dashboard and waits for the form to
 * be ready. Returns the created darlehen id and its fall id.
 */
export const createDarlehenAsGs = async (
  gsPage: Page,
): Promise<CreatedDarlehen> => {
  await gsPage.bringToFront();
  await gsPage.goto(`${urls.gs}/dashboard`);

  const createResponse = gsPage.waitForResponse(
    (response) =>
      /\/api\/v1\/darlehen\/[^/]+$/.test(response.url()) &&
      response.request().method() === 'POST',
  );
  await gsPage.getByTestId('dashboard-darlehen-create').click();
  const response = await createResponse;
  const darlehen: FreiwilligDarlehen = await response.json();

  await expect(gsPage.getByTestId('form-darlehen-loading')).toBeHidden();
  await expect(gsPage.getByTestId('form-darlehen-gs-form')).toBeVisible();

  if (!darlehen.fallId) {
    throw new Error('Created darlehen is missing a fallId');
  }

  return { darlehenId: darlehen.id, fallId: darlehen.fallId };
};

/**
 * GS creates a Freiwilligdarlehen, fills the required fields, uploads the
 * Betreibungsauszug, selects a grund without an extra document and submits
 * (eingibt) it, leaving the darlehen in status EINGEGEBEN.
 */
export const eingebenDarlehenAsGs = async (
  gsPage: Page,
): Promise<CreatedDarlehen> => {
  const created = await createDarlehenAsGs(gsPage);

  const darlehenPO = new DarlehenPO(gsPage);
  await darlehenPO.fillGsForm(darlehenGs);
  await darlehenPO.uploadDokument('BETREIBUNGS_AUSZUG');
  await darlehenPO.selectGrund(grundOhneDokument);
  await darlehenPO.eingeben();

  return created;
};
