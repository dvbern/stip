import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

import {
  DarlehenPO,
  GsDashboardPO,
  getE2eUrls,
  initializeMultiUserTest,
  setupGesuchWithApi,
} from '@dv/shared/util-fn/e2e-util';

import {
  ausbildungValues,
  createZahlungsverbindungUpdateFn,
  darlehenGs,
  darlehenSbGewaehren,
  gesuchFormularUpdateFn,
  grundOhneDokument,
} from './darlehen.data';
import { bringGesuchToEingereicht } from './darlehen.seed';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn, createZahlungsverbindungUpdateFn),
);
const urls = getE2eUrls();

test.describe.configure({ mode: 'serial' });
test.slow();

test.describe('Freiwilligdarlehen Happy Path (Gewährung)', () => {
  let gsPage1: Page;
  let sbPage1: Page; // expected to be without Freigabestelle permissions
  let sbPage2: Page;
  let darlehenId: string;
  let fallId: string;

  test.beforeAll(async ({ gsPage, createSbPage }) => {
    gsPage1 = gsPage;
    sbPage1 = await createSbPage();
    sbPage2 = await createSbPage(1);
  });

  test('seed: Gültiges Gesuch vorbereiten und Cockpit öffnen', async () => {
    await bringGesuchToEingereicht(gsPage1, getGesuchId()!, getTrancheId()!);
  });

  test('GS erfasst und gibt ein Freiwilligdarlehen ein', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);
    const dashboardPO = new GsDashboardPO(gsPage1);

    await dashboardPO.goToDashBoard();
    const created = await dashboardPO.createDarlehen();
    darlehenId = created.id;
    fallId = created.fallId!;

    await expect(darlehenPO.elems.loading).toBeHidden();
    await expect(darlehenPO.elems.gsForm).toBeVisible();

    await darlehenPO.fillGsForm(darlehenGs);

    await darlehenPO.uploadDokument('BETREIBUNGS_AUSZUG');

    await darlehenPO.selectGrund(grundOhneDokument);

    await darlehenPO.eingeben();

    await expect(darlehenPO.elems.gsForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonEingeben).toBeHidden();

    await dashboardPO.goToDashBoard();

    await dashboardPO.expectCanCreateDarlehen(false);
    const darlehenSubitem = dashboardPO.getDarlehen(darlehenId);
    await dashboardPO.expectSubitemStatus(darlehenSubitem, 'Eingegeben');
  });

  test('SB gewährt und gibt das Darlehen frei', async () => {
    const darlehenPO = new DarlehenPO(sbPage1);

    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    await darlehenPO.selectGewaehren(true);

    await expect(darlehenPO.elems.betragDarlehen).toBeVisible();

    await darlehenPO.fillSbForm(darlehenSbGewaehren);

    await darlehenPO.freigeben();

    await expect(darlehenPO.elems.sbForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonFreigeben).toBeHidden();
    await sbPage1.close();
  });

  test('Freigabestelle schliesst das Darlehen ab (Akzeptiert)', async () => {
    const darlehenPO = new DarlehenPO(sbPage2);

    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    await expect(
      darlehenPO.elems.gewaehrenRadio.getByRole('radio', { name: 'Ja' }),
    ).toBeChecked();
    await expect(darlehenPO.elems.kommentar).toHaveValue(
      darlehenSbGewaehren.kommentar!,
    );

    await darlehenPO.abschliessen();

    await sbPage2.close();
  });

  test('GS kann die Verfügung herunterladen', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);
    const dashboardPO = new GsDashboardPO(gsPage1);

    await darlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.gsForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonAbschliessen).toBeHidden();

    await dashboardPO.goToDashBoard();
    await expect(dashboardPO.getDarlehen(darlehenId)).not.toBeVisible();
    await dashboardPO.expectCanCreateDarlehen(true);

    await darlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);

    // todo: also check on verfueung seite und nachrichten (for all darlehen tests)
    await darlehenPO.downloadDarlehenVerfuegung();

    await gsPage1.close();
  });
});
