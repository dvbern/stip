import {
  Adresse,
  Darlehen,
  EinnahmenKosten,
  Eltern,
  Familiensituation,
  Geschwister,
  LebenslaufItem,
  PersonInAusbildung,
  Steuerdaten,
  SteuererklaerungUpdate,
  Zahlungsverbindung,
} from '@dv/shared/model/gesuch';
import {
  generateSVN,
  specificMonthPlusYears,
  specificYearsAgo,
} from '@dv/shared/util-fn/e2e-util';

import { AusbildungValues } from '../po/ausbildung.po';

export const ausbildung: AusbildungValues = {
  fallId: '',
  status: 'AKTIV',
  editable: true,
  ausbildungsort: 'Bern',
  ausbildungsstaetteText: 'Universität Bern',
  ausbildungsgangText: 'Master',
  fachrichtung: 'Kunstgeschichte',
  ausbildungBegin: `01.09.${specificYearsAgo(1)}`,
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
  mutterWiederverheiratet: false,
  vaterUnbekanntVerstorben: 'VERSTORBEN',
};

export const mutter = (seed: string): Eltern => ({
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

export const steuererklaerung: Omit<SteuererklaerungUpdate, 'steuerdatenTyp'> =
  {
    steuererklaerungInBern: true,
  };

export const steuerdaten: Steuerdaten = {
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
  veranlagungsCode: 23,
};

export const bruder: Geschwister = {
  nachname: 'Tester',
  vorname: 'Geschwister1',
  geburtsdatum: `01.01.${specificYearsAgo(19)}`,
  wohnsitz: 'MUTTER_VATER',
  ausbildungssituation: 'IN_AUSBILDUNG',
  id: '',
};

export const einnahmenKosten: EinnahmenKosten = {
  nettoerwerbseinkommen: 10000,
  zulagen: 0,
  renten: 1200,
  eoLeistungen: 0,
  ergaenzungsleistungen: 0,
  beitraege: 3000,
  ausbildungskosten: 1980,
  fahrkosten: 798,
  auswaertigeMittagessenProWoche: 5,
  vermoegen: 6,
  steuerjahr: +specificYearsAgo(1),
  veranlagungsCode: 0,
  steuernKantonGemeinde: 0,
  verdienstRealisiert: false,
};

export const darlehen: Darlehen = {
  willDarlehen: false,
};
