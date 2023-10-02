export namespace SharedEinnahmenKostenInAusbildungPO {
  export const getFormLoading = () =>
    cy.getBySel('form-einnahmen-kosten-loading');
  export const getFormNettoerwerbseinkommen = () =>
    cy.getBySel('form-einnahmen-kosten-nettoerwerbseinkommen');

  export const getFormWohnkosten = () =>
    cy.getBySel('form-einnahmen-kosten-wohnkosten');
  export const getFormPersonenImHaushalt = () =>
    cy.getBySel('form-einnahmen-kosten-personenImHaushalt');
  export const getFormAuswaertigeMittagessenProWoche = () =>
    cy.getBySel('form-einnahmen-kosten-auswaertigeMittagessenProWoche');
}
