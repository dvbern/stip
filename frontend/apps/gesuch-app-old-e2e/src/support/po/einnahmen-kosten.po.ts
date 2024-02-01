import { EinnahmenKosten } from '@dv/shared/model/gesuch';
import { getRadioOption } from '@dv/shared/util-fn/e2e-helpers';

const elements = {
  loading: () => cy.getBySel('form-einnahmen-kosten-loading'),

  form: () => cy.getBySel('form-einnahmen-kosten-form'),

  nettoerwerbseinkommen: () =>
    cy.getBySel('form-einnahmen-kosten-nettoerwerbseinkommen'),
  alimente: () => cy.getBySel('form-einnahmen-kosten-alimente'),
  zulagen: () => cy.getBySel('form-einnahmen-kosten-zulagen'),
  renten: () => cy.getBySel('form-einnahmen-kosten-renten'),
  eoLeistungen: () => cy.getBySel('form-einnahmen-kosten-eoLeistungen'),
  ergaenzungsleistungen: () =>
    cy.getBySel('form-einnahmen-kosten-ergaenzungsleistungen'),
  beitraege: () => cy.getBySel('form-einnahmen-kosten-beitraege'),
  ausbildungskostenSekundarstufeZwei: () =>
    cy.getBySel('form-einnahmen-kosten-ausbildungskostenSekundarstufeZwei'),
  ausbildungskostenTertiaerstufe: () =>
    cy.getBySel('form-einnahmen-kosten-ausbildungskostenTertiaerstufe'),
  fahrkosten: () => cy.getBySel('form-einnahmen-kosten-fahrkosten'),
  wohnkosten: () => cy.getBySel('form-einnahmen-kosten-wohnkosten'),
  auswaertigeMittagessenProWoche: () =>
    cy.getBySel('form-einnahmen-kosten-auswaertigeMittagessenProWoche'),
  personenImHaushalt: () =>
    cy.getBySel('form-einnahmen-kosten-personenImHaushalt'),
  verdienstRealisiert: () =>
    cy.getBySel('form-einnahmen-kosten-verdienstRealisiert'),
  willDarlehen: () => cy.getBySel('form-einnahmen-kosten-willDarlehen'),
};

const fillEinnahmenKostenForm = (einnahmenKosten: EinnahmenKosten) => {
  elements
    .nettoerwerbseinkommen()
    .type(einnahmenKosten.nettoerwerbseinkommen.toString());

  elements.zulagen().type(`${einnahmenKosten.zulagen ?? 0}`);

  elements
    .ausbildungskostenSekundarstufeZwei()
    .type(`${einnahmenKosten.ausbildungskostenSekundarstufeZwei}`);

  elements.fahrkosten().type(`${einnahmenKosten.fahrkosten}`);

  elements.wohnkosten().type(`${einnahmenKosten.wohnkosten}`);

  elements
    .auswaertigeMittagessenProWoche()
    .type(`${einnahmenKosten.auswaertigeMittagessenProWoche}`);

  elements
    .personenImHaushalt()
    .type(`${einnahmenKosten.personenImHaushalt ?? 0}`);

  elements.verdienstRealisiert().within(() => {
    getRadioOption(einnahmenKosten.verdienstRealisiert).click();
  });

  elements.willDarlehen().within(() => {
    getRadioOption(einnahmenKosten.willDarlehen ?? false).click();
  });
};

export const EinnahmenKostenPO = {
  elements,
  fillEinnahmenKostenForm,
};
