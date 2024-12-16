import { Locator, Page } from '@playwright/test';

import { EinnahmenKosten } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  expectFormToBeValid,
  selectMatRadio,
} from '@dv/shared/util-fn/e2e-util';

export class EinnahmenKostenPO {
  public elems: {
    page: Page;
    loading: Locator;
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
    wgWohnend: Locator;
    verdienstRealisiert: Locator;
    betreuungskostenKinder: Locator;
    steuerjahr: Locator;
    vermoegen: Locator;
    veranlagungsCode: Locator;

    incompleteWarning: () => Locator;

    buttonSaveContinue: Locator;
    buttonNext: Locator;
  };

  constructor(page: Page) {
    this.elems = {
      page,
      loading: page.getByTestId('form-einnahmen-kosten-loading'),

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
      wgWohnend: page.getByTestId('form-einnahmen-kosten-wgWohnend'),
      verdienstRealisiert: page.getByTestId(
        'form-einnahmen-kosten-verdienstRealisiert',
      ),
      betreuungskostenKinder: page.getByTestId(
        'form-einnahmen-kosten-betreuungskostenKinder',
      ),
      steuerjahr: page.getByTestId('form-einnahmen-kosten-steuerjahr'),
      vermoegen: page.getByTestId('form-einnahmen-kosten-vermoegen'),
      veranlagungsCode: page.getByTestId(
        'form-einnahmen-kosten-veranlagungsCode',
      ),

      buttonSaveContinue: page.getByTestId('button-save-continue'),
      buttonNext: page.getByTestId('button-next'),
    };
  }

  public async fillEinnahmenKostenForm(einnahmenKosten: EinnahmenKosten) {
    await this.elems.nettoerwerbseinkommen.fill(
      `${einnahmenKosten.nettoerwerbseinkommen ?? 0}`,
    );

    if (isDefined(einnahmenKosten.zulagen)) {
      await this.elems.zulagen.fill(`${einnahmenKosten.zulagen}`);
    }
    if (isDefined(einnahmenKosten.renten)) {
      await this.elems.renten.fill(`${einnahmenKosten.renten}`);
    }
    if (isDefined(einnahmenKosten.eoLeistungen)) {
      await this.elems.eoLeistungen.fill(`${einnahmenKosten.eoLeistungen}`);
    }
    if (isDefined(einnahmenKosten.ergaenzungsleistungen)) {
      await this.elems.ergaenzungsleistungen.fill(
        `${einnahmenKosten.ergaenzungsleistungen}`,
      );
    }
    if (isDefined(einnahmenKosten.beitraege)) {
      await this.elems.beitraege.fill(`${einnahmenKosten.beitraege}`);
    }

    await this.elems.ausbildungskostenTertiaerstufe.fill(
      `${einnahmenKosten.ausbildungskostenTertiaerstufe}`,
    );

    if (isDefined(einnahmenKosten.betreuungskostenKinder)) {
      await this.elems.betreuungskostenKinder.fill(
        `${einnahmenKosten.betreuungskostenKinder}`,
      );
    }

    await this.elems.fahrkosten.fill(`${einnahmenKosten.fahrkosten}`);

    if (isDefined(einnahmenKosten.wohnkosten)) {
      await this.elems.wohnkosten.fill(`${einnahmenKosten.wohnkosten}`);
    }

    if (isDefined(einnahmenKosten.auswaertigeMittagessenProWoche)) {
      await this.elems.auswaertigeMittagessenProWoche.fill(
        `${einnahmenKosten.auswaertigeMittagessenProWoche}`,
      );
    }

    if (isDefined(einnahmenKosten.vermoegen)) {
      await this.elems.vermoegen.fill(`${einnahmenKosten.vermoegen}`);
    }

    if (isDefined(einnahmenKosten.wgWohnend)) {
      await selectMatRadio(this.elems.wgWohnend, einnahmenKosten.wgWohnend);
    }

    await selectMatRadio(
      this.elems.verdienstRealisiert,
      einnahmenKosten.verdienstRealisiert,
    );

    await expectFormToBeValid(this.elems.form);
  }
}
