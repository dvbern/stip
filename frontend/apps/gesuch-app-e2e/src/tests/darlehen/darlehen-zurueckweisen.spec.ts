import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

import {
  DarlehenPO,
  getE2eUrls,
  initializeMultiUserTest,
  setupGesuchWithApi,
} from '@dv/shared/util-fn/e2e-util';

import {
  ausbildungValues,
  createZahlungsverbindungUpdateFn,
  gesuchFormularUpdateFn,
} from './darlehen.data';
import {
  bringGesuchToEingereicht,
  eingebenDarlehenAsGs,
} from './darlehen.seed';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn, createZahlungsverbindungUpdateFn),
);
const urls = getE2eUrls();

test.describe.configure({ mode: 'serial' });
test.slow();

// todo: add tests for documents after Zurückweisung
test.describe('Freiwilligdarlehen Zurückweisung', () => {
  let gsPage1: Page;
  let sbPage1: Page;
  let sbPage2: Page;
  let darlehenId: string;
  let fallId: string;

  test.beforeAll(async ({ gsPage, createSbPage }) => {
    gsPage1 = gsPage;
    sbPage1 = await createSbPage();
    sbPage2 = await createSbPage(1);
  });

  test('seed: Darlehen als GS eingeben (Status EINGEGEBEN)', async () => {
    await bringGesuchToEingereicht(gsPage1, getGesuchId()!, getTrancheId()!);

    const created = await eingebenDarlehenAsGs(gsPage1);
    darlehenId = created.darlehenId;
    fallId = created.fallId;
  });

  test('SB weist das Darlehen zurück', async () => {
    const darlehenPO = new DarlehenPO(sbPage1);

    // 1. Als SB das eingegebene Darlehen öffnen.
    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    // 2. Schaltfläche „Zurückweisen“ klicken.
    // 3. Im Kommentar-Dialog einen Grund eingeben.
    // 4. Dialog bestätigen.
    await darlehenPO.zurueckweisen(
      'E2E Zurückweisung: bitte Betrag korrigieren',
    );

    // Expected: Das Darlehen kehrt in einen durch den GS bearbeitbaren Status
    // zurück (IN_BEARBEITUNG_GS).
    const gsDarlehenPO = new DarlehenPO(gsPage1);
    await gsDarlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);
    await expect(gsDarlehenPO.elems.gsForm).not.toHaveClass(/readonly/);
    await expect(gsDarlehenPO.elems.buttonEingeben).toBeVisible();
  });

  test('GS sieht die Zurückweisung und kann erneut bearbeiten', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);

    // 1. Als GS das zurückgewiesene Darlehen erneut öffnen.
    await darlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);

    // 2. Prüfen, dass das GS-Formular wieder bearbeitbar ist.
    await expect(darlehenPO.elems.gsForm).not.toHaveClass(/readonly/);

    // 3. Einen Wert anpassen und erneut „Eingeben“ klicken, im Dialog bestätigen.
    await darlehenPO.elems.betragGewuenscht.fill('5500');
    await darlehenPO.eingeben();

    // Expected: Nach erneutem Eingeben ist der Status wieder EINGEGEBEN.
    await expect(darlehenPO.elems.gsForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonEingeben).toBeHidden();

    await gsPage1.close();
    await sbPage1.close();
    await sbPage2.close();
  });
});
