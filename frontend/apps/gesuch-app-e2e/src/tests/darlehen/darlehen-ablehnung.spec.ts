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
  darlehenSbAblehnen,
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

test.describe('Freiwilligdarlehen Negativer Entscheid (Ablehnung)', () => {
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

  test('seed: Gültiges Gesuch vorbereiten und Darlehen als GS eingeben', async () => {
    await bringGesuchToEingereicht(gsPage1, getGesuchId()!, getTrancheId()!);

    const created = await eingebenDarlehenAsGs(gsPage1);
    darlehenId = created.darlehenId;
    fallId = created.fallId;
  });

  test('SB1: lehnt Gewährung ab und gibt frei', async () => {
    const darlehenPO = new DarlehenPO(sbPage1);

    // 1. Als SB das eingegebene Darlehen öffnen.
    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    // 2. In der Radiogruppe „Darlehen gewähren“ die Option „Nein“ wählen.
    await darlehenPO.selectGewaehren(false);

    // 3. Prüfen, dass das Betragsfeld deaktiviert und der Upload „Negative
    //    Verfügung“ eingeblendet wird.
    await expect(darlehenPO.elems.betragDarlehen).toBeHidden();

    // 4. Eine Datei als negative Verfügung hochladen.
    await darlehenPO.uploadNegativeVerfuegung();

    // 5. Kommentar ausfüllen.
    if (darlehenSbAblehnen.kommentar) {
      await darlehenPO.elems.kommentar.fill(darlehenSbAblehnen.kommentar);
    }

    // 6. „Freigeben“ klicken und im Dialog bestätigen.
    await darlehenPO.freigeben();

    // Expected: Nach der Freigabe ist das Darlehen in Freigabe (IN_FREIGABE).
    // und ohne V0_Freigabestelle sollte die form readonly sein.
    await expect(darlehenPO.elems.sbForm).toHaveClass(/readonly/);
    await sbPage1.close();
  });

  test('SB2: Freigabestelle schliesst mit Ablehnung ab', async () => {
    const darlehenPO = new DarlehenPO(sbPage2);

    // 1. Als Freigabestelle das Darlehen im Status IN_FREIGABE öffnen.
    await darlehenPO.goto(urls.sb, getGesuchId()!, darlehenId, fallId);
    await expect(darlehenPO.elems.sbForm).toBeVisible();

    // 2. Die hochgeladene negative Verfügung ist als Download-Link sichtbar.
    await expect(
      darlehenPO.elems.sbForm.getByText('small.png').first(),
    ).toBeVisible();

    // 3. „Abschliessen“ klicken und im Dialog bestätigen.
    await darlehenPO.abschliessen();
    await expect(darlehenPO.elems.sbForm).toHaveClass(/readonly/);
    await expect(darlehenPO.elems.buttonAbschliessen).toBeHidden();

    await sbPage2.close();
  });

  test('GS sieht das abgelehnte Darlehen als readonly', async () => {
    const gsDarlehenPO = new DarlehenPO(gsPage1);
    await gsDarlehenPO.goto(urls.gs, getGesuchId()!, darlehenId, fallId);
    await expect(gsDarlehenPO.elems.gsForm).toHaveClass(/readonly/);
    await expect(gsDarlehenPO.elems.buttonEingeben).toBeHidden();
    await gsPage1.close();
  });
});
