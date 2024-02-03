import { APIRequestContext, test as base, expect } from '@playwright/test';
import { addMonths, format } from 'date-fns';

import {
  Adresse,
  LebenslaufItem,
  PersonInAusbildung,
} from '@dv/shared/model/gesuch';

import {
  expectFormToBeValid,
  expectStepTitleToContainText,
} from './helpers/helpers';
import { BEARER_COOKIE } from './helpers/types';
import { AusbildungPO, AusbildungValues } from './po/ausbildung.po';
import { CockpitPO } from './po/cockpit.po';
import { LebenslaufPO } from './po/lebenslauf.po';
import { PersonPO } from './po/person.po';
import { STIP_STORAGE_STATE } from '../playwright.config';

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
  heimatort: 'Bern',
  zivilstand: 'LEDIG',
  wohnsitz: 'FAMILIE',
  quellenbesteuert: false,
  sozialhilfebeitraege: false,
  korrespondenzSprache: 'DEUTSCH',
};

const nextMonth = format(addMonths(new Date(), 1), 'MM.yyyy');
const inTwoYears = format(addMonths(new Date(), 24), 'MM.yyyy');

const ausbildung: AusbildungValues = {
  ausbildungsland: 'CH',
  ausbildungsstaette: 'Universtität Bern',
  ausbildungsgang: 'Bachelor',
  fachrichtung: 'Informatik',
  ausbildungBegin: nextMonth,
  ausbildungEnd: inTwoYears,
  pensum: 'VOLLZEIT',
};

const ausbildung1: LebenslaufItem = {
  bildungsart: 'VORLEHRE',
  von: '08.2006',
  bis: '12.2019',
  wohnsitz: 'BE',
  ausbildungAbgeschlossen: true,
  id: '',
};

const ausbildung2: LebenslaufItem = {
  bildungsart: 'FACHMATURITAET',
  von: '08.2020',
  bis: '01.2024',
  wohnsitz: 'BE',
  ausbildungAbgeschlossen: false,
  id: '',
};

const taetigkeit: LebenslaufItem = {
  taetigskeitsart: 'ERWERBSTAETIGKEIT',
  taetigkeitsBeschreibung: 'Serviceangestellter',
  von: '07.2019',
  bis: '07.2020',
  wohnsitz: 'BE',
  id: '',
};

// https://playwright.dev/docs/api-testing#sending-api-requests-from-ui-tests

let apiContext: APIRequestContext;
let gesuchsId: string;

const test = base.extend<{ cockpit: CockpitPO }>({
  cockpit: async ({ page }, use) => {
    const cockpit = new CockpitPO(page);

    await cockpit.goToDashBoard();

    const requestPromise = page.waitForResponse('**/api/v1/gesuch/*');

    await cockpit.getGesuchNew().click();
    await page.waitForURL('**/gesuch/person/*');

    const response = await requestPromise;
    const body = await response.json();
    gesuchsId = body.id;

    await use(cockpit);
  },
});

test.beforeAll(async ({ playwright, baseURL, browser }) => {
  const context = await browser.newContext({
    storageState: STIP_STORAGE_STATE,
  });

  const cookies = await context.cookies();
  const bearer = cookies.find((c) => c.name === BEARER_COOKIE);

  apiContext = await playwright.request.newContext({
    baseURL,
    extraHTTPHeaders: {
      Authorization: `Bearer ${bearer?.value}`,
    },
  });
});

test.afterAll(async () => {
  await apiContext.delete(`/api/v1/gesuch/${gesuchsId}`);

  await apiContext.dispose();
});

test.describe('Neues gesuch erstellen', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  test('Neues gesuch erstellen', async ({ page, cockpit }) => {
    // Step 1: Person ============================================================
    await expectStepTitleToContainText('Person in Ausbildung', page);

    const personPO = new PersonPO(page);

    await expect(personPO.elems.loading()).toBeHidden();

    await personPO.fillPersonForm(person);

    expectFormToBeValid(personPO.elems.form);

    await personPO.elems.buttonSaveContinue.click();

    // Step 2: Ausbildung ========================================================
    await expectStepTitleToContainText('Ausbildung', page);

    const ausbildungPO = new AusbildungPO(page);

    await expect(ausbildungPO.elems.loading()).toBeHidden();

    await ausbildungPO.fillEducationForm(ausbildung);

    await expectFormToBeValid(ausbildungPO.elems.form);

    await ausbildungPO.elems.buttonSaveContinue.click();

    // Step 3: Lebenslauf ========================================================
    await expectStepTitleToContainText('Lebenslauf', page);

    const lebenslaufPO = new LebenslaufPO(page);

    await lebenslaufPO.addAusbildung(ausbildung1);

    await lebenslaufPO.addAusbildung(ausbildung2);

    await lebenslaufPO.addTaetigkeit(taetigkeit);

    await lebenslaufPO.elems.buttonContinue.click();

    // Step 4: Familiensituation ===================================================
  });
});
