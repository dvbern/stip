package ch.dvbern.stip.berechnung.dto.v1;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
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

    public static ElternteilV1Builder builderWithDefaults() {
        return new ElternteilV1Builder();
    }
}
