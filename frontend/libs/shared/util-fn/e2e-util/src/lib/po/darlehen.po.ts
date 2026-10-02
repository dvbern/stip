import { Locator, Page, expect } from '@playwright/test';

import { DarlehenGrund } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';

import { SmallImageFile } from '../files';

/**
 * Values used to fill the Gesuchsteller part of the Freiwilligdarlehen form.
 */
export interface DarlehenGsValues {
  betragGewuenscht?: number;
  schulden?: number;
  anzahlBetreibungen?: number;
}

/**
 * Values used to fill the Sachbearbeiter part of the Freiwilligdarlehen form.
 */
export interface DarlehenSbValues {
  gewaehren: boolean;
  betrag?: number;
  kommentar?: string;
}

/**
 * Page object for the shared Freiwilligdarlehen form. It covers both the
 * Gesuchsteller (GS) actions (erfassen, ausfüllen, Dokumente, eingeben, löschen)
 * and the Sachbearbeiter / Freigabestelle actions (gewähren, Betrag, Kommentar,
 * freigeben, zurückweisen, abschliessen).
 *
 * Navigation is app-agnostic: both the GS- and SB-App use the route
 * `/gesuch/:gesuchId/darlehen/:darlehenId/fall/:fallId`, only the base URL
 * differs, so the full URL is passed in from the spec.
 */
export class DarlehenPO {
  public elems: {
    page: Page;
    loading: Locator;

    // Gesuchsteller form
    gsForm: Locator;
    betragGewuenscht: Locator;
    schulden: Locator;
    anzahlBetreibungen: Locator;
    fieldSetGruende: Locator;
    grundNichtBerechtigt: Locator;
    grundAusbildungZwoelfJahre: Locator;
    grundHoheGebuehren: Locator;
    grundAnschaffungenFuerAusbildung: Locator;
    grundZweitausbildung: Locator;
    missingDocumentsError: Locator;
    buttonEingeben: Locator;
    buttonDelete: Locator;

    // Sachbearbeiter / Freigabestelle form
    sbForm: Locator;
    gewaehrenGroup: Locator;
    betragDarlehen: Locator;
    kommentar: Locator;
    buttonFreigeben: Locator;
    buttonZurueckweisen: Locator;
    buttonAbschliessen: Locator;

    // Dialogs
    dialogConfirm: Locator;
    dialogCancel: Locator;
    kommentarDialogInput: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-darlehen-loading'),

      gsForm: page.getByTestId('form-darlehen-gs-form'),
      betragGewuenscht: page.getByTestId('form-darlehen-betragGewuenscht'),
      schulden: page.getByTestId('form-darlehen-schulden'),
      anzahlBetreibungen: page.getByTestId('form-darlehen-anzahlBetreibungen'),
      fieldSetGruende: page.getByTestId('form-darlehen-gruende'),
      grundNichtBerechtigt: page.getByTestId(
        'form-darlehen-grundNichtBerechtigt',
      ),
      grundAusbildungZwoelfJahre: page.getByTestId(
        'form-darlehen-grundAusbildungZwoelfJahre',
      ),
      grundHoheGebuehren: page.getByTestId('form-darlehen-grundHoheGebuehren'),
      grundAnschaffungenFuerAusbildung: page.getByTestId(
        'form-darlehen-grundAnschaffungenFuerAusbildung',
      ),
      grundZweitausbildung: page.getByTestId(
        'form-darlehen-grundZweitausbildung',
      ),
      missingDocumentsError: page.getByTestId(
        'form-darlehen-missing-documents-error',
      ),
      buttonEingeben: page.getByTestId('button-eingeben-darlehen'),
      buttonDelete: page.getByTestId('button-delete-darlehen'),

      sbForm: page.getByTestId('form-darlehen-sb-form'),
      gewaehrenGroup: page.getByTestId('form-darlehen-darlehenGewaehren'),
      betragDarlehen: page.getByTestId('form-darlehen-betragDarlehen'),
      kommentar: page.getByTestId('form-darlehen-kommentar'),
      buttonFreigeben: page.getByTestId('button-darlehen-freigeben'),
      buttonZurueckweisen: page.getByTestId('button-darlehen-zurueckweisen'),
      buttonAbschliessen: page.getByTestId('button-darlehen-abschliessen'),

      dialogConfirm: page.getByTestId('dialog-confirm'),
      dialogCancel: page.getByTestId('dialog-cancel'),
      kommentarDialogInput: page.getByTestId('form-kommentar-dialog-kommentar'),
    };
  }

  /**
   * Navigate to a specific Darlehen using the full (app-prefixed) base URL.
   *
   * Also waits for the loading indicator to be hidden.
   */
  public async goto(
    baseUrl: string,
    gesuchId: string,
    darlehenId: string,
    fallId: string,
  ) {
    await this.elems.page.goto(
      `${baseUrl}/gesuch/${gesuchId}/darlehen/${darlehenId}/fall/${fallId}`,
    );
    await expect(this.elems.loading).toBeHidden(); // use waitFor instead of expect for the whole suit?
  }

  // --- Gesuchsteller actions ---

  public async fillGsForm(darlehen: DarlehenGsValues) {
    if (isDefined(darlehen.betragGewuenscht)) {
      await this.elems.betragGewuenscht.fill(`${darlehen.betragGewuenscht}`);
    }
    if (isDefined(darlehen.schulden)) {
      await this.elems.schulden.fill(`${darlehen.schulden}`);
    }
    if (isDefined(darlehen.anzahlBetreibungen)) {
      await this.elems.anzahlBetreibungen.fill(
        `${darlehen.anzahlBetreibungen}`,
      );
    }
  }

  public grundCheckbox(grund: DarlehenGrund): Locator {
    switch (grund) {
      case 'NICHT_BERECHTIGT':
        return this.elems.grundNichtBerechtigt;
      case 'AUSBILDUNG_ZWOELF_JAHRE':
        return this.elems.grundAusbildungZwoelfJahre;
      case 'HOHE_GEBUEHREN':
        return this.elems.grundHoheGebuehren;
      case 'ANSCHAFFUNGEN_FUER_AUSBILDUNG':
        return this.elems.grundAnschaffungenFuerAusbildung;
      case 'ZWEITAUSBILDUNG':
        return this.elems.grundZweitausbildung;
    }
  }

  public async selectGrund(grund: DarlehenGrund) {
    await this.grundCheckbox(grund).getByRole('checkbox').check();
  }

  /**
   * Upload a Darlehen document of the given type via the document-upload dialog.
   */
  public async uploadDokument(dokumentTyp: DarlehenGrundDokument) {
    const uploadButton = this.elems.page.getByTestId(
      `button-document-upload-${dokumentTyp}`,
    );
    await uploadButton.scrollIntoViewIfNeeded();
    await uploadButton.click();

    const uploadCall = this.elems.page.waitForResponse(
      (response) =>
        response.url().includes('/api/v1/darlehen/dokument') &&
        response.request().method() === 'POST',
    );
    await this.elems.page
      .getByTestId('file-input')
      .setInputFiles(SmallImageFile);
    await uploadCall;
    await this.elems.page.keyboard.press('Escape');
    await expect(this.elems.page.getByTestId('file-input')).toHaveCount(0);
  }

  /**
   * Click "Eingeben" and confirm the dialog, waiting for the eingeben response.
   */
  public async eingeben() {
    await this.elems.buttonEingeben.click();
    const eingebenResponse = this.elems.page.waitForResponse(
      (response) =>
        /\/api\/v1\/darlehen\/.*\/eingeben/.test(response.url()) &&
        response.request().method() === 'POST',
    );
    await this.elems.dialogConfirm.click();
    await eingebenResponse;
    await expect(this.elems.loading).toBeHidden();
  }

  /**
   * Click "Löschen" and confirm the dialog, waiting for the delete response.
   */
  public async delete() {
    await this.elems.buttonDelete.click();
    const deleteResponse = this.elems.page.waitForResponse(
      (response) =>
        /\/api\/v1\/darlehen\/.*\/gs/.test(response.url()) &&
        response.request().method() === 'DELETE',
    );
    await this.elems.dialogConfirm.click();
    await deleteResponse;
  }

  // --- Sachbearbeiter / Freigabestelle actions ---

  public async selectGewaehren(gewaehren: boolean) {
    await this.elems.gewaehrenGroup
      .getByRole('radio', { name: gewaehren ? 'Ja' : 'Nein' })
      .check();
  }

  public async fillSbForm(darlehen: DarlehenSbValues) {
    await this.selectGewaehren(darlehen.gewaehren);
    if (darlehen.gewaehren && isDefined(darlehen.betrag)) {
      await this.elems.betragDarlehen.fill(`${darlehen.betrag}`);
    }
    if (isDefined(darlehen.kommentar)) {
      await this.elems.kommentar.fill(darlehen.kommentar);
    }
  }

  /**
   * Upload the negative Verfügung file for a rejected ("Nein") Darlehen.
   */
  public async uploadNegativeVerfuegung() {
    await this.elems.sbForm
      .getByTestId('file-input')
      .setInputFiles(SmallImageFile);
  }

  public async freigeben() {
    await this.elems.buttonFreigeben.click();
    const freigebenResponse = this.elems.page.waitForResponse(
      (response) =>
        /\/api\/v1\/darlehen\/.*\/freigeben/.test(response.url()) &&
        response.request().method() === 'POST',
    );
    await this.elems.dialogConfirm.click();
    await freigebenResponse;
    // await expect(this.elems.loading).toBeHidden();
    await this.elems.loading.waitFor({ state: 'hidden' });
  }

  public async zurueckweisen(kommentar: string) {
    await this.elems.buttonZurueckweisen.click();
    await this.elems.kommentarDialogInput.fill(kommentar);
    const zurueckweisenResponse = this.elems.page.waitForResponse(
      (response) =>
        /\/api\/v1\/darlehen\/.*\/zurueckweisen/.test(response.url()) &&
        response.request().method() === 'POST',
    );
    await this.elems.dialogConfirm.click();
    await zurueckweisenResponse;
    await expect(this.elems.loading).toBeHidden();
  }

  public async abschliessen() {
    await this.elems.buttonAbschliessen.click();
    const abschliessenResponse = this.elems.page.waitForResponse(
      (response) =>
        /\/api\/v1\/darlehen\/.*\/(akzeptieren|ablehnen)/.test(
          response.url(),
        ) && response.request().method() === 'POST',
    );
    await this.elems.dialogConfirm.click();
    await abschliessenResponse;
    await expect(this.elems.loading).toBeHidden();
  }
}

type DarlehenGrundDokument =
  | 'BETREIBUNGS_AUSZUG'
  | 'AUFSTELLUNG_KOSTEN_ELTERN'
  | 'KOPIE_SCHULGELDRECHNUNG'
  | 'BELEGE_ANSCHAFFUNGEN';
