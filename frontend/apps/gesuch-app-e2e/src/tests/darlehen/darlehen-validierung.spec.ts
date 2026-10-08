import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

import {
  DarlehenPO,
  initializeMultiUserTest,
  setupGesuchWithApi,
} from '@dv/shared/util-fn/e2e-util';

import {
  ausbildungValues,
  createZahlungsverbindungUpdateFn,
  darlehenGs,
  gesuchFormularUpdateFn,
  grundMitDokument,
} from './darlehen.data';
import { bringGesuchToEingereicht, createDarlehenAsGs } from './darlehen.seed';

const { test, getGesuchId, getTrancheId } = initializeMultiUserTest(
  ausbildungValues,
  setupGesuchWithApi(gesuchFormularUpdateFn, createZahlungsverbindungUpdateFn),
);

test.describe.configure({ mode: 'serial' });
test.slow();

// Todo: Diese Tests sind besser als Component Testst auszuführen
test.describe.skip('Freiwilligdarlehen GS-Formular Validierung', () => {
  let gsPage1: Page;

  test.beforeAll(async ({ gsPage }) => {
    gsPage1 = gsPage;
  });

  test('seed: Gültiges Gesuch vorbereiten und leeres Darlehen erstellen', async () => {
    await bringGesuchToEingereicht(gsPage1, getGesuchId()!, getTrancheId()!);

    await createDarlehenAsGs(gsPage1);
  });

  test('Eingeben mit leerem Formular schlägt fehl', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);

    // 1. Als GS ein neues, leeres Darlehen-Formular öffnen (aus Seed).
    // 2. Ohne Eingaben „Eingeben“ klicken.
    await darlehenPO.elems.buttonEingeben.click();

    // Expected: Pflichtfeld-Fehlermeldungen werden angezeigt, kein Dialog,
    // Status bleibt IN_BEARBEITUNG_GS.
    await expect(darlehenPO.elems.gsForm).toHaveClass(/ng-invalid/);
    await expect(
      darlehenPO.elems.gsForm.locator('mat-error').first(),
    ).toBeVisible();
    await expect(darlehenPO.elems.dialogConfirm).toBeHidden();
    await expect(darlehenPO.elems.buttonEingeben).toBeVisible();
  });

  test('Fehlender Grund verhindert das Eingeben', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);

    // 1. Beträge und Anzahl Betreibungen gültig ausfüllen, aber keinen Grund
    //    aktivieren.
    await darlehenPO.fillGsForm(darlehenGs);

    // 2. Betreibungsauszug hochladen.
    await darlehenPO.uploadDokument('BETREIBUNGS_AUSZUG');

    // 3. „Eingeben“ klicken.
    await darlehenPO.elems.buttonEingeben.click();

    // Expected: Die Gründe-Gruppe zeigt einen Validierungsfehler, das Eingeben
    // wird verhindert.
    await expect(darlehenPO.elems.fieldSetGruende).toHaveClass(/ng-invalid/);
    await expect(darlehenPO.elems.dialogConfirm).toBeHidden();
    await expect(darlehenPO.elems.buttonEingeben).toBeVisible();
  });

  test('Fehlende Pflichtdokumente verhindern das Eingeben', async () => {
    const darlehenPO = new DarlehenPO(gsPage1);

    // 1. Alle Pflichtfelder ausfüllen und einen Grund wählen, der ein
    //    Zusatzdokument erfordert (Hohe Gebühren) - Zusatzdokument NICHT
    //    hochladen.
    await darlehenPO.selectGrund(grundMitDokument);

    // 3. „Eingeben“ klicken.
    await darlehenPO.elems.buttonEingeben.click();

    // Expected: „Fehlende Dokumente“ wird angezeigt, das Eingeben wird
    // verhindert.
    await expect(darlehenPO.elems.missingDocumentsError).toBeVisible();
    await expect(darlehenPO.elems.dialogConfirm).toBeHidden();
    await expect(darlehenPO.elems.buttonEingeben).toBeVisible();

    await gsPage1.close();
  });
});
