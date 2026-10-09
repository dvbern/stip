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

test.describe('Freiwilligdarlehen Zurückweisung', () => {
  let gsPage1: Page;
  let sbPage1: Page;
  let darlehenId: string;
  let fallId: string;

  test.beforeAll(async ({ gsPage, createSbPage }) => {
    gsPage1 = gsPage;
    sbPage1 = await createSbPage();
  });

  test('seed: Darlehen als GS eingeben (Status EINGEGEBEN)', async () => {
    await bringGesuchToEingereicht(gsPage1, getGesuchId()!, getTrancheId()!);

    const created = await eingebenDarlehenAsGs(gsPage1);
    darlehenId = created.darlehenId;
    fallId = created.fallId;
  });

  test('SB weist das Darlehen zurück', async () => {
    const darlehenPO = new DarlehenPO(sbPage1);

    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    await darlehenPO.zurueckweisen(
      'E2E Zurückweisung: bitte Betrag korrigieren',
    );
    await sbPage1.close();
  });

  test('GS sieht die Zurückweisung und kann erneut bearbeiten', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);
    await darlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);

    await expect(darlehenPO.elems.gsForm).not.toHaveClass(/readonly/);

    await darlehenPO.elems.betragGewuenscht.fill('5500');
    await darlehenPO.eingeben();

    await expect(darlehenPO.elems.gsForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonEingeben).toBeHidden();

    await gsPage1.close();
  });
});
