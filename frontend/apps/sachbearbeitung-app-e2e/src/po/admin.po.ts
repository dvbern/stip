import { Locator, Page, expect } from '@playwright/test';

import { Sozialdienst } from '@dv/shared/model/gesuch';

export class AdminPO {
  public elems: {
    page: Page;
    adminLink: Locator;
    sozialdienst: {
      link: Locator;
      filter: Locator;
      createButton: Locator;
      saveButton: Locator;
      rows: Locator;
      detail: {
        form: Locator;
        name: Locator;
        iban: Locator;
        strasse: Locator;
        hausnummer: Locator;
        plz: Locator;
        ort: Locator;
        coAdresse: Locator;
        land: Locator;
        nachname: Locator;
        vorname: Locator;
        email: Locator;
      };
    };
  };

  constructor(public page: Page) {
    this.elems = {
      page,
      adminLink: page
        .getByTestId('gesuch-step-nav-administration')
        .locator('visible=true'),
      sozialdienst: {
        link: page
          .getByTestId('option-nav-sozialdienste')
          .locator('visible=true'),
        filter: page.getByTestId('sozialdienst-filter-name'),
        createButton: page.getByTestId('sozialdienst-create'),
        saveButton: page.getByTestId('button-save'),
        rows: page.getByRole('row'),
        detail: {
          form: page.getByTestId('form-sozialdienst-form'),
          name: page.getByTestId('form-sozialdienst-name'),
          iban: page.getByTestId('form-sozialdienst-iban'),
          strasse: page.getByTestId('form-address-strasse'),
          hausnummer: page.getByTestId('form-address-hausnummer'),
          plz: page.getByTestId('form-address-plz'),
          ort: page.getByTestId('form-address-ort'),
          coAdresse: page.getByTestId('form-address-coAdresse'),
          land: page.getByTestId('form-address-land'),
          nachname: page.getByTestId('form-sozialdienst-nachname'),
          vorname: page.getByTestId('form-sozialdienst-vorname'),
          email: page.getByTestId('form-sozialdienst-email'),
        },
      },
    };
  }

  goToAdmin = async () => {
    await this.elems.adminLink.click();
  };

  sozialdienste = {
    goToOverview: async () => {
      await this.elems.sozialdienst.link.click();
    },
    goToCreate: async () => {
      await this.elems.sozialdienst.link.click();
      await this.elems.sozialdienst.createButton.click();
    },
    filter: async (name: string) => {
      await this.elems.sozialdienst.filter.fill(name);
    },
    fillOutNewSozialdienstForm: async (
      sozialdienst?: Partial<Sozialdienst>,
    ) => {
      const randId = Math.round(Math.random() * 100000);
      const email = `test-sozialdienst-${randId}@mailbucket.dvbern.ch`;
      await this.elems.sozialdienst.detail.name.fill(
        sozialdienst?.name ?? 'Sozialdienst 1',
      );
      await this.elems.sozialdienst.detail.iban.fill(
        sozialdienst?.iban ?? 'CH9300762011623852957',
      );
      await this.elems.sozialdienst.detail.strasse.fill(
        sozialdienst?.adresse?.strasse ?? 'Musterstrasse',
      );
      await this.elems.sozialdienst.detail.hausnummer.fill(
        sozialdienst?.adresse?.hausnummer ?? '1',
      );
      await this.elems.sozialdienst.detail.plz.fill(
        sozialdienst?.adresse?.plz ?? '1234',
      );
      await this.elems.sozialdienst.detail.ort.fill(
        sozialdienst?.adresse?.ort ?? 'Musterort',
      );
      await this.elems.sozialdienst.detail.coAdresse?.fill(
        sozialdienst?.adresse?.coAdresse ?? 'c/o Muster',
      );
      await this.elems.sozialdienst.detail.nachname.fill(
        sozialdienst?.sozialdienstAdmin?.nachname ?? 'Muster',
      );
      await this.elems.sozialdienst.detail.vorname.fill(
        sozialdienst?.sozialdienstAdmin?.vorname ?? `e2e-${randId}`,
      );
      await this.elems.sozialdienst.detail.email.fill(email);
      return email;
    },
    save: async () => {
      const createSuccessPromise = this.page.waitForResponse(
        (response) =>
          response.request().method() === 'POST' &&
          response.url().includes('/api/v1/sozialdienst'),
      );
      await this.elems.sozialdienst.saveButton.click();
      await createSuccessPromise;
    },
    deleteSozialdienst: async (name: string) => {
      await this.elems.sozialdienst.filter.fill(name);
      await expect(this.elems.sozialdienst.rows).toHaveCount(2);
      await this.elems.sozialdienst.rows
        .getByTestId('sozialdienst-delete')
        .click();
    },
  };
}
