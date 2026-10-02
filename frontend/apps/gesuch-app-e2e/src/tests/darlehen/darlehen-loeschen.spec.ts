// spec: .github/e2e-plans/gesuch-app_freiwilligdarlehen_plan.md

import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

import {
  ConfirmDialogPO,
  DarlehenPO,
  GSMainNavigationPO,
  getE2eUrls,
  initializeMultiUserTest,
  setupGesuchWithApi,
} from '@dv/shared/util-fn/e2e-util';

import {
  ausbildungValues,
  createZahlungsverbindungUpdateFn,
  gesuchFormularUpdateFn,
} from './darlehen.data';
import { bringGesuchToEingereicht, createDarlehenAsGs } from './darlehen.seed';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn, createZahlungsverbindungUpdateFn),
);
const urls = getE2eUrls();

test.describe.configure({ mode: 'serial' });
test.slow();

// todo: delete can only happen from the dashboard! (issue ?)
test.describe('Freiwilligdarlehen Löschen durch den GS', () => {
  let gsPage1: Page;

  test.beforeAll(async ({ gsPage }) => {
    gsPage1 = gsPage;
  });

  test('seed: Gültiges Gesuch vorbereiten und Darlehen erstellen', async () => {
    await bringGesuchToEingereicht(gsPage1, getGesuchId()!, getTrancheId()!);

    await createDarlehenAsGs(gsPage1);
  });

  test('GS löscht das Darlehen auf darlehen Form', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);

    // 1. Als GS das im Status IN_BEARBEITUNG_GS befindliche Darlehen öffnen
    //    (aus Seed bereits offen).
    await expect(darlehenPO.elems.gsForm).toBeVisible();

    // 2. Schaltfläche „Löschen“ klicken.
    // 3. Im Bestätigungsdialog bestätigen.
    await darlehenPO.delete();

    // Expected: Der GS wird auf die Startseite/Cockpit zurückgeleitet und das
    // Darlehen-Formular ist nicht mehr sichtbar.
    await gsPage1.waitForURL(`${urls.gs}/dashboard`);
    await expect(darlehenPO.elems.gsForm).toBeHidden();

    await gsPage1.close();
  });

  test.skip('GS löscht das Darlehen auf der Dashboard-Seite', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);

    // 1. Als GS das im Status IN_BEARBEITUNG_GS befindliche Darlehen öffnen
    //    (aus Seed bereits offen).
    await expect(darlehenPO.elems.gsForm).toBeVisible();
    await darlehenPO.selectGrund('ZWEITAUSBILDUNG');

    // navigate away to test if confirmation dialog appears
    const gsMainNavigationPO = new GSMainNavigationPO(gsPage1);
    await gsMainNavigationPO.elems.dashboardLink.click();

    const confirmDialogPO = new ConfirmDialogPO(gsPage1);
    await expect(confirmDialogPO.elems.dialog).toBeVisible();
    await confirmDialogPO.elems.confirmButton.click();

    // todo: finish with dashboard checks

    // 2. Schaltfläche „Löschen“ klicken.
    // 3. Im Bestätigungsdialog bestätigen.
    await darlehenPO.delete();

    // Expected: Der GS wird auf die Startseite/Cockpit zurückgeleitet und das
    // Darlehen-Formular ist nicht mehr sichtbar.
    await gsPage1.waitForURL(`${urls.gs}/dashboard`);
    await expect(darlehenPO.elems.gsForm).toBeHidden();

    await gsPage1.close();
  });
});
