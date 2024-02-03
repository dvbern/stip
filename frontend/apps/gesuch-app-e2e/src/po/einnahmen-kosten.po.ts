import { Locator, Page } from '@playwright/test';

import { EinnahmenKosten } from '@dv/shared/model/gesuch';

export class EinnahmenKostenPO {
  public elems: {
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
  };

  constructor(page: Page) {
    this.elems = {
      loading: () => page.getByTestId('form-einnahmen-kosten-loading'),

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
    };
  }

  public async fillEinnahmenKostenForm(einnahmenKosten: EinnahmenKosten) {
    await this.elems.nettoerwerbseinkommen.fill(
      einnahmenKosten.nettoerwerbseinkommen.toString(),
    );
    await this.elems.alimente.fill(`${einnahmenKosten.alimente ?? 0}`);
    await this.elems.zulagen.fill(`${einnahmenKosten.zulagen ?? 0}`);
    await this.elems.renten.fill(`${einnahmenKosten.renten ?? 0}`);
    await this.elems.eoLeistungen.fill(`${einnahmenKosten.eoLeistungen ?? 0}`);
    await this.elems.ergaenzungsleistungen.fill(
      `${einnahmenKosten.ergaenzungsleistungen ?? 0}`,
    );
    await this.elems.beitraege.fill(`${einnahmenKosten.beitraege ?? 0}`);
    await this.elems.ausbildungskostenSekundarstufeZwei.fill(
      `${einnahmenKosten.ausbildungskostenSekundarstufeZwei ?? 0}`,
    );
    await this.elems.ausbildungskostenTertiaerstufe.fill(
      `${einnahmenKosten.ausbildungskostenTertiaerstufe ?? 0}`,
    );
    await this.elems.fahrkosten.fill(`${einnahmenKosten.fahrkosten ?? 0}`);
    await this.elems.wohnkosten.fill(`${einnahmenKosten.wohnkosten ?? 0}`);
    await this.elems.auswaertigeMittagessenProWoche.fill(
      `${einnahmenKosten.auswaertigeMittagessenProWoche ?? 0}`,
    );
    await this.elems.personenImHaushalt.fill(
      `${einnahmenKosten.personenImHaushalt ?? 0}`,
    );

    await this.elems.verdienstRealisiert
      .getByTestId(einnahmenKosten.verdienstRealisiert ? 'yes' : 'no')
      .getByRole('radio')
      .click();

    await this.elems.willDarlehen
      .getByTestId(einnahmenKosten.willDarlehen ? 'yes' : 'no')
      .getByRole('radio')
      .click();
  }
}
