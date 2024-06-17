import { expect } from '@playwright/test';
import { addMonths, format } from 'date-fns';

import {
  Adresse,
  Auszahlung,
  EinnahmenKosten,
  Eltern,
  Familiensituation,
  Geschwister,
  Kind,
  LebenslaufItem,
  Partner,
  PersonInAusbildung,
} from '@dv/shared/model/gesuch';
import { expectStepTitleToContainText } from '@dv/shared/util-fn/e2e-util';

import { AusbildungPO, AusbildungValues } from '../../po/ausbildung.po';
import { AuszahlungPO } from '../../po/auszahlung.po';
import { EinnahmenKostenPO } from '../../po/einnahmen-kosten.po';
import { ElternPO } from '../../po/eltern.po';
import { FamilyPO } from '../../po/familiy.po';
import { GeschwisterPO } from '../../po/geschwister.po';
import { KinderPO } from '../../po/kinder.po';
import { LebenslaufPO } from '../../po/lebenslauf.po';
import { PartnerPO } from '../../po/partner.po';
import { PersonPO } from '../../po/person.po';
import { initializeTest } from '../../utils';

const adresse: Adresse = {
  land: 'CH',
  coAdresse: '',
  strasse: 'Aarbergergasse',
  hausnummer: '5a',
  plz: '3065',
  ort: 'Bolligen',
};

const person: PersonInAusbildung = {
  sozialversicherungsnummer: '756.7357.7357.00',
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
  zivilstand: 'VERHEIRATET',
  wohnsitz: 'EIGENER_HAUSHALT',
  sozialhilfebeitraege: false,
  korrespondenzSprache: 'DEUTSCH',
};

const thisMonth = format(new Date(), 'MM.yyyy');
const nextMonth = format(addMonths(new Date(), 1), 'MM.yyyy');
const inTwoYears = format(addMonths(new Date(), 24), 'MM.yyyy');

const ausbildung: AusbildungValues = {
  ausbildungsort: 'Bern',
  ausbildungsstaette: 'Universität Bern',
  ausbildungsgang: 'Bsc. Biologie',
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
  bis: thisMonth,
  wohnsitz: 'BE',
  ausbildungAbgeschlossen: false,
  id: '',
};

const taetigkeit: LebenslaufItem = {
  taetigkeitsart: 'ERWERBSTAETIGKEIT',
  taetigkeitsBeschreibung: 'Serviceangestellter',
  von: '07.2019',
  bis: '07.2020',
  wohnsitz: 'BE',
  id: '',
};

const auszahlung: Auszahlung = {
  vorname: '',
  adresse,
  iban: 'CH9300762011623852957',
  nachname: '',
  kontoinhaber: 'GESUCHSTELLER',
};

const einnahmenKosten: EinnahmenKosten = {
  nettoerwerbseinkommen: 15000,
  fahrkosten: 100,
  verdienstRealisiert: false,
  wohnkosten: 1000,
  ausbildungskostenSekundarstufeZwei: 1000,
  ausbildungskostenTertiaerstufe: 1300,
  zulagen: 250,
  wgWohnend: false,
  auswaertigeMittagessenProWoche: 5,
  betreuungskostenKinder: 100,
  veranlagungsCode: 20,
  steuerjahr: 2020,
  vermoegen: 0,
};

const partner: Partner = {
  adresse,
  vorname: 'Susanne',
  geburtsdatum: '16.12.1990',
  sozialversicherungsnummer: '756.2222.2222.55',
  nachname: 'Schmitt',
};

const vater: Eltern = {
  sozialversicherungsnummer: '756.2222.2222.24',
  nachname: 'Muster',
  vorname: 'Maximilian',
  adresse,
  identischerZivilrechtlicherWohnsitz: true,
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1969',
  ausweisbFluechtling: false,
  ergaenzungsleistungAusbezahlt: false,
  sozialhilfebeitraegeAusbezahlt: false,
  id: '',
  wohnkosten: 100,
  elternTyp: 'VATER',
};

const mutter: Eltern = {
  sozialversicherungsnummer: '756.3333.3333.35',
  nachname: 'Muster',
  vorname: 'Maxine',
  adresse,
  identischerZivilrechtlicherWohnsitz: true,
  telefonnummer: '0041791111111',
  geburtsdatum: '25.12.1968',
  ausweisbFluechtling: false,
  ergaenzungsleistungAusbezahlt: false,
  sozialhilfebeitraegeAusbezahlt: false,
  id: '',
  wohnkosten: 100,
  elternTyp: 'VATER',
};

const bruder: Geschwister = {
  nachname: 'Muster',
  vorname: 'Simon',
  geburtsdatum: '25.12.2005',
  wohnsitz: 'FAMILIE',
  ausbildungssituation: 'VORSCHULPFLICHTIG',
  id: '',
};

const kind: Kind = {
  nachname: 'Muster',
  vorname: 'Sara',
  geburtsdatum: '25.12.2018',
  wohnsitz: 'FAMILIE',
  ausbildungssituation: 'VORSCHULPFLICHTIG',
  erhalteneAlimentebeitraege: 0,
  id: '',
};

const familienlsituation: Familiensituation = {
  elternVerheiratetZusammen: true,
};

const { test } = initializeTest();

test.describe('Neues gesuch erstellen', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  test('Gesuch minimal', async ({ page, cockpit }) => {
    test.slow();
    // Step 1: Person ============================================================
    await expectStepTitleToContainText('Person in Ausbildung', page);
    const personPO = new PersonPO(page);
    await expect(personPO.elems.loading).toBeHidden();

    await personPO.fillPlzNotKtBern();

    await expect(personPO.elems.vermoegenVorjahr).toBeVisible();

    await personPO.fillPersonForm(person);

    await expect(personPO.elems.vermoegenVorjahr).toBeHidden();

    await personPO.elems.buttonSaveContinue.click();

    // Step 2: Ausbildung ========================================================
    await expectStepTitleToContainText('Ausbildung', page);
    const ausbildungPO = new AusbildungPO(page);
    await expect(ausbildungPO.elems.loading).toBeHidden();

    await ausbildungPO.fillEducationForm(ausbildung);

    await ausbildungPO.elems.buttonSaveContinue.click();

    // Step 3: Lebenslauf ========================================================
    await expectStepTitleToContainText('Lebenslauf', page);
    const lebenslaufPO = new LebenslaufPO(page);
    await expect(lebenslaufPO.elems.loading).toBeHidden();

    await lebenslaufPO.addAusbildung(ausbildung1);
    await lebenslaufPO.addAusbildung(ausbildung2);
    await lebenslaufPO.addTaetigkeit(taetigkeit);

    await lebenslaufPO.elems.buttonContinue.click();

    // Step 4: Familiensituation ===================================================
    await expectStepTitleToContainText('Familiensituation', page);
    const familiyPO = new FamilyPO(page);
    await expect(familiyPO.elems.loading).toBeHidden();

    await familiyPO.fillMinimalForm(familienlsituation);

    await familiyPO.elems.buttonSaveContinue.click();

    // Step 5: Eltern =============================================================
    await expectStepTitleToContainText('Eltern', page);
    const elternPO = new ElternPO(page);
    await expect(elternPO.elems.loading).toBeHidden();

    await elternPO.addVater(vater);
    await elternPO.addMutter(mutter);

    await elternPO.elems.buttonContinue.click();

    // Step 6: Geschwister  ========================================================
    await expectStepTitleToContainText('Geschwister', page);
    const geschwisterPO = new GeschwisterPO(page);
    await expect(geschwisterPO.elems.loading).toBeHidden();

    await geschwisterPO.addGeschwister(bruder);

    await geschwisterPO.elems.buttonContinue.click();

    // Step 7: Partner =============================================================
    await expectStepTitleToContainText('Ehe- / Konkubinatspartner', page);
    const partnerPO = new PartnerPO(page);
    await expect(partnerPO.elems.loading).toBeHidden();

    await partnerPO.fillPartnerForm(partner);

    await partnerPO.elems.buttonSaveContinue.click();

    // Step 8: Kinder =============================================================
    await expectStepTitleToContainText('Kinder', page);
    const kinderPO = new KinderPO(page);
    await expect(kinderPO.elems.loading).toBeHidden();

    await kinderPO.addKind(kind);

    await kinderPO.elems.buttonContinue.click();

    // Step 9: Auszahlung ===========================================================
    await expectStepTitleToContainText('Auszahlung', page);
    const auszahlungPO = new AuszahlungPO(page);
    await expect(auszahlungPO.elems.loading).toBeHidden();

    await auszahlungPO.fillAuszahlungEigenesKonto(auszahlung);

    // hotfix for flaky test of Einnahmen & Kosten form
    // fix by changing the initialization of the form in a separate task
    const ausbildungPromise = page.waitForResponse(
      '**/api/v1/ausbildungsstaette',
    );

    await auszahlungPO.elems.buttonSaveContinue.click();

    // Step 10: Einnahmen und Kosten =================================================
    await expectStepTitleToContainText('Einnahmen & Kosten', page);
    const einnahmenKostenPO = new EinnahmenKostenPO(page);
    await expect(einnahmenKostenPO.elems.loading).toBeHidden();

    await ausbildungPromise;

    await einnahmenKostenPO.fillEinnahmenKostenForm(einnahmenKosten);

    await einnahmenKostenPO.elems.buttonSaveContinue.click();

    // Step 11: Dokumente ===========================================================

    await expectStepTitleToContainText('Dokumente', page);

    await page.getByTestId('button-continue').click();

    // Step 12: Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', page);
  });
});
