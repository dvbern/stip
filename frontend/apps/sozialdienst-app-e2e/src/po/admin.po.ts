import { Locator, Page, expect } from '@playwright/test';

import { SozialdienstBenutzer } from '@dv/shared/model/gesuch';

export class AdminPO {
  public elems: {
    page: Page;
    adminLink: Locator;
    sozialdienstMitarbeiter: {
      link: Locator;
      filter: Locator;
      createButton: Locator;
      saveButton: Locator;
      rows: Locator;
      detail: {
        form: Locator;
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
      sozialdienstMitarbeiter: {
        link: page
          .getByTestId('option-nav-sozialdienst-benutzer')
          .locator('visible=true'),
        filter: page.getByTestId('sozialdienst-mitarbeiter-filter-name'),
        createButton: page.getByTestId('sozialdienst-mitarbeiter-create'),
        saveButton: page.getByTestId('button-save'),
        rows: page.getByRole('row'),
        detail: {
          form: page.getByTestId('form-sozialdienst-mitarbeiter-form'),
          nachname: page.getByTestId('form-sozialdienst-mitarbeiter-nachname'),
          vorname: page.getByTestId('form-sozialdienst-mitarbeiter-vorname'),
          email: page.getByTestId('form-sozialdienst-mitarbeiter-email'),
        },
      },
    };
  }

  goToAdmin = async () => {
    await this.elems.adminLink.click();
  };

  sozialdienstMitarbeiter = {
    goToOverview: async () => {
      await this.elems.sozialdienstMitarbeiter.link.click();
    },
    goToCreate: async () => {
      await this.elems.sozialdienstMitarbeiter.link.click();
      await this.elems.sozialdienstMitarbeiter.createButton.click();
    },
    goToEdit: async (name: string) => {
      await this.elems.sozialdienstMitarbeiter.filter.fill(name);
      await expect(this.elems.sozialdienstMitarbeiter.rows).toHaveCount(2);
      await this.elems.sozialdienstMitarbeiter.rows
        .getByTestId('sozialdienst-mitarbeiter-edit')
        .click();
    },
    filter: async (name: string) => {
      await this.elems.sozialdienstMitarbeiter.filter.fill(name);
    },
    fillOutNewSozialdienstMitarbeiterForm: async (
      sozialdienstMitarbeiter?: Partial<SozialdienstBenutzer>,
    ) => {
      const randId = Math.round(Math.random() * 100000);
      const email = `test-sozialdienst-mitarbeiter-${randId}@mailbucket.dvbern.ch`;
      await this.elems.sozialdienstMitarbeiter.detail.nachname.fill(
        sozialdienstMitarbeiter?.nachname ?? 'Muster',
      );
      await this.elems.sozialdienstMitarbeiter.detail.vorname.fill(
        sozialdienstMitarbeiter?.vorname ?? `e2e-${randId}`,
      );
      await this.elems.sozialdienstMitarbeiter.detail.email.fill(email);
      return email;
    },
    save: async () => {
      const createSuccessPromise = this.page.waitForResponse(
        '**/api/v1/sozialdienst/benutzer',
      );
      await this.elems.sozialdienstMitarbeiter.saveButton.click();
      await createSuccessPromise;
    },
    deleteSozialdienstMitarbeiter: async (name: string) => {
      await this.elems.sozialdienstMitarbeiter.filter.fill(name);
      await expect(this.elems.sozialdienstMitarbeiter.rows).toHaveCount(2);
      await this.elems.sozialdienstMitarbeiter.rows
        .getByTestId('sozialdienst-mitarbeiter-delete')
        .click();
    },
  };
}
