import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

import {
  DarlehenPO,
  GSMainNavigationPO,
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

test.describe.configure({ mode: 'serial' });
test.slow();

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

    await expect(darlehenPO.elems.gsForm).toBeVisible();

    await darlehenPO.delete();

    await gsPage1.waitForURL(/dashboard/);
    await expect(darlehenPO.elems.gsForm).toBeHidden();

    await gsPage1.close();
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

    await gsPage1.close();
  });
});
