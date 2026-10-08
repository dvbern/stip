import { Locator, Page, expect } from '@playwright/test';

import { Gesuchstatus } from '@dv/shared/model/gesuch';

import { AusbildungPO, AusbildungValues } from './ausbildung.po';

export class gsDashboardPO {
  public elems: {
    page: Page;
    dashboardWelcomeTitle: Locator;

    // dashboard
    createAusbildung: Locator;
    ausbildungItem: Locator;

    // Ausbildung
    ausbildungTitle: Locator;
    ausbildungDelete: Locator;

    // Gesuch
    gesuchEdit: Locator;
    gesuchDelete: Locator;
    gesuchOpen: Locator;
    gesuchUploadMissingDocuments: Locator;
    gesuchAusbildungsjahrTitle: Locator;
    gesuchItem: Locator;
    gesuchOrderType: Locator; // erstgesuch | folgegesuch
    getGesuchStatus: (gesuchStatus: Gesuchstatus) => Locator; // dynamic based on gesuch.gesuchStatus
    gesuchEinreichefristReduziertTitle: Locator;
    gesuchEinreichefristReduziertRemaining: Locator;
    gesuchEinreichefristNormalTitle: Locator;
    gesuchEinreichefristNormalRemaining: Locator;
    gesuchEinreichefristAbgelaufenWarning: Locator;
    gesuchNachfristFehlendeDokumenteTitle: Locator;
    gesuchNachfristFehlendeDokumenteRemaining: Locator;

    // subitem status (common for both Aenderung and Darlehen)
    gesuchSubitemStatus: Locator;

    // Aenderung
    createAenderung: Locator;
    gesuchSubitemAenderung: Locator;
    aenderungEdit: Locator;
    aenderungDelete: Locator;

    // Darlehen
    gesuchSubitemDarlehen: Locator;
    darlehenEdit: Locator;
    darlehenDelete: Locator;

    buttonCancel: Locator;
    buttonAcceptULA: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      dashboardWelcomeTitle: page.getByTestId('dashboard-welcome-title'),
      createAusbildung: page.getByTestId('dashboard-ausbildung-create'),
      createAenderung: page.getByTestId('dashboard-aenderung-create'),
      ausbildungItem: page.getByTestId('dashboard-ausbildung-item'),

      gesuchEdit: page.getByTestId('dashboard-gesuch-edit'),
      gesuchDelete: page.getByTestId('dashboard-gesuch-delete'),
      gesuchOpen: page.getByTestId('dashboard-gesuch-open'),
      gesuchUploadMissingDocuments: page.getByTestId(
        'dashboard-gesuch-upload-missing-documents',
      ),
      ausbildungTitle: page.getByTestId('dashboard-ausbildung-title'),
      ausbildungDelete: page.getByTestId('dashboard-ausbildung-delete'),
      gesuchAusbildungsjahrTitle: page.getByTestId(
        'dashboard-gesuch-ausbildungsjahr-title',
      ),
      gesuchItem: page.getByTestId('dashboard-gesuch-item'),
      gesuchOrderType: page.getByTestId('dashboard-gesuch-order-type'),
      getGesuchStatus: (gesuchStatus: Gesuchstatus) =>
        page.getByTestId(`dashboard-gesuch-status-${gesuchStatus}`),
      gesuchEinreichefristReduziertTitle: page.getByTestId(
        'dashboard-gesuch-einreichefrist-reduziert-title',
      ),
      gesuchEinreichefristReduziertRemaining: page.getByTestId(
        'dashboard-gesuch-einreichefrist-reduziert-remaining',
      ),
      gesuchEinreichefristNormalTitle: page.getByTestId(
        'dashboard-gesuch-einreichefrist-normal-title',
      ),
      gesuchEinreichefristNormalRemaining: page.getByTestId(
        'dashboard-gesuch-einreichefrist-normal-remaining',
      ),
      gesuchEinreichefristAbgelaufenWarning: page.getByTestId(
        'dashboard-gesuch-einreichefrist-abgelaufen-warning',
      ),
      gesuchNachfristFehlendeDokumenteTitle: page.getByTestId(
        'dashboard-gesuch-nachfrist-fehlende-dokumente-title',
      ),
      gesuchNachfristFehlendeDokumenteRemaining: page.getByTestId(
        'dashboard-gesuch-nachfrist-fehlende-dokumente-remaining',
      ),

      gesuchSubitemStatus: page.getByTestId('dashboard-gesuch-subitem-status'),
      gesuchSubitemAenderung: page.getByTestId(
        'dashboard-gesuch-subitem-aenderung',
      ),
      aenderungEdit: page.getByTestId('dashboard-aenderung-edit'),
      aenderungDelete: page.getByTestId('dashboard-aenderung-delete'),

      gesuchSubitemDarlehen: page.getByTestId(
        'dashboard-gesuch-subitem-darlehen',
      ),
      darlehenEdit: page.getByTestId('dashboard-darlehen-edit'),
      darlehenDelete: page.getByTestId('dashboard-darlehen-delete'),

      buttonCancel: page.getByTestId('cancel-button'),
      buttonAcceptULA: page.getByTestId('accept-button'),
    };
  }

  public async goToDashBoard() {
    await this.elems.page.goto('/dashboard');
  }

  public async createNewStipendium(ausbildung: AusbildungValues) {
    await this.elems.createAusbildung.click();

    const hasULAialog = await this.elems.page.isVisible(
      '[data-testid="shared-dialog-nutzungsbedingungen"]',
    );
    if (hasULAialog) {
      const ulaPromise = this.elems.page.waitForResponse(
        '**/api/v1/benutzer/nutzungsbedingungenAkzeptieren/*',
      );
      await this.elems.buttonAcceptULA.click();
      await ulaPromise;
      await this.elems.createAusbildung.click();
    }

    const ausbildungPO = new AusbildungPO(this.elems.page);

    expect(ausbildungPO.elems.form).toBeVisible();

    await expect(ausbildungPO.elems.loading).toBeHidden();

    await ausbildungPO.fillEducationForm(ausbildung);

    await ausbildungPO.elems.buttonSave.click();
  }
}
