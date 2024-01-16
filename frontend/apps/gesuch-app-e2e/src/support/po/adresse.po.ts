export interface Adresse {
  strasse: string;
  coAdresse?: string;
  hausnummer: string;
  plz: string;
  ort: string;
  land: string;
}

const fillAddressForm = (adresse: Adresse) => {
  cy.getBySel('form-address-strasse').type(adresse.strasse);
  cy.getBySel('form-address-hausnummer').type(adresse.hausnummer);
  cy.getBySel('form-address-plz').type(adresse.plz);
  cy.getBySel('form-address-ort').type(adresse.ort);
  cy.getBySel('form-address-land').click();
  cy.get('mat-option').contains(adresse.land).click();
};

export const AddressPO = {
  fillAddressForm,
};
