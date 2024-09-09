export type PersoenlicheBerechnung = {
  typ: 'persoenlich';
  name: string;
  total: number;
  totalEinnahmen: number;
  totalKosten: number;
  einnahmen: {
    total: number;
    anzahlPersonenImHaushalt: number;
    eigenerHaushalt: boolean;
    nettoerwerbseinkommen: string;
    eoLeistungen: string;
    alimente: string;
    unterhaltsbeitraege: string;
    kinderUndAusbildungszulagen: string;
    ergaenzungsleistungen: string;
    beitraegeGemeindeInstitution: string;
    steuerbaresVermoegen: string;
    anrechenbaresVermoegen: string;
    elterlicheLeistung: string;
    einkommenPartner: string;
    freibetragErwerbseinkommen: string;
    vermoegensanteilInProzent: string;
    limiteAlterAntragsstellerHalbierungElternbeitrag: string;
  };
  kosten: {
    total: number;
    anzahlPersonenImHaushalt: number;
    anteilLebenshaltungskosten: number;
    mehrkostenVerpflegung: number;
    grundbedarfPersonen: number;
    wohnkostenPersonen: number;
    medizinischeGrundversorgungPersonen: number;
    kantonsGemeindesteuern: number;
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
    totalEinkuenfte: string;
    ergaenzungsleistungen: string;
    steuerbaresVermoegen: string;
    anrechenbaresVermoegen: string;
    sauele2: string;
    sauele3: string;
    mietwert: string;
    alimenteOderRenten: string;
    einkommensfreibeitrag: string;
    maxSaeule3a: string;
    freibetragVermoegen: string;
    vermoegensanteilInProzent: string;
  };
  kosten: {
    total: number;
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
  gueltigAb: string;
  gueltigBis: string;
  monate: number;
  total: number;
};
