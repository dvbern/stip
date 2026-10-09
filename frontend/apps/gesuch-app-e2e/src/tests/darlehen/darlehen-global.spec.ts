import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

import {
  DarlehenPO,
  GSMainNavigationPO,
  GsDashboardPO,
  getE2eUrls,
  initializeMultiUserTest,
  setupGesuchWithApi,
} from '@dv/shared/util-fn/e2e-util';

import {
  ausbildungValues,
  createZahlungsverbindungUpdateFn,
  darlehenGs,
  darlehenSbAblehnen,
  darlehenSbGewaehren,
  gesuchFormularUpdateFn,
  grundOhneDokument,
} from './darlehen.data';
import {
  bringGesuchToEingereicht,
  createDarlehenAsGs,
  eingebenDarlehenAsGs,
} from './darlehen.seed';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn, createZahlungsverbindungUpdateFn),
);
const urls = getE2eUrls();

test.describe.configure({ mode: 'serial' });
test.slow();

test.describe('Freiwilligdarlehen', () => {
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

  test.afterAll(async () => {
    await gsPage1.close();
    await sbPage1.close();
    await sbPage2.close();
  });

  test('seed: Gültiges Gesuch vorbereiten', async () => {
    await bringGesuchToEingereicht(gsPage1, getGesuchId()!, getTrancheId()!);
  });

  test.describe('Freiwilligdarlehen Happy Path (Zurückweisung und Gewährung)', () => {
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

    test('SB weist das Darlehen zurück', async () => {
      const darlehenPO = new DarlehenPO(sbPage1);

      await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
      await expect(darlehenPO.elems.sbForm).toBeVisible();

      await darlehenPO.zurueckweisen(
        'E2E Zurückweisung: bitte Betrag korrigieren',
      );
    });

    test('GS sieht die Zurückweisung und gibt das Darlehen erneut ein', async () => {
      const darlehenPO = new DarlehenPO(gsPage1);
      await darlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);

      await expect(darlehenPO.elems.gsForm).not.toHaveClass(/readonly/);

      await darlehenPO.elems.betragGewuenscht.fill('5500');
      await darlehenPO.eingeben();

      await expect(darlehenPO.elems.gsForm).toHaveClass(/readonly/);
      await expect(darlehenPO.elems.buttonEingeben).toBeHidden();
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
    });
  });

  test.describe('Freiwilligdarlehen Negativer Entscheid (Ablehnung)', () => {
    test('seed: Darlehen als GS eingeben', async () => {
      const created = await eingebenDarlehenAsGs(gsPage1);
      darlehenId = created.darlehenId;
      fallId = created.fallId;
    });

    test('SB1: lehnt Gewährung ab und gibt frei', async () => {
      const darlehenPO = new DarlehenPO(sbPage1);

      await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
      await expect(darlehenPO.elems.sbForm).toBeVisible();

      await darlehenPO.selectGewaehren(false);

      await expect(darlehenPO.elems.betragDarlehen).toBeHidden();

      await darlehenPO.uploadNegativeVerfuegung();

      if (darlehenSbAblehnen.kommentar) {
        await darlehenPO.elems.kommentar.fill(darlehenSbAblehnen.kommentar);
      }

      await darlehenPO.freigeben();

      await expect(darlehenPO.elems.sbForm).toHaveClass(/readonly/);
    });

    test('SB2: Freigabestelle schliesst mit Ablehnung ab', async () => {
      const darlehenPO = new DarlehenPO(sbPage2);

      await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
      await expect(darlehenPO.elems.sbForm).toBeVisible();

      await expect(
        darlehenPO.elems.sbForm.getByText('small.png').first(),
      ).toBeVisible();

      await darlehenPO.abschliessen();
      await expect(darlehenPO.elems.sbForm).toHaveClass(/readonly/);
      await expect(darlehenPO.elems.buttonAbschliessen).toBeHidden();
    });

    test('GS sieht das abgelehnte Darlehen als readonly', async () => {
      const gsDarlehenPO = new DarlehenPO(gsPage1);
      await gsDarlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);
      await expect(gsDarlehenPO.elems.gsForm).toHaveClass(/readonly/);
      await expect(gsDarlehenPO.elems.buttonEingeben).toBeHidden();
    });
  });

  test.describe('Freiwilligdarlehen Löschen durch den GS', () => {
    test('seed: Darlehen erstellen', async () => {
      await createDarlehenAsGs(gsPage1);
    });

    test('GS löscht das Darlehen auf darlehen Form', async () => {
      const darlehenPO = new DarlehenPO(gsPage1);

      await expect(darlehenPO.elems.gsForm).toBeVisible();

      await darlehenPO.delete();

      await gsPage1.waitForURL(/dashboard/);
      await expect(darlehenPO.elems.gsForm).toBeHidden();
    });

    test.skip('GS löscht das Darlehen auf der Dashboard-Seite', async () => {
      const darlehenPO = new DarlehenPO(gsPage1);

      await expect(darlehenPO.elems.gsForm).toBeVisible();
      await darlehenPO.selectGrund('ZWEITAUSBILDUNG');

      // navigate away to test if confirmation dialog appears
      const gsMainNavigationPO = new GSMainNavigationPO(gsPage1);
      await gsMainNavigationPO.elems.dashboardLink.click();

      await expect(darlehenPO.elems.confirmDialog).toBeVisible();
      await darlehenPO.elems.dialogConfirm.click();

      await darlehenPO.delete();

      await gsPage1.waitForURL(/dashboard/);
      await expect(darlehenPO.elems.gsForm).toBeHidden();
    });
  });
});
