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
    unterhaltsbeitraege: Locator;
    zulagen: Locator;
    renten: Locator;
    eoLeistungen: Locator;
    ergaenzungsleistungen: Locator;
    beitraege: Locator;
    ausbildungskosten: Locator;
    fahrkosten: Locator;
    wohnkosten: Locator;
    auswaertigeMittagessenProWoche: Locator;
    wgWohnend: Locator;
    betreuungskostenKinder: Locator;
    steuerjahr: Locator;
    vermoegen: Locator;
    veranlagungsStatus: Locator;

    einnahmenBGSA: Locator;
    andereEinnahmen: Locator;
    taggelderAHVIV: Locator;

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
      unterhaltsbeitraege: page.getByTestId(
        'form-einnahmen-kosten-unterhaltsbeitraege',
      ),
      zulagen: page.getByTestId('form-einnahmen-kosten-zulagen'),
      renten: page.getByTestId('form-einnahmen-kosten-renten'),
      eoLeistungen: page.getByTestId('form-einnahmen-kosten-eoLeistungen'),
      ergaenzungsleistungen: page.getByTestId(
        'form-einnahmen-kosten-ergaenzungsleistungen',
      ),
      beitraege: page.getByTestId('form-einnahmen-kosten-beitraege'),
      ausbildungskosten: page.getByTestId(
        'form-einnahmen-kosten-ausbildungskosten',
      ),
      fahrkosten: page.getByTestId('form-einnahmen-kosten-fahrkosten'),
      wohnkosten: page.getByTestId('form-einnahmen-kosten-wohnkosten'),
      auswaertigeMittagessenProWoche: page.getByTestId(
        'form-einnahmen-kosten-auswaertigeMittagessenProWoche',
      ),
      wgWohnend: page.getByTestId('form-einnahmen-kosten-wgWohnend'),

      betreuungskostenKinder: page.getByTestId(
        'form-einnahmen-kosten-betreuungskostenKinder',
      ),
      taggelderAHVIV: page.getByTestId('form-einnahmen-kosten-taggelderAHVIV'),
      einnahmenBGSA: page.getByTestId('form-einnahmen-kosten-einnahmenBGSA'),
      andereEinnahmen: page.getByTestId(
        'form-einnahmen-kosten-andereEinnahmen',
      ),

      steuerjahr: page.getByTestId('form-einnahmen-kosten-steuerjahr'),
      vermoegen: page.getByTestId('form-einnahmen-kosten-vermoegen'),
      veranlagungsStatus: page.getByTestId(
        'form-einnahmen-kosten-veranlagungsStatus',
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
    if (isDefined(einnahmenKosten.unterhaltsbeitraege)) {
      await this.elems.unterhaltsbeitraege.fill(
        `${einnahmenKosten.unterhaltsbeitraege}`,
      );
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
    await this.elems.ausbildungskosten.fill(
      `${einnahmenKosten.ausbildungskosten}`,
    );
    if (isDefined(einnahmenKosten.betreuungskostenKinder)) {
      await this.elems.betreuungskostenKinder.fill(
        `${einnahmenKosten.betreuungskostenKinder}`,
      );
    }
    if (isDefined(einnahmenKosten.einnahmenBGSA)) {
      await this.elems.einnahmenBGSA.fill(`${einnahmenKosten.einnahmenBGSA}`);
    }
    if (isDefined(einnahmenKosten.andereEinnahmen)) {
      await this.elems.andereEinnahmen.fill(
        `${einnahmenKosten.andereEinnahmen}`,
      );
    }
    if (isDefined(einnahmenKosten.taggelderAHVIV)) {
      await this.elems.taggelderAHVIV.fill(`${einnahmenKosten.taggelderAHVIV}`);
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

    await expectFormToBeValid(this.elems.form);
  }
}
