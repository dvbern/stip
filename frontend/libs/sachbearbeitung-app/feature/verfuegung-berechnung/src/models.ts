export type PersoenlicheBerechnung = {
  typ: 'persoenlich';
  name: string;
  total: number;
  totalEinnahmen: number;
  totalKosten: number;
  einnahmen: {
    total: number;
    nettoerwerbseinkommen: string;
    eoLeistungen: string;
    unterhaltsbeitraege: string;
    kinderUndAusbildungszulagen: string;
    ergaenzungsleistungen: string;
    beitraegeGemeindeInstitution: string;
    steuerbaresVermoegen: string;
    elterlicheLeistung: string;
    einkommenPartner: string;
  };
  kosten: {
    total: number;
    anteilLebenshaltungskosten: string;
    mehrkostenVerpflegung: string;
    grundbedarf0Personen: string;
    wohnkosten0Personen: string;
    medizinischeGrundversorgung0Personen: string;
    kantonsGemeindesteuern: string;
    bundessteuern: string;
    fahrkostenPartner: string;
    verpflegungPartner: string;
    betreuungskostenKinder: string;
    ausbildungskosten: string;
    fahrkosten: string;
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
    vermoegensaufrechnung: string;
    abzuege: string;
    beitraegeSaule: string;
    mietwert: string;
    alimenteOderRenten: string;
    einkommensfreibeitrag: string;
  };
  kosten: {
    total: number;
    anzahlPersonen: string;
    grundbedarf: string;
    wohnkosten: string;
    medizinischeGrundversorgung: string;
    integrationszulage: string;
    kantonsGemeindesteuern: string;
    bundessteuern: string;
    fahrkosten: string;
    fahrkostenPartner: string;
    verpflegung: string;
    verpflegungPartner: string;
  };
};

export type Berechnung = PersoenlicheBerechnung | FamilienBerechnung;
export type BerechnungsValue = keyof Pick<Berechnung, 'einnahmen' | 'kosten'>;
