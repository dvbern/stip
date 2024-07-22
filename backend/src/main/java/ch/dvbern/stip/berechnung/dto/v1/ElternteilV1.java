package ch.dvbern.stip.berechnung.dto.v1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
    int integrationszulage; //TODO
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

    public static ElternteilV1 buildFromDependants(
        final Gesuchsperiode gesuchsperiode,
        final Eltern elternteil,
        final Steuerdaten steuerdaten,
        final int anzahlPersonenImHaushalt,
        final int anzahlGeschwisterInAusbildung
    ) {
        final ElternteilV1Builder builder = builderWithDefaults();

        builder.essenskostenPerson1(steuerdaten.getVerpflegung());
        builder.essenskostenPerson2(steuerdaten.getVerpflegungPartner());

        builder.grundbedarf(BerechnungRequestV1.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt));

        builder.fahrkostenPerson1(steuerdaten.getFahrkosten());
        builder.fahrkostenPerson2(steuerdaten.getFahrkostenPartner());

        // Where do we get this from?
        // TODO: builder.integrationszulage(); gesuchsperiode.getIntegrationszulage();

        builder.steuernBund(steuerdaten.getSteuernBund());
        builder.steuernStaat(steuerdaten.getSteuernStaat());

        builder.medizinischeGrundversorgung(
            BerechnungRequestV1.getMedizinischeGrundversorgung(
                (int) ChronoUnit.YEARS.between(
                    LocalDate.now(),
                    elternteil.getGeburtsdatum()),
                gesuchsperiode)
        );

        builder.effektiveWohnkosten(
            BerechnungRequestV1.getEffektiveWohnkosten(elternteil.getWohnkosten(), gesuchsperiode, anzahlPersonenImHaushalt)
        );

        builder.totalEinkuenfte(steuerdaten.getTotalEinkuenfte());
        builder.ergaenzungsleistungen(steuerdaten.getErgaenzungsleistungen());
        builder.eigenmietwert(steuerdaten.getEigenmietwert());
        builder.einzahlungSaeule2(Objects.requireNonNullElse(steuerdaten.getSaeule2(), 0));
        builder.einzahlungSaeule3a(Objects.requireNonNullElse(steuerdaten.getSaeule3a(), 0));
        builder.steuerbaresVermoegen(steuerdaten.getVermoegen());
        builder.selbststaendigErwerbend(steuerdaten.getIsArbeitsverhaeltnisSelbstaendig());
        builder.anzahlPersonenImHaushalt(anzahlPersonenImHaushalt);
        builder.anzahlGeschwisterInAusbildung(anzahlGeschwisterInAusbildung);

        return builder.build();
    }


}
