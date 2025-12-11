import { PersonValueItem, SteuerdatenTyp } from '@dv/shared/model/gesuch';

export type PersoenlicheBerechnung = {
  typ: 'persoenlich';
  name: string;
  sozialversicherungsnummer: string;
  geburtsdatum: string;
  total: number;
  yearRange: string;
  gueltigAb: string;
  gueltigBis: string;
  monate: number;
  berechnung: number;
  totalEinnahmen: number;
  totalKosten: number;
  totalVorTeilung?: number;
  geteilteBerechnung: {
    berechnungsanteilKinder: number;
    anteil: number;
  } | null;
  verminderteBerechnung?: {
    monate: number;
    unreducedTotal: number;
  };
  einnahmen: {
    vornamePia: string;
    vornamePartner: string;
    total: number;

    nettoerwerbseinkommen: number;
    nettoerwerbseinkommenPartner: number;
    nettoerwerbseinkommenTotal: number;
    einnahmenBGSA: number;
    kinderAusbildungszulagen: number;
    kinderAusbildungszulagenPartner: number;
    kinderAusbildungszulagenKinder: PersonValueItem[];
    kinderAusbildungszulagenTotal: number;
    unterhaltsbeitraege: number;
    unterhaltsbeitraegePartner: number;
    unterhaltsbeitraegeKinder: PersonValueItem[];
    unterhaltsbeitraegeTotal: number;
    eoLeistungen: number;
    eoLeistungenPartner: number;
    eoLeistungenTotal: number;
    taggelderAHVIV: number;
    taggelderAHVIVPartner: number;
    taggelderAHVIVTotal: number;
    renten: number;
    rentenPartner: number;
    rentenKinder: PersonValueItem[];
    rentenTotal: number;
    ergaenzungsleistungen: number;
    ergaenzungsleistungenPartner: number;
    ergaenzungsleistungenKinder: PersonValueItem[];
    ergaenzungsleistungenTotal: number;
    beitraegeGemeindeInstitutionen: number;
    andereEinnahmen: number;
    andereEinnahmenPartner: number;
    andereEinnahmenKinder: PersonValueItem[];
    andereEinnahmenTotal: number;
    anrechenbaresVermoegen: number;
    steuerbaresVermoegen: number;
    limiteAlterAntragsstellerHalbierungElternbeitrag: number;
  };
  kosten: {
    vornamePia: string;
    vornamePartner: string;
    anzahlPersonenImHaushalt: number;
    total: number;

    ausbildungskosten: number;
    fahrkosten: number;
    verpflegungskosten: number;
    grundbedarf: number;
    wohnkosten: number;
    medizinischeGrundversorgung: PersonValueItem[];
    fahrkostenPartner: number;
    verpflegungPartner: number;
    betreuungskostenKinder: number;
    kantonsGemeindesteuern: number;
    bundessteuern: number;
    anteilLebenshaltungskosten: number;
  };
};

export type FamilienBerechnung = {
  typ: 'familien';
  familienBudgetTyp: SteuerdatenTyp;
  name: string; // TODO: what if Familie?
  sozialversicherungsnummer: string; // TODO: what if Familie?
  geburtsdatum: string; // TODO: what if Familie?
  steuerjahr: number;
  veranlagungsStatus: string;
  gueltigAb: string;
  gueltigBis: string;
  monate: number;
  total: number;
  totalEinnahmen: number;
  totalKosten: number;
  einnahmen: {
    total: number;

    totalEinkuenfte: number;
    einnahmenBGSA: number;
    ergaenzungsleistungen: number;
    andereEinnahmen: number;
    eigenmietwert: number;
    unterhaltsbeitraege: number;
    sauele3: number;
    sauele2: number;
    renten: number;
    einkommensfreibetrag: number;
    einkommensfreibetragLimite: number;
    zwischentotal: number;
    anrechenbaresVermoegen: number;
    freibetragVermoegen: number;
    steuerbaresVermoegen: number;
  };
  kosten: {
    anzahlPersonenImHaushalt: number;
    total: number;

    grundbedarf: number;
    wohnkosten: number;
    medizinischeGrundversorgung: number;
    integrationszulage: number;
    integrationszulageAnzahl: number;
    integrationszulageLimite: number;
    integrationszulageTotal: number;
    kantonsGemeindesteuern: number;
    bundessteuern: number;
    fahrkosten: PersonValueItem[];
    verpflegung: PersonValueItem[];
  };
};

export type Berechnung = PersoenlicheBerechnung | FamilienBerechnung;
export type BerechnungsValue = keyof Pick<Berechnung, 'einnahmen' | 'kosten'>;
export type GesamtBerechnung = {
  persoenlich: PersoenlicheBerechnung;
  familien: FamilienBerechnung[];
  total: number;
};
