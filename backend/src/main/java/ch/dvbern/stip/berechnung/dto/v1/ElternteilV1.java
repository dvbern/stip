package ch.dvbern.stip.berechnung.dto.v1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
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

    public static ElternteilV1Builder builderFromDependants(
        final Gesuchsperiode gesuchsperiode,
        final List<Eltern> eltern,
        final Steuerdaten steuerdaten,
        final int anzahlPersonenImHaushalt,
        final List<AbstractFamilieEntity> kinderDerElternInHaushalten,
        final int anzahlGeschwisterInAusbildung,
        final ElternTyp elternTyp
    ) {
        final ElternteilV1Builder builder = new ElternteilV1Builder();

        builder.essenskostenPerson1(steuerdaten.getVerpflegung());
        builder.essenskostenPerson2(steuerdaten.getVerpflegungPartner());

        builder.grundbedarf(
            BerechnungRequestV1.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt, false)
        );

        builder.fahrkostenPerson1(steuerdaten.getFahrkosten());
        builder.fahrkostenPerson2(steuerdaten.getFahrkostenPartner());

        builder.steuernBund(steuerdaten.getSteuernBund());
        builder.steuernStaat(steuerdaten.getSteuernKantonGemeinde());

        int medizinischeGrundversorgung = 0;
        int anzahlKinderInAusbildung = 0;
        if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
            for (final var elternteil : eltern) {
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    (int) ChronoUnit.YEARS.between(elternteil.getGeburtsdatum(), LocalDate.now()),
                    gesuchsperiode
                );
            }
            for (final var kindDerElternInHaushalten : kinderDerElternInHaushalten) {
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    (int) ChronoUnit.YEARS.between(kindDerElternInHaushalten.getGeburtsdatum(), LocalDate.now()),
                    gesuchsperiode
                );
                if (kindDerElternInHaushalten instanceof Geschwister geschwister) {
                    if (geschwister.getAusbildungssituation() != Ausbildungssituation.KEINE) {
                        anzahlKinderInAusbildung += 1;
                    }
                } else {
                    anzahlKinderInAusbildung += 1;
                }
            }
        } else {
            switch (steuerdaten.getSteuerdatenTyp()) {
                case VATER -> {
                    final var kinderDerElternVaterVollzeit  = kinderDerElternInHaushalten.stream().filter(
                        kind -> Objects.requireNonNullElse(kind.getWohnsitzAnteilVater(), BigDecimal.valueOf(100)).intValue() == 100
                    ).toList();
                    for (final var kind : kinderDerElternVaterVollzeit) {
                        medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                            (int) ChronoUnit.YEARS.between(kind.getGeburtsdatum(), LocalDate.now()),
                            gesuchsperiode
                        );
                        if (kind instanceof Geschwister geschwister) {
                            if (geschwister.getAusbildungssituation() != Ausbildungssituation.KEINE) {
                                anzahlKinderInAusbildung += 1;
                            }
                        } else {
                            anzahlKinderInAusbildung += 1;
                        }
                    }
                }
                case MUTTER -> {
                    final var kinderDerElternMutterVollzeit = kinderDerElternInHaushalten.stream().filter(
                        kind -> Objects.requireNonNullElse(kind.getWohnsitzAnteilMutter(), BigDecimal.valueOf(100)).intValue() == 100
                    ).toList();
                    for (final var kind : kinderDerElternMutterVollzeit) {
                        medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                            (int) ChronoUnit.YEARS.between(kind.getGeburtsdatum(), LocalDate.now()),
                            gesuchsperiode
                        );
                        if (kind instanceof Geschwister geschwister) {
                            if (geschwister.getAusbildungssituation() != Ausbildungssituation.KEINE) {
                                anzahlKinderInAusbildung += 1;
                            }
                        } else {
                            anzahlKinderInAusbildung += 1;
                        }
                    }
                }
            }
            final var kinderDerElternTeilzeit = kinderDerElternInHaushalten.stream()
                .filter(
                    kind -> Objects.requireNonNullElse(kind.getWohnsitzAnteilMutter(), BigDecimal.ZERO).intValue() > 0 &&
                            Objects.requireNonNullElse(kind.getWohnsitzAnteilVater(),  BigDecimal.ZERO).intValue() > 0
                ).toList();
            if (
                ((elternTyp == ElternTyp.VATER) && (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.VATER)) ||
                ((elternTyp == ElternTyp.MUTTER) && (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.MUTTER))
            ) {
                for (final var kind : kinderDerElternTeilzeit) {
                    medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                        (int) ChronoUnit.YEARS.between(kind.getGeburtsdatum(), LocalDate.now()),
                        gesuchsperiode
                    );
                    if (kind instanceof Geschwister geschwister) {
                        if (geschwister.getAusbildungssituation() != Ausbildungssituation.KEINE) {
                            anzahlKinderInAusbildung += 1;
                        }
                    } else {
                        anzahlKinderInAusbildung += 1;
                    }
                }
            }
        }

        builder.integrationszulage(
            Integer.min(
                gesuchsperiode.getIntegrationszulage() * anzahlKinderInAusbildung,
                gesuchsperiode.getLimiteEkFreibetragIntegrationszulage() - gesuchsperiode.getEinkommensfreibetrag()
            )
        );

        switch (steuerdaten.getSteuerdatenTyp()) {
            case VATER -> {
                final var elternteilToUse = eltern.stream().filter(
                    elternteil -> elternteil.getElternTyp() == ElternTyp.VATER
                ).toList().get(0);
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    (int) ChronoUnit.YEARS.between(elternteilToUse.getGeburtsdatum(), LocalDate.now()),
                    gesuchsperiode
                );
            }
            case MUTTER -> {
                final var elternteilToUse = eltern.stream().filter(
                    elternteil -> elternteil.getElternTyp() == ElternTyp.MUTTER
                ).toList().get(0);
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    (int) ChronoUnit.YEARS.between(elternteilToUse.getGeburtsdatum(), LocalDate.now()),
                    gesuchsperiode
                );
            }
        }

        builder.medizinischeGrundversorgung(medizinischeGrundversorgung);

        builder.effektiveWohnkosten(
            BerechnungRequestV1.getEffektiveWohnkosten(
                steuerdaten.getWohnkosten(),
                gesuchsperiode,
                anzahlPersonenImHaushalt
            )
        );

        builder.totalEinkuenfte(Objects.requireNonNullElse(steuerdaten.getTotalEinkuenfte(), 0));
        builder.ergaenzungsleistungen(Objects.requireNonNullElse(steuerdaten.getErgaenzungsleistungen(), 0));
        builder.eigenmietwert(Objects.requireNonNullElse(steuerdaten.getEigenmietwert(), 0));
        builder.einzahlungSaeule2(Objects.requireNonNullElse(steuerdaten.getSaeule2(), 0));
        builder.einzahlungSaeule3a(Objects.requireNonNullElse(steuerdaten.getSaeule3a(), 0));
        builder.steuerbaresVermoegen(Objects.requireNonNullElse(steuerdaten.getVermoegen(), 0));
        builder.selbststaendigErwerbend(steuerdaten.getIsArbeitsverhaeltnisSelbstaendig());
        builder.anzahlPersonenImHaushalt(anzahlPersonenImHaushalt);
        builder.anzahlGeschwisterInAusbildung(anzahlGeschwisterInAusbildung);

        return builder;
    }

    public static ElternteilV1 buildFromDependants(
        final Gesuchsperiode gesuchsperiode,
        final List<Eltern> eltern,
        final Steuerdaten steuerdaten,
        final int anzahlPersonenImHaushalt,
        final List<AbstractFamilieEntity> kinderDerElternInHaushalten,
        final int anzahlGeschwisterInAusbildung,
        final ElternTyp elternTyp
    ) {
        return builderFromDependants(
            gesuchsperiode,
            eltern,
            steuerdaten,
            anzahlPersonenImHaushalt,
            kinderDerElternInHaushalten,
            anzahlGeschwisterInAusbildung,
            elternTyp
        ).build();
    }
}
