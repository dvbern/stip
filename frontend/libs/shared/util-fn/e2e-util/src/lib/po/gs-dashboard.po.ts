import { Locator, Page, expect } from '@playwright/test';

import { FreiwilligDarlehen, Gesuchstatus } from '@dv/shared/model/gesuch';

import { AusbildungPO, AusbildungValues } from './ausbildung.po';

export class GsDashboardPO {
  public elems: {
    page: Page;
    dashboardWelcomeTitle: Locator;

    // dashboard
    createAusbildung: Locator;
    ausbildungItem: Locator;
    dashboardLoading: Locator;

    // Ausbildung
    ausbildungTitle: Locator;
    ausbildungDelete: Locator;

    // Gesuch (first default gesuch)
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

    // Aenderung (there can / should only be one shown on dashboard)
    createAenderung: Locator;
    gesuchSubitemAenderung: Locator;
    aenderungEdit: Locator;
    aenderungDelete: Locator;
    aenderungGueltigAb: Locator; // tranche-erstellen dialog
    aenderungKommentar: Locator; // tranche-erstellen dialog

    // Darlehen
    createDarlehen: Locator;
    gesuchSubitemDarlehen: Locator;
    darlehenEdit: Locator;
    darlehenDelete: Locator;

    // Dialogs
    dialogConfirm: Locator;
    dialogCancel: Locator;

    buttonCancel: Locator;
    buttonAcceptULA: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      dashboardWelcomeTitle: page.getByTestId('dashboard-welcome-title'),
      createAusbildung: page.getByTestId('dashboard-ausbildung-create'),
      dashboardLoading: page.getByTestId('dashboard-loading'),
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
      aenderungGueltigAb: page.getByTestId(
        'form-aenderung-melden-dialog-gueltig-ab',
      ),
      aenderungKommentar: page.getByTestId(
        'form-aenderung-melden-dialog-kommentar',
      ),

      createDarlehen: page.getByTestId('dashboard-darlehen-create'),
      gesuchSubitemDarlehen: page.getByTestId(
        'dashboard-gesuch-subitem-darlehen',
      ),
      darlehenEdit: page.getByTestId('dashboard-darlehen-edit'),
      darlehenDelete: page.getByTestId('dashboard-darlehen-delete'),

      dialogConfirm: page.getByTestId('dialog-confirm'),
      dialogCancel: page.getByTestId('dialog-cancel'),

      buttonCancel: page.getByTestId('cancel-button'),
      buttonAcceptULA: page.getByTestId('accept-button'),
    };
  }

  public async goToDashBoard() {
    await this.elems.page.goto('/dashboard');
    await this.waitForDashboard();
  }

  public async waitForDashboard() {
    await expect(this.elems.dashboardWelcomeTitle).toBeVisible();
    await expect(this.elems.dashboardLoading).toBeHidden();
  }

  public getGesuch(gesuchId: string): Locator {
    return this.elems.gesuchItem.filter({
      has: this.elems.page.locator(`a[href*="/gesuch/${gesuchId}/"]`),
    });
  }

  public getAusbildung(gesuchId: string): Locator {
    return this.elems.ausbildungItem.filter({
      has: this.getGesuch(gesuchId),
    });
  }

  // todo: there shall only be one shown on the dashboard at a time
  public getAenderung(aenderungId: string): Locator {
    return this.elems.gesuchSubitemAenderung.filter({
      has: this.elems.page.locator(`a[href$="/aenderung/${aenderungId}"]`),
    });
  }

  public getDarlehen(darlehenId: string): Locator {
    return this.elems.gesuchSubitemDarlehen.filter({
      has: this.elems.page.locator(`a[href*="/darlehen/${darlehenId}/fall/"]`),
    });
  }

  public getGesuchSubitems(gesuch: Locator = this.elems.gesuchItem) {
    const group = gesuch.locator('..');
    return {
      aenderung: group.getByTestId('dashboard-gesuch-subitem-aenderung'),
      darlehen: group.getByTestId('dashboard-gesuch-subitem-darlehen'),
    };
  }

  // --- Actions ---

  // there can be multiple Gesuche on the dash
  public async openGesuch(gesuch: Locator = this.elems.gesuchItem) {
    const link = gesuch.getByTestId('dashboard-gesuch-open');

    await link.click();
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

    await expect(ausbildungPO.elems.form).toBeVisible();

    await expect(ausbildungPO.elems.loading).toBeHidden();

    await ausbildungPO.fillEducationForm(ausbildung);

    await ausbildungPO.elems.buttonSave.click();
  }

  /**
   * Click "Gesuch löschen" and confirm the dialog, waiting for the delete response.
   */
  public async deleteGesuch() {
    await this.elems.gesuchDelete.click();
    await this.confirmAndWaitForDelete(/\/api\/v1\/gesuch\/[^/]+$/);
  }

  /**
   * Click "Ausbildung löschen" and confirm the dialog. The backend deletes the
   * first Gesuch of the Ausbildung (same endpoint as `deleteGesuch`).
   */
  public async deleteAusbildung() {
    await this.elems.ausbildungDelete.click();
    await this.confirmAndWaitForDelete(/\/api\/v1\/gesuch\/[^/]+$/);
  }

  /**
   * Open the "Aenderung melden" dialog, fill it and confirm it.
   * `gueltigAb` has to be in the dialog's date format (dd.MM.yyyy).
   */
  public async createAenderung(values: {
    gueltigAb: string;
    kommentar?: string;
  }) {
    await this.elems.createAenderung.click();
    await this.elems.aenderungGueltigAb.fill(values.gueltigAb);
    if (values.kommentar) {
      await this.elems.aenderungKommentar.fill(values.kommentar);
    }
    const createResponse = this.elems.page.waitForResponse(
      (response) =>
        /\/api\/v1\/gesuchtranche\/[^/]+\/aenderungsantrag/.test(
          response.url(),
        ) && response.request().method() === 'POST',
    );
    await this.elems.dialogConfirm.click();
    await createResponse;
  }

  /**
   * Click "Aenderung löschen" and confirm the dialog, waiting for the delete response.
   */
  public async deleteAenderung() {
    await this.elems.aenderungDelete.click();
    await this.confirmAndWaitForDelete(
      /\/api\/v1\/gesuchtranche\/[^/]+\/aenderung\/delete/,
    );
  }

  /**
   * Create a new Freiwilligdarlehen. Returns the created darlehen; the app
   * navigates to the darlehen form afterwards.
   */
  public async createDarlehen(): Promise<FreiwilligDarlehen> {
    const createResponse = this.elems.page.waitForResponse(
      (response) =>
        /\/api\/v1\/darlehen\/[^/]+$/.test(response.url()) &&
        response.request().method() === 'POST',
    );
    await this.elems.createDarlehen.click();
    return (await createResponse).json();
  }

  /**
   * Click "Darlehen löschen" and confirm the dialog, waiting for the delete response.
   */
  public async deleteDarlehen() {
    await this.elems.darlehenDelete.click();
    await this.confirmAndWaitForDelete(/\/api\/v1\/darlehen\/[^/]+\/gs/);
  }

  // --- Checks ---

  public async expectGesuchStatus(
    status: Gesuchstatus,
    gesuch: Locator = this.elems.gesuchItem,
  ) {
    await expect(
      gesuch.getByTestId(`dashboard-gesuch-status-${status}`),
    ).toBeVisible();
  }

  public async expectSubitemStatus(subitem: Locator, status: string | RegExp) {
    await expect(
      subitem.getByTestId('dashboard-gesuch-subitem-status'),
    ).toHaveText(status);
  }

  /**
   * Check which Einreichefrist info is shown on the Gesuch. Only the info for
   * the given type is expected to be visible.
   */
  public async expectEinreichefrist(
    type: 'reduziert' | 'normal' | 'abgelaufen' | 'none',
  ) {
    const { elems } = this;
    await expect(elems.gesuchEinreichefristReduziertTitle).toBeVisible({
      visible: type === 'reduziert',
    });
    await expect(elems.gesuchEinreichefristReduziertRemaining).toBeVisible({
      visible: type === 'reduziert',
    });
    await expect(elems.gesuchEinreichefristNormalTitle).toBeVisible({
      visible: type === 'normal',
    });
    await expect(elems.gesuchEinreichefristNormalRemaining).toBeVisible({
      visible: type === 'normal',
    });
    await expect(elems.gesuchEinreichefristAbgelaufenWarning).toBeVisible({
      visible: type === 'abgelaufen',
    });
  }

  public async expectNachfristFehlendeDokumente(visible = true) {
    await expect(this.elems.gesuchNachfristFehlendeDokumenteTitle).toBeVisible({
      visible,
    });
    await expect(
      this.elems.gesuchNachfristFehlendeDokumenteRemaining,
    ).toBeVisible({ visible });
  }

  public async expectCanEditGesuch(canEdit = true) {
    await expect(this.elems.gesuchEdit).toBeVisible({ visible: canEdit });
  }

  public async expectCanDeleteGesuch(canDelete = true) {
    await expect(this.elems.gesuchDelete).toBeVisible({ visible: canDelete });
  }

  public async expectCanCreateAenderung(canCreate = true) {
    await expect(this.elems.createAenderung).toBeVisible({
      visible: canCreate,
    });
  }

  public async expectCanCreateDarlehen(canCreate = true) {
    await expect(this.elems.createDarlehen).toBeVisible({ visible: canCreate });
  }

  public async expectOffeneAenderung(visible = true) {
    await expect(this.elems.gesuchSubitemAenderung).toBeVisible({ visible });
  }

  private async confirmAndWaitForDelete(urlPattern: RegExp) {
    const deleteResponse = this.elems.page.waitForResponse(
      (response) =>
        urlPattern.test(response.url()) &&
        response.request().method() === 'DELETE',
    );
    await this.elems.dialogConfirm.click();
    await deleteResponse;
  }
}
