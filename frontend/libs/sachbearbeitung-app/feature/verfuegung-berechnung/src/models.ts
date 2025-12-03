export type PersoenlicheBerechnung = {
  typ: 'persoenlich';
  sozialversicherungsnummer: string;
  name: string;
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
    anzahlPersonenImHaushalt: number;
    eigenerHaushalt: boolean;
    einkommen: number;
    einkommenPartner: number;
    einkommenTotal: number;
    einnahmenBGSA: number;
    eoLeistungen: number;
    alimente: number;
    unterhaltsbeitraege: number;
    kinderUndAusbildungszulagen: number;
    kinderUndAusbildungszulagenPartner: number;
    kinderUndAusbildungszulagenKinder: { name: string; value: number }[];
    kinderUndAusbildungszulagenTotal: number;
    ergaenzungsleistungen: number;
    beitraegeGemeindeInstitution: number;
    steuerbaresVermoegen: number;
    anrechenbaresVermoegen: number;
    elterlicheLeistung: number;
    freibetragErwerbseinkommen: number;
    vermoegensanteilInProzent: number;
    limiteAlterAntragsstellerHalbierungElternbeitrag: number;
  };
  kosten: {
    vornamePia: string;
    vornamePartner: string;
    total: number;
    anzahlPersonenImHaushalt: number;
    anteilLebenshaltungskosten: number;
    mehrkostenVerpflegung: number;
    grundbedarfPersonen: number;
    wohnkostenPersonen: number;
    medizinischeGrundversorgungPersonen: number;
    kantonsGemeindesteuern: number;
    kantonsGemeindesteuernPartner: number;
    bundessteuern: number;
    fahrkostenPartner: number;
    verpflegungPartner: number;
    betreuungskostenKinder: number;
    ausbildungskosten: number;
    fahrkosten: number;
  };
};

export type FamilienBerechnung = {
  typ: 'familien';
  nameKey: string;
  year: number;
  total: number;
  totalEinnahmen: number;
  totalKosten: number;
  einnahmen: {
    total: number;
    totalEinkuenfte: number;
    ergaenzungsleistungen: number;
    steuerbaresVermoegen: number;
    anrechenbaresVermoegen: number;
    sauele2: number;
    sauele3: number;
    mietwert: number;
    kinderalimente: number;
    einkommensfreibeitrag: number;
    maxSaeule3a: number;
    freibetragVermoegen: number;
    vermoegensanteilInProzent: number;
  };
  kosten: {
    total: number;
    abzugslimite: number;
    anzahlPersonen: number;
    grundbedarf: number;
    wohnkosten: number;
    medizinischeGrundversorgung: number;
    integrationszulage: number;
    kantonsGemeindesteuern: number;
    bundessteuern: number;
    fahrkosten: number;
    fahrkostenPartner: number;
    verpflegung: number;
    verpflegungPartner: number;
  };
};

export type Berechnung = PersoenlicheBerechnung | FamilienBerechnung;
export type BerechnungsValue = keyof Pick<Berechnung, 'einnahmen' | 'kosten'>;
export type GesamtBerechnung = {
  persoenlich: PersoenlicheBerechnung;
  familien: FamilienBerechnung[];
  total: number;
};
