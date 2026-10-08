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

    // 3.-5. Gewünschter Betrag, Schulden und Anzahl Betreibungen ausfüllen.
    await darlehenPO.fillGsForm(darlehenGs);

    // 6. Den erforderlichen Betreibungsauszug hochladen.
    await darlehenPO.uploadDokument('BETREIBUNGS_AUSZUG');

    // 7. Einen Grund aktivieren, der kein zusätzliches Dokument erfordert.
    await darlehenPO.selectGrund(grundOhneDokument);

    // 8. Schaltfläche „Eingeben“ klicken.
    // 9. Im Bestätigungsdialog bestätigen.
    await darlehenPO.eingeben();

    // Expected: GS-Formular ist nach dem Eingeben schreibgeschützt (Status EINGEGEBEN).
    await expect(darlehenPO.elems.gsForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonEingeben).toBeHidden();

    await dashboardPO.goToDashBoard();
    await dashboardPO.expectCanCreateDarlehen(false);
    const darlehenSubitem = dashboardPO.getDarlehen(darlehenId);
    await dashboardPO.expectSubitemStatus(darlehenSubitem, 'Eingegeben');
  });

  test('SB gewährt und gibt das Darlehen frei', async () => {
    const darlehenPO = new DarlehenPO(sbPage1);

    // 1. Als SB das Darlehen öffnen.
    // 2. Warten, bis das SB-Formular geladen ist.
    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    // 3. In der Radiogruppe „Darlehen gewähren“ die Option „Ja“ wählen.
    await darlehenPO.selectGewaehren(true);

    // Expected: Betragsfeld wird aktiviert, negative Verfügung ausgeblendet.
    await expect(darlehenPO.elems.betragDarlehen).toBeVisible();

    // 4. Feld „Betrag Darlehen“ ausfüllen.
    // 5. Feld „Kommentar“ ausfüllen.
    await darlehenPO.fillSbForm(darlehenSbGewaehren);

    // 6. Schaltfläche „Freigeben“ klicken.
    // 7. Im Bestätigungsdialog bestätigen.
    await darlehenPO.freigeben();

    // Expected: Darlehen ist in Freigabe, SB kann nicht mehr schreiben.
    await expect(darlehenPO.elems.sbForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonFreigeben).toBeHidden();
    await sbPage1.close();
  });

  test('Freigabestelle schliesst das Darlehen ab (Akzeptiert)', async () => {
    const darlehenPO = new DarlehenPO(sbPage2);

    // 1. Als Freigabestelle das Darlehen im Status IN_FREIGABE öffnen.
    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    // 2. Werte kontrollieren (Gewährung „Ja“, Betrag, Kommentar sind vorbelegt).
    await expect(
      darlehenPO.elems.gewaehrenGroup.getByRole('radio', { name: 'Ja' }),
    ).toBeChecked();
    await expect(darlehenPO.elems.kommentar).toHaveValue(
      darlehenSbGewaehren.kommentar!,
    );

    // 3. Schaltfläche „Abschliessen“ klicken.
    // 4. Im Bestätigungsdialog bestätigen.
    await darlehenPO.abschliessen();

    // Expected: Für den GS ist das Darlehen nur noch lesbar (Status AKZEPTIERT).
    const gsDarlehenPO = new DarlehenPO(gsPage1);
    await gsDarlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);
    await expect(gsDarlehenPO.elems.gsForm).toHaveClass(/readonly/);
    await expect(gsDarlehenPO.elems.buttonAbschliessen).toBeHidden();

    await sbPage2.close();
  });

  test('GS kann die Verfügung herunterladen', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);
    const dashboardPO = new GsDashboardPO(gsPage1);

    await dashboardPO.goToDashBoard();

    await expect(dashboardPO.getDarlehen(darlehenId)).not.toBeVisible();

    await dashboardPO.expectCanCreateDarlehen(true);

    await darlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);

    // Expected: Verfügung kann heruntergeladen werden.
    await darlehenPO.downloadDarlehenVerfuegung();

    await gsPage1.close();
  });
});
