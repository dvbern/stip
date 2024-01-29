import { expect, test as base } from '@playwright/test';
import { CockpitPO } from './po/cockpit.po';
import { PersonPO } from './po/person.po';
import { Adresse, PersonInAusbildung } from '@dv/shared/model/gesuch';

const adresse: Adresse = {
  land: 'CH',
  coAdresse: '',
  strasse: 'Aarbergergasse',
  hausnummer: '5a',
  plz: '3000',
  ort: 'Bern',
};

const person: PersonInAusbildung = {
  sozialversicherungsnummer: '756.1111.1111.13',
  anrede: 'HERR',
  nachname: 'Muster',
  vorname: 'Max',
  adresse,
  identischerZivilrechtlicherWohnsitz: true,
  email: 'max.muster@dvbern.ch',
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1990',
  nationalitaet: 'CH',
  // niederlassungsstatus: Niederlassungsstatus.C,
  heimatort: 'Bern',
  zivilstand: 'LEDIG',
  wohnsitz: 'FAMILIE',
  quellenbesteuert: false,
  sozialhilfebeitraege: false,
  digitaleKommunikation: true,
  korrespondenzSprache: 'DEUTSCH',
};

const test = base.extend<{ cockpit: CockpitPO }>({
  cockpit: async ({ page }, use) => {
    const cockpit = new CockpitPO(page);

    await cockpit.goToDashBoard();

    await use(cockpit);
  },
});

test('Neues gesuch erstellen', async ({ page, cockpit }) => {
  await cockpit.getGesuchNew().click();

  await expect(page.getByTestId('step-title')).toContainText(
    'Person in Ausbildung',
  );

  const personPage = new PersonPO(page);

  expect(personPage.elements.loading).not.toBe({});

  await personPage.fillPersonForm(person);

  await expect(personPage.elements.form).toHaveClass('ng-valid');
});
