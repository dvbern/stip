package ch.dvbern.stip.berechnung.dto.v1;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ElternteilV1 {
    int essenskostenPerson1;
    int essenskostenPerson2;
    int grundbedarf;
    int fahrkostenPerson1;
    int fahrkostenPerson2;
    int integrationszulage;
    int steuernBund;
    int steuernStaat;
    int medizinischeGrundversorgung;
    int effektiveWohnkosten;
    int totalEinkuenfte;
    int ergaenzungsleistungen;
    int eigenmietwert;
    int alimente;
    int einzahlungSaeule3a;
    int einzahlungSaeule2;
    int steuerbaresVermoegen;
    boolean selbststaendigErwerbend;
    int anzahlPersonenImHaushalt;
    int anzahlGeschwisterInAusbildung;

    public static ElternteilV1 withDefaults() {
        return new ElternteilV1Builder()
            .essenskostenPerson1(0)
            .essenskostenPerson2(0)
            .grundbedarf(0)
            .fahrkostenPerson1(0)
            .fahrkostenPerson2(0)
            .integrationszulage(0)
            .steuernBund(0)
            .steuernStaat(0)
            .medizinischeGrundversorgung(0)
            .effektiveWohnkosten(0)
            .totalEinkuenfte(0)
            .ergaenzungsleistungen(0)
            .eigenmietwert(0)
            .alimente(0)
            .einzahlungSaeule3a(0)
            .einzahlungSaeule2(0)
            .steuerbaresVermoegen(0)
            .selbststaendigErwerbend(false)
            .anzahlPersonenImHaushalt(0)
            .anzahlGeschwisterInAusbildung(0)
            .build();
    }
}
