import {
  AbschlussSlim,
  AuszahlungUpdate,
  GesuchFormularUpdate,
} from '@dv/shared/model/gesuch';
import {
  ExplicitNull,
  fruehlingOrHerbst,
  generateSVN,
  specificMonthPlusYears,
  specificYearsAgo,
} from '@dv/shared/util-fn/e2e-util';

import { AusbildungValues } from '../po/ausbildung.po';

export const ausbildungValues: AusbildungValues = {
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

export const createZahlungsverbindungUpdateFn = (
  landId: string,
): AuszahlungUpdate => ({
  auszahlungAnSozialdienst: false,
  zahlungsverbindung: {
    vorname: 'E2E',
    nachname: 'Sanchez',
    iban: 'CH1809000000150664878',
    adresse: {
      landId: landId,
      strasse: 'Strasse',
      hausnummer: '5a',
      plz: '3008',
      ort: 'Bern',
    },
  },
});

export const gesuchFormularUpdateFn = (
  seed: string,
  abschluesse: AbschlussSlim[],
  landId: string,
): ExplicitNull<GesuchFormularUpdate> => ({
  personInAusbildung: {
    sozialversicherungsnummer: generateSVN(seed + '_person'),
    anrede: 'FRAU',
    nachname: 'Sanchez',
    vorname: 'E2E',
    adresse: {
      id: null,
      coAdresse: null,
      strasse: 'Hausmatte',
      hausnummer: '42B',
      landId: landId,
      plz: '3032',
      ort: 'Hinterkappelen',
    },
    identischerZivilrechtlicherWohnsitz: true,
    identischerZivilrechtlicherWohnsitzPLZ: null,
    identischerZivilrechtlicherWohnsitzOrt: null,
    email: 'stip-laura-sanchez@mailbucket.dvbern.ch',
    telefonnummer: '0791231212',
    geburtsdatum: `${specificYearsAgo(20)}-01-01`,
    nationalitaetId: landId,
    heimatortPLZ: '3011',
    heimatort: 'Bern',
    niederlassungsstatus: null,
    vormundschaft: false,
    zivilstand: 'LEDIG',
    wohnsitz: 'EIGENER_HAUSHALT',
    sozialhilfebeitraege: false,
    korrespondenzSprache: 'DEUTSCH',
    einreisedatum: null,
    wohnsitzAnteilMutter: null,
    wohnsitzAnteilVater: null,
    zustaendigeKESB: null,
  },
  familiensituation: {
    elternVerheiratetZusammen: false,
    elternteilUnbekanntVerstorben: true,
    gerichtlicheAlimentenregelung: false,
    mutterUnbekanntVerstorben: 'VERSTORBEN',
    mutterUnbekanntGrund: null,
    vaterUnbekanntVerstorben: 'VERSTORBEN',
    vaterUnbekanntGrund: null,
    werZahltAlimente: null,
  },
  partner: null,
  elterns: null,
  steuererklaerung: null,
  geschwisters: null,
  lebenslaufItems: [
    {
      id: null,
      von: `01.${specificYearsAgo(4)}`,
      bis: `08.${specificYearsAgo(1)}`,
      wohnsitz: 'AG',
      abschlussId:
        abschluesse.find(
          (abschluss) =>
            abschluss.bezeichnungDe.includes('Eidg. Berufsattest') &&
            abschluss.ausbildungskategorie ===
              'BERUFSFACHSCHULEN_UEBERBETRIEBLICHE_KURSE',
        )?.id ?? null,
      ausbildungAbgeschlossen: false,
      fachrichtungBerufsbezeichnung: 'Informatik',
      taetigkeitsart: null,
      taetigkeitsBeschreibung: null,
      invalid: null,
    },
  ],
  kinds: null,
  einnahmenKosten: {
    nettoerwerbseinkommen: 2000,
    arbeitspensumProzent: 50,
    fahrkosten: 0,
    zulagen: 600,
    renten: 0,
    eoLeistungen: 0,
    ergaenzungsleistungen: 420,
    beitraege: 0,
    ausbildungskosten: 300,
    wohnkosten: 1500,
    wgWohnend: false,
    auswaertigeMittagessenProWoche: null,
    veranlagungsStatus: 'Provisorisch Veranlagt',
    steuerjahr: 2023,
    vermoegen: 20900,
    steuern: 0,
    unterhaltsbeitraege: null,
    einnahmenBGSA: null,
    taggelderAHVIV: null,
    andereEinnahmen: null,
    wgAnzahlPersonen: null,
    alternativeWohnformWohnend: false,
    verpflegungskosten: null,
  },
  einnahmenKostenPartner: null,
});
