import { expect } from '@playwright/test';

import {
  Adresse,
  Auszahlung,
  Darlehen,
  EinnahmenKosten,
  Eltern,
  Familiensituation,
  Geschwister,
  LebenslaufItem,
  PersonInAusbildung,
  Steuerdaten,
} from '@dv/shared/model/gesuch';
import {
  SmallImageFile,
  expectStepTitleToContainText,
  generateSVN,
  getE2eUrls,
} from '@dv/shared/util-fn/e2e-util';

import { AusbildungValues } from '../../po/ausbildung.po';
import { AuszahlungPO } from '../../po/auszahlung.po';
import { DarlehenPO } from '../../po/darlehen.po';
import { EinnahmenKostenPO } from '../../po/einnahmen-kosten.po';
import { ElternPO } from '../../po/eltern.po';
import { FamilyPO } from '../../po/familiy.po';
import { GeschwisterPO } from '../../po/geschwister.po';
import { KinderPO } from '../../po/kinder.po';
import { LebenslaufPO } from '../../po/lebenslauf.po';
import { PersonPO } from '../../po/person.po';
import { SteuerdatenPO } from '../../po/steuerdaten.po';
import {
  initializeTest,
  specificMonth,
  specificMonthPlusYears,
  specificYearsAgo,
  thisYear,
} from '../../utils';

const ausbildung: AusbildungValues = {
  fallId: '',
  status: 'AKTIV',
  editable: true,
  ausbildungsort: 'Bern',
  ausbildungsstaetteText: 'Universität Bern',
  ausbildungsgangText: 'Master',
  fachrichtung: 'Kunstgeschichte',
  ausbildungBegin: specificMonth(9),
  ausbildungEnd: specificMonthPlusYears(8, 3),
  pensum: 'VOLLZEIT',
};

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
  wohnsitz: 'MUTTER_VATER',
  wohnsitzAnteilMutter: 100,
  sozialhilfebeitraege: false,
  korrespondenzSprache: 'DEUTSCH',
});

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
  ergaenzungsleistungen: 0,
  sozialhilfebeitraege: false,
  wohnkosten: 16260,
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
};

const darlehen: Darlehen = {
  willDarlehen: false,
};

const { test, getGesuchId, getTrancheId } = initializeTest(
  'GESUCHSTELLER',
  ausbildung,
);

test.describe('Neues gesuch erstellen', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  test('Gesuch Testfall-2', async ({ page, cockpit: _ }, testInfo) => {
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

    // Step 2: Lebenslauf ========================================================
    await expectStepTitleToContainText('Lebenslauf', page);
    const lebenslaufPO = new LebenslaufPO(page);
    await expect(lebenslaufPO.elems.loading).toBeHidden();

    await lebenslaufPO.addTaetigkeit(taetigkeit);

    await lebenslaufPO.elems.buttonContinue.click();

    // Step 3: Familiensituation ===================================================
    await expectStepTitleToContainText('Familiensituation', page);
    const familiyPO = new FamilyPO(page);
    await expect(familiyPO.elems.loading).toBeHidden();

    await familiyPO.fillUnbekanntOderVerstorben(familienlsituation);

    await familiyPO.elems.buttonSaveContinue.click();

    // Step 4.1: Eltern =============================================================
    await expectStepTitleToContainText('Eltern', page);
    const elternPO = new ElternPO(page);
    await expect(elternPO.elems.loading).toBeHidden();

    await elternPO.addMutter(mutter(seed));

    await elternPO.elems.buttonContinue.click();

    // Step 4.2: Steuerdaten Eltern =================================================
    await expectStepTitleToContainText('Steuerdaten', page);
    const steuerdatenPO = new SteuerdatenPO(page);
    await expect(steuerdatenPO.elems.loading).toBeHidden();

    await steuerdatenPO.fillSteuerdaten(steuerdaten);

    await steuerdatenPO.elems.buttonSaveContinue.click();

    // Step 5: Geschwister  ========================================================
    await expectStepTitleToContainText('Geschwister', page);
    const geschwisterPO = new GeschwisterPO(page);
    await expect(geschwisterPO.elems.loading).toBeHidden();

    await geschwisterPO.addGeschwister(bruder);

    await geschwisterPO.elems.buttonContinue.click();

    // Step 6: Kinder =============================================================
    await expectStepTitleToContainText('Kinder', page);
    const kinderPO = new KinderPO(page);
    await kinderPO.elems.buttonContinue.click();

    // Step 7: Auszahlung ===========================================================
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

    // Step 8: Einnahmen und Kosten =================================================
    await expectStepTitleToContainText('Einnahmen & Kosten', page);
    const einnahmenKostenPO = new EinnahmenKostenPO(page);
    await expect(einnahmenKostenPO.elems.loading).toBeHidden();

    await ausbildungPromise;

    await einnahmenKostenPO.fillEinnahmenKostenForm(einnahmenKosten);

    await einnahmenKostenPO.elems.buttonSaveContinue.click();

    // Step 9: Darlehen =============================================================
    await expectStepTitleToContainText('Darlehen', page);
    const darlehenPO = new DarlehenPO(page);
    await expect(darlehenPO.elems.loading).toBeHidden();

    await darlehenPO.fillDarlehenForm(darlehen);

    const requiredDokumenteResponse = page.waitForResponse(
      '**/api/v1/gesuchtranche/*/requiredDokumente',
    );
    await darlehenPO.elems.buttonSaveContinue.click();

    // Step 10: Dokumente ===========================================================

    await expectStepTitleToContainText('Dokumente', page);
    await requiredDokumenteResponse;

    const uploads = await page
      .locator('[data-testid^="button-document-upload"]')
      .all();
    for (const upload of uploads) {
      const uploadCall = page.waitForResponse(
        (response) =>
          response.url().includes('/api/v1/gesuchDokument') &&
          response.request().method() === 'POST',
      );
      await upload.click();
      await page.getByTestId('file-input').setInputFiles(SmallImageFile);
      await page.keyboard.press('Escape');
      await expect(page.getByTestId('file-input')).toHaveCount(0);
      await uploadCall;
    }

    await page.getByTestId('button-continue').click();

    // Step 11: Freigabe ===========================================================
    await expectStepTitleToContainText('Freigabe', page);

    await page.getByTestId('button-abschluss').click();
    const freigabeResponse = page.waitForResponse(
      '**/api/v1/gesuch/*/einreichen',
    );
    await page.getByTestId('dialog-confirm').click();
    await freigabeResponse;

    // Go to Berechnung (SB-App)
    const urls = getE2eUrls();
    await page.goto(
      `${urls.sb}/verfuegung/${getGesuchId()}/tranche/${getTrancheId()}`,
    );

    await expect(page.getByTestId('zusammenfassung-resultat')).toHaveClass(
      /accept/,
      { timeout: 10000 },
    );

    await page.goto(
      `${urls.sb}/verfuegung/${getGesuchId()}/tranche/${getTrancheId()}/berechnung/1`,
    );

    await expect(
      page.getByTestId('berechnung-persoenlich-total'),
    ).toContainText("- 14'974");
    await expect(page.getByTestId('berechnung-familien-total')).toContainText(
      "- 55'492",
    );
  });
});
