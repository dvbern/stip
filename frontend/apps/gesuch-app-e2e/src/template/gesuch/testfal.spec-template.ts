import { Response, expect } from '@playwright/test';
import { addYears, format } from 'date-fns';

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
  Steuerdaten,
} from '@dv/shared/model/gesuch';
import {
  SmallImageFile,
  expectStepTitleToContainText,
  generateSVN,
  getE2eUrls,
} from '@dv/shared/util-fn/e2e-util';

import { initializeTest } from '../../initialize-test';
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
import { SteuerdatenPO } from '../../po/steuerdaten.po';

const thisYear = format(new Date(), 'yyyy');
const specificMonth = (month: number) =>
  `${month}.${format(new Date(), 'yyyy')}`;
const specificMonthPlusYears = (month: number, years: number) =>
  `${month}.${format(addYears(new Date(), years), 'yyyy')}`;
const specificYearsAgo = (years: number) =>
  format(addYears(new Date(), -years), 'yyyy');

const adressen = {
  person: {
    land: 'CH',
    coAdresse: '',
    strasse: 'Kramgasse',
    hausnummer: '1',
    plz: '3011',
    ort: 'Bern',
  },
  mutter: {
    land: 'CH',
    coAdresse: '',
    strasse: 'Aarbergergasse',
    hausnummer: '1',
    plz: '3065',
    ort: 'Bolligen',
  },
} as const satisfies Record<string, Adresse>;

const person = (seed: string): PersonInAusbildung => ({
  sozialversicherungsnummer: generateSVN(seed + '_person'),
  anrede: 'HERR',
  nachname: 'Muster',
  vorname: 'Fritz',
  adresse: adressen.person,
  identischerZivilrechtlicherWohnsitz: true,
  email: 'max.muster@dvbern.ch',
  telefonnummer: '0316338555',
  geburtsdatum: `01.01.${specificYearsAgo(20)}`,
  nationalitaet: 'CH',
  heimatort: 'Bern',
  zivilstand: 'LEDIG',
  wohnsitz: 'FAMILIE',
  sozialhilfebeitraege: false,
  korrespondenzSprache: 'DEUTSCH',
});

const ausbildung: AusbildungValues = {
  ausbildungsort: 'Bern',
  ausbildungsstaette: 'Universität Bern',
  ausbildungsgang: 'Master',
  fachrichtung: 'Kunstgeschichte',
  ausbildungBegin: specificMonth(9),
  ausbildungEnd: specificMonthPlusYears(8, 3),
  pensum: 'VOLLZEIT',
};

const taetigkeit: LebenslaufItem = {
  taetigkeitsart: 'ERWERBSTAETIGKEIT',
  taetigkeitsBeschreibung: 'Serviceangestellter',
  von: `01.${specificYearsAgo(4)}`,
  bis: `08.${thisYear}`,
  wohnsitz: 'BE',
  id: '',
};

const familienlsituation: Familiensituation = {
  elternVerheiratetZusammen: false,
  gerichtlicheAlimentenregelung: false,
  elternteilUnbekanntVerstorben: true,
  mutterUnbekanntVerstorben: 'WEDER_NOCH',
  mutterWiederverheiratet: false,
  vaterUnbekanntVerstorben: 'VERSTORBEN',
};

const mutter = (seed: string): Eltern => ({
  sozialversicherungsnummer: generateSVN(seed + '_mutter'),
  nachname: 'Tester',
  vorname: 'Mutter1',
  adresse: adressen.mutter,
  identischerZivilrechtlicherWohnsitz: true,
  telefonnummer: '0316338355',
  geburtsdatum: `01.01.${specificYearsAgo(44)}`,
  ausweisbFluechtling: false,
  elternTyp: 'MUTTER',
  id: '',
});

const steuerdaten: Steuerdaten = {
  steuerdatenTyp: 'MUTTER',
  totalEinkuenfte: 8620,
  eigenmietwert: 0,
  isArbeitsverhaeltnisSelbstaendig: false,
  kinderalimente: 0,
  vermoegen: 0,
  ergaenzungsleistungen: 0,
  ergaenzungsleistungenPartner: 0,
  sozialhilfebeitraege: 0,
  sozialhilfebeitraegePartner: 0,
  wohnkosten: 16260,
  steuernKantonGemeinde: 0,
  steuernBund: 0,
  fahrkosten: 0,
  fahrkostenPartner: 0,
  verpflegung: 0,
  verpflegungPartner: 0,
  steuerjahr: +specificYearsAgo(2),
  veranlagungsCode: 91,
};

const bruder: Geschwister = {
  nachname: 'Tester',
  vorname: 'Geschwister1',
  geburtsdatum: `01.01.${specificYearsAgo(19)}`,
  wohnsitz: 'MUTTER_VATER',
  ausbildungssituation: 'IN_AUSBILDUNG',
  wohnsitzAnteilMutter: 100,
  id: '',
};

const partner = (seed: string): Partner => ({
  adresse: adressen.person,
  vorname: 'Susanne',
  geburtsdatum: '16.12.1990',
  sozialversicherungsnummer: generateSVN(seed + '_partner'),
  nachname: 'Schmitt',
});

const kind: Kind = {
  nachname: 'Muster',
  vorname: 'Sara',
  geburtsdatum: '25.12.2018',
  wohnsitz: 'FAMILIE',
  ausbildungssituation: 'VORSCHULPFLICHTIG',
  erhalteneAlimentebeitraege: 0,
  id: '',
};

const auszahlung: Auszahlung = {
  vorname: '',
  adresse: adressen.person,
  iban: 'CH9300762011623852957',
  nachname: '',
  kontoinhaber: 'GESUCHSTELLER',
};

const einnahmenKosten: EinnahmenKosten = {
  nettoerwerbseinkommen: 10000,
  zulagen: 0,
  renten: 1200,
  eoLeistungen: 0,
  ergaenzungsleistungen: 0,
  beitraege: 3000,
  ausbildungskostenTertiaerstufe: 1980,
  fahrkosten: 798,
  auswaertigeMittagessenProWoche: 5,
  vermoegen: 6,
  steuerjahr: +specificYearsAgo(1),
  veranlagungsCode: 0,
  steuernKantonGemeinde: 0,
  verdienstRealisiert: false,
  willDarlehen: true,
};

const { test, getGesuchId } = initializeTest('GESUCHSTELLER');

test.describe('Neues gesuch erstellen', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  test('Gesuch minimal', async ({ page, cockpit: _ }, testInfo) => {
    const seed = `${testInfo.title}-${testInfo.workerIndex}`;
    test.slow();
    // Step 1: Person ============================================================
    await expect(page.getByTestId('step-title')).toBeAttached();
    await page.getByTestId('step-nav-person').first().click();
    await expectStepTitleToContainText('Person in Ausbildung', page);
    const personPO = new PersonPO(page);
    await expect(personPO.elems.loading).toBeHidden();

    await personPO.fillPersonForm(person(seed));

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

    await lebenslaufPO.addTaetigkeit(taetigkeit);

    await lebenslaufPO.elems.buttonContinue.click();

    // Step 4: Familiensituation ===================================================
    await expectStepTitleToContainText('Familiensituation', page);
    const familiyPO = new FamilyPO(page);
    await expect(familiyPO.elems.loading).toBeHidden();

    await familiyPO.fillMinimalForm(familienlsituation);

    await familiyPO.elems.buttonSaveContinue.click();

    // Step 5.1: Eltern =============================================================
    await expectStepTitleToContainText('Eltern', page);
    const elternPO = new ElternPO(page);
    await expect(elternPO.elems.loading).toBeHidden();

    await elternPO.addMutter(mutter(seed));

    await elternPO.elems.buttonContinue.click();

    // Step 5.2: Steuerdaten Eltern =================================================
    await expectStepTitleToContainText('Steuerdaten', page);
    const steuerdatenPO = new SteuerdatenPO(page);
    await expect(steuerdatenPO.elems.loading).toBeHidden();

    await steuerdatenPO.fillSteuerdaten(steuerdaten);

    await steuerdatenPO.elems.buttonSaveContinue.click();

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

    await partnerPO.fillPartnerForm(partner(seed));

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

    const uploads = await page
      .locator('[data-testid^="button-document-upload"]')
      .all();
    const uploadCalls: Promise<Response>[] = [];
    for (const upload of uploads) {
      uploadCalls.push(
        page.waitForResponse(
          (response) =>
            response.url().includes('/api/v1/dokument') &&
            response.request().method() === 'POST',
        ),
      );
      await upload.click();
      await page.getByTestId('file-input').setInputFiles(SmallImageFile);
      await page.keyboard.press('Escape');
      await expect(page.getByTestId('file-input')).toHaveCount(0);
    }
    await Promise.all(uploadCalls);

    await page.getByTestId('button-continue').click();

    // Step 12: Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', page);

    await page.getByTestId('button-abschluss').click();
    await page.getByTestId('dialog-confirm').click();

    // Go to Berechnung (SB-App)
    const urls = getE2eUrls();
    await page.goto(`${urls.sb}/verfuegung/${getGesuchId()}`);

    await expect(page.getByTestId('zusammenfassung-resultat')).toHaveClass(
      /accept/,
    );

    await page.goto(`${urls.sb}/verfuegung/${getGesuchId()}/berechnung/1`);

    await expect(
      page.getByTestId('berechnung-persoenlich-total'),
    ).toContainText("- 14'192");
    await expect(page.getByTestId('berechnung-familien-total')).toContainText(
      "- 54'856",
    );
  });
});
