import { Locator, Page } from '@playwright/test';

import { EinnahmenKosten } from '@dv/shared/model/gesuch';

import { expectFormToBeValid, selectMatRadio } from '../helpers/helpers';

export class EinnahmenKostenPO {
  public elems: {
    page: Page;
    loading: () => Locator;
    form: Locator;
    nettoerwerbseinkommen: Locator;
    alimente: Locator;
    zulagen: Locator;
    renten: Locator;
    eoLeistungen: Locator;
    ergaenzungsleistungen: Locator;
    beitraege: Locator;
    ausbildungskostenSekundarstufeZwei: Locator;
    ausbildungskostenTertiaerstufe: Locator;
    fahrkosten: Locator;
    wohnkosten: Locator;
    auswaertigeMittagessenProWoche: Locator;
    personenImHaushalt: Locator;
    verdienstRealisiert: Locator;
    willDarlehen: Locator;

    incompleteWarning: () => Locator;

    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: () => page.getByTestId('form-einnahmen-kosten-loading'),

      incompleteWarning: () =>
        page.getByTestId('gesuch-form-einnahmenkosten-data-incomplete-warning'),

      form: page.getByTestId('form-einnahmen-kosten-form'),

      nettoerwerbseinkommen: page.getByTestId(
        'form-einnahmen-kosten-nettoerwerbseinkommen',
      ),
      alimente: page.getByTestId('form-einnahmen-kosten-alimente'),
      zulagen: page.getByTestId('form-einnahmen-kosten-zulagen'),
      renten: page.getByTestId('form-einnahmen-kosten-renten'),
      eoLeistungen: page.getByTestId('form-einnahmen-kosten-eoLeistungen'),
      ergaenzungsleistungen: page.getByTestId(
        'form-einnahmen-kosten-ergaenzungsleistungen',
      ),
      beitraege: page.getByTestId('form-einnahmen-kosten-beitraege'),
      ausbildungskostenSekundarstufeZwei: page.getByTestId(
        'form-einnahmen-kosten-ausbildungskostenSekundarstufeZwei',
      ),
      ausbildungskostenTertiaerstufe: page.getByTestId(
        'form-einnahmen-kosten-ausbildungskostenTertiaerstufe',
      ),
      fahrkosten: page.getByTestId('form-einnahmen-kosten-fahrkosten'),
      wohnkosten: page.getByTestId('form-einnahmen-kosten-wohnkosten'),
      auswaertigeMittagessenProWoche: page.getByTestId(
        'form-einnahmen-kosten-auswaertigeMittagessenProWoche',
      ),
      personenImHaushalt: page.getByTestId(
        'form-einnahmen-kosten-personenImHaushalt',
      ),
      verdienstRealisiert: page.getByTestId(
        'form-einnahmen-kosten-verdienstRealisiert',
      ),
      willDarlehen: page.getByTestId('form-einnahmen-kosten-willDarlehen'),

      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  public async fillEinnahmenKostenForm(einnahmenKosten: EinnahmenKosten) {
    await this.elems.nettoerwerbseinkommen.fill(
      `${einnahmenKosten.nettoerwerbseinkommen ?? 0}`,
    );

    await this.elems.zulagen.fill(`${einnahmenKosten.zulagen ?? 0}`);

    await this.elems.ausbildungskostenSekundarstufeZwei.fill(
      `${einnahmenKosten.ausbildungskostenSekundarstufeZwei ?? 0}`,
    );

    await this.elems.fahrkosten.fill(`${einnahmenKosten.fahrkosten ?? 0}`);

    await this.elems.wohnkosten.fill(`${einnahmenKosten.wohnkosten ?? 0}`);

    await this.elems.auswaertigeMittagessenProWoche.fill(
      `${einnahmenKosten.auswaertigeMittagessenProWoche ?? 0}`,
    );

    await this.elems.personenImHaushalt.fill(
      `${einnahmenKosten.personenImHaushalt ?? 0}`,
    );

    await selectMatRadio(
      this.elems.verdienstRealisiert,
      einnahmenKosten.verdienstRealisiert,
    );

    await selectMatRadio(
      this.elems.willDarlehen,
      einnahmenKosten.willDarlehen ?? false,
    );

    await expectFormToBeValid(this.elems.form);
  }
}
