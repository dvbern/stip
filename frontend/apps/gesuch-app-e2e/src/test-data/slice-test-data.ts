import {
  Adresse,
  EinnahmenKosten,
  Eltern,
  Familiensituation,
  FreiwilligDarlehen,
  Geschwister,
  LebenslaufItem,
  PersonInAusbildung,
  Steuerdaten,
  SteuererklaerungUpdate,
  Zahlungsverbindung,
} from '@dv/shared/model/gesuch';
import {
  AusbildungValues,
  fruehlingOrHerbst,
  generateSVN,
  specificMonthPlusYears,
  specificYearsAgo,
} from '@dv/shared/util-fn/e2e-util';

export const ausbildung: AusbildungValues = {
  fallId: '',
  status: 'AKTIV',
  editable: true,
  ausbildungsortPLZ: '3011',
  ausbildungsort: 'Bern',
  ausbildungsstaetteText: 'Universität Bern',
  ausbildungsgangText: 'Bachelor',
  fachrichtungBerufsbezeichnung: 'Kunstgeschichte',
  ausbildungBegin: `${fruehlingOrHerbst()}.${specificYearsAgo(1)}`,
  ausbildungEnd: specificMonthPlusYears(8, 3),
  pensum: 'VOLLZEIT',
};

export const zahlungsverbindung: Zahlungsverbindung = {
  vorname: 'Spoerri',
  nachname: 'Spoerri',
  iban: '1809000000150664878',
  adresse: {
    landId: 'Schweiz',
    strasse: 'Huberstrasse',
    hausnummer: '5a',
    plz: '3008',
    ort: 'Bern',
  },
};

export const adressen = {
  person: {
    landId: 'Schweiz',
    coAdresse: '',
    strasse: 'Kramgasse',
    hausnummer: '1',
    plz: '3011',
    ort: 'Bern',
  },
  mutter: {
    landId: 'Schweiz',
    coAdresse: '',
    strasse: 'Aarbergergasse',
    hausnummer: '1',
    plz: '3065',
    ort: 'Bolligen',
  },
} as const satisfies Record<string, Adresse>;

export const person = (seed: string): PersonInAusbildung => ({
  sozialversicherungsnummer: generateSVN(seed + '_person'),
  anrede: 'HERR',
  nachname: 'Muster',
  vorname: 'Fritz',
  adresse: adressen.person,
  identischerZivilrechtlicherWohnsitz: true,
  email: 'max.muster@dvbern.ch',
  telefonnummer: '0316338555',
  geburtsdatum: `01.01.${specificYearsAgo(20)}`,
  nationalitaetId: 'Schweiz',
  heimatortPLZ: '3011',
  heimatort: 'Bern',
  zivilstand: 'LEDIG',
  wohnsitz: 'MUTTER_VATER',
  wohnsitzAnteilMutter: 100,
  sozialhilfebeitraege: false,
  korrespondenzSprache: 'DEUTSCH',
});

export const taetigkeit: LebenslaufItem = {
  taetigkeitsart: 'ERWERBSTAETIGKEIT',
  taetigkeitsBeschreibung: 'Serviceangestellter',
  von: `01.${specificYearsAgo(4)}`,
  bis: `08.${specificYearsAgo(1)}`,
  wohnsitz: 'BE',
  id: '',
};

export const familienlsituation: Familiensituation = {
  elternVerheiratetZusammen: false,
  gerichtlicheAlimentenregelung: false,
  elternteilUnbekanntVerstorben: true,
  mutterUnbekanntVerstorben: 'WEDER_NOCH',
  vaterUnbekanntVerstorben: 'VERSTORBEN',
};

export const mutter = (seed: string): Eltern => ({
  sozialversicherungsnummer: generateSVN(seed + '_mutter'),
  nachname: 'Tester',
  vorname: 'Mutter1',
  adresse: adressen.mutter,
  identischerZivilrechtlicherWohnsitz: true,
  telefonnummer: '0316338355',
  sozialhilfebeitraege: false,
  wiederverheiratet: false,
  wohnkosten: 16260,
  geburtsdatum: `01.01.${specificYearsAgo(44)}`,
  ausweisbFluechtling: false,
  elternTyp: 'MUTTER',
  id: '',
});

export const steuererklaerung: Omit<SteuererklaerungUpdate, 'steuerdatenTyp'> =
  {
    steuererklaerungInBern: true,
    ergaenzungsleistungen: 1200,
    unterhaltsbeitraege: 250,
    renten: 600,
    einnahmenBGSA: 100,
    andereEinnahmen: 300,
  };

export const steuerdaten: Steuerdaten = {
  steuerdatenTyp: 'MUTTER',
  totalEinkuenfte: 8620,
  eigenmietwert: 0,
  isArbeitsverhaeltnisSelbstaendig: false,
  vermoegen: 0,
  steuernKantonGemeinde: 0,
  steuernBund: 0,
  fahrkosten: 0,
  fahrkostenPartner: 0,
  verpflegung: 0,
  verpflegungPartner: 0,
  steuerjahr: +specificYearsAgo(2),
  veranlagungsStatus: 'Provisorisch Veranlagt',
};

export const bruder: Geschwister = {
  nachname: 'Tester',
  vorname: 'Geschwister1',
  geburtsdatum: `01.01.${specificYearsAgo(19)}`,
  wohnsitz: 'MUTTER_VATER',
  ausbildungssituation: 'IN_AUSBILDUNG',
  geschwisterTyp: 'LEIBLICH',
  entryId: '',
};

export const einnahmenKosten: EinnahmenKosten = {
  nettoerwerbseinkommen: 10000,
  arbeitspensumProzent: 50,
  zulagen: 0,
  renten: 1200,
  eoLeistungen: 0,
  ergaenzungsleistungen: 0,
  beitraege: 3000,
  ausbildungskosten: 1980,
  fahrkosten: 798,
  auswaertigeMittagessenProWoche: 5,
  vermoegen: 2000,
  steuerjahr: +specificYearsAgo(1),
};

export const einnhamenKostenSb: Omit<
  EinnahmenKosten,
  'nettoerwerbseinkommen' | 'fahrkosten'
> = {
  veranlagungsStatus: 'Provisorisch Veranlagt',
  steuern: 0,
};

export const darlehen: FreiwilligDarlehen = {
  id: '',
};
