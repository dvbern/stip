package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Value
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

    public static ElternteilV1Builder buildFromDependants(
        final Gesuchsperiode gesuchsperiode,
        final Eltern elternteil,
        final Steuerdaten steuerdaten,
        final int anzahlPersonenImHaushalt) {
        final ElternteilV1Builder builder = builderWithDefaults();
        builder.essenskostenPerson1(steuerdaten.getVerpflegung());
        builder.essenskostenPerson2(steuerdaten.getVerpflegungPartner());

        builder.grundbedarf(getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt));

        builder.fahrkostenPerson1(steuerdaten.getFahrkosten());
        builder.fahrkostenPerson2(steuerdaten.getFahrkostenPartner());



        builder.anzahlPersonenImHaushalt(anzahlPersonenImHaushalt);
        builder.effektiveWohnkosten(getEffektiveWohnkosten(elternteil, gesuchsperiode, anzahlPersonenImHaushalt));


        return builder;
    }

    public static int getEffektiveWohnkosten(final Eltern elternteil, final Gesuchsperiode gesuchsperiode, int anzahlPersonenImHaushalt) {
        int maxWohnkosten = gesuchsperiode.getWohnkostenFam5pluspers();
        switch (anzahlPersonenImHaushalt) {
            case 1:
                maxWohnkosten = gesuchsperiode.getWohnkostenFam1pers();
                break;
            case 2:
                maxWohnkosten = gesuchsperiode.getWohnkostenFam2pers();
                break;
            case 3:
                maxWohnkosten = gesuchsperiode.getWohnkostenFam3pers();
                break;
            case 4:
                maxWohnkosten = gesuchsperiode.getWohnkostenFam4pers();
                break;
            default:
                break;
        }
        return Integer.min(elternteil.getWohnkosten(), maxWohnkosten);
    }

    public static int getGrundbedarf(final Gesuchsperiode gesuchsperiode, final int anzahlPersonenImHaushalt) {
        int grundbedarf = 0;
        switch (anzahlPersonenImHaushalt) {
            case 1:
                grundbedarf = gesuchsperiode.getPerson1();
                break;
            case 2:
                grundbedarf = gesuchsperiode.getPersonen2();
                break;
            case 3:
                grundbedarf = gesuchsperiode.getPersonen3();
                break;
            case 4:
                grundbedarf = gesuchsperiode.getPersonen4();
                break;
            case 5:
                grundbedarf = gesuchsperiode.getPersonen5();
                break;
            case 6:
                grundbedarf = gesuchsperiode.getPersonen6();
                break;
            case 7:
                grundbedarf = gesuchsperiode.getPersonen7();
                break;
            default:
                grundbedarf = gesuchsperiode.getPersonen7() + (anzahlPersonenImHaushalt - 7) * gesuchsperiode.getProWeiterePerson();
                break;
        }
        return grundbedarf;
    }

}
