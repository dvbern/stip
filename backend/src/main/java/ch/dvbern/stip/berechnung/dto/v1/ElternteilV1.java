/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.berechnung.dto.v1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
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
        final int anzahlGeschwisterInNachobligatorischerAusbildung,
        final ElternTyp elternTyp,
        final Familiensituation familiensituation
    ) {
        final ElternteilV1Builder builder = new ElternteilV1Builder();

        builder.essenskostenPerson1(steuerdaten.getVerpflegung());
        builder.essenskostenPerson2(Objects.requireNonNullElse(steuerdaten.getVerpflegungPartner(), 0));

        builder.grundbedarf(
            BerechnungRequestV1.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt, false)
        );

        builder.fahrkostenPerson1(steuerdaten.getFahrkosten());
        builder.fahrkostenPerson2(Objects.requireNonNullElse(steuerdaten.getFahrkostenPartner(), 0));

        builder.steuernBund(steuerdaten.getSteuernBund());
        builder.steuernStaat(steuerdaten.getSteuernKantonGemeinde());

        int medizinischeGrundversorgung = 0;
        if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
            for (final var elternteil : eltern) {
                builder.ergaenzungsleistungen(Objects.requireNonNullElse(elternteil.getErgaenzungsleistungen(), 0));
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    elternteil.getGeburtsdatum(),
                    gesuchsperiode
                );
            }
            for (final var kindDerElternInHaushalten : kinderDerElternInHaushalten) {
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    kindDerElternInHaushalten.getGeburtsdatum(),
                    gesuchsperiode
                );
            }
        } else {
            final var steuernElternTyp =
                steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.MUTTER
                    ? ElternTyp.MUTTER
                    : ElternTyp.VATER; // Never is Family
            final var elternteilToUse = eltern.stream()
                .filter(
                    elternteil -> elternteil.getElternTyp() == steuernElternTyp
                )
                .toList()
                .get(0);
            builder.ergaenzungsleistungen(Objects.requireNonNullElse(elternteilToUse.getErgaenzungsleistungen(), 0));

            final var kindDesElternteilsVollzeit = kinderDerElternInHaushalten.stream()
                .filter(
                    kind -> Objects
                        .requireNonNullElse(kind.getWohnsitzAnteil(steuernElternTyp), BigDecimal.valueOf(100))
                        .intValue() == 100
                )
                .toList();
            for (final var kind : kindDesElternteilsVollzeit) {
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    kind.getGeburtsdatum(),
                    gesuchsperiode
                );
            }

            final var kinderDerElternTeilzeit = kinderDerElternInHaushalten.stream()
                .filter(
                    kind -> Objects.requireNonNullElse(kind.getWohnsitzAnteilMutter(), BigDecimal.ZERO).intValue() > 0
                    &&
                    Objects.requireNonNullElse(kind.getWohnsitzAnteilVater(), BigDecimal.ZERO).intValue() > 0
                )
                .toList();
            if (
                ((elternTyp == ElternTyp.VATER) && (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.VATER)) ||
                ((elternTyp == ElternTyp.MUTTER) && (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.MUTTER))
            ) {
                for (final var kind : kinderDerElternTeilzeit) {
                    medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                        kind.getGeburtsdatum(),
                        gesuchsperiode
                    );
                }
            }
        }

        int wohnkosten = 0;
        switch (steuerdaten.getSteuerdatenTyp()) {
            case VATER -> {
                final var elternteilToUse = eltern.stream()
                    .filter(
                        elternteil -> elternteil.getElternTyp() == ElternTyp.VATER
                    )
                    .toList()
                    .get(0);
                wohnkosten += elternteilToUse.getWohnkosten();
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    elternteilToUse.getGeburtsdatum(),
                    gesuchsperiode
                );
                if (Boolean.TRUE.equals(familiensituation.getVaterWiederverheiratet())) {
                    medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                        LocalDate.now().minusYears(29),
                        gesuchsperiode // Wir gehen davon aus, dass der Partner eines Elternteils älter als 25 ist.
                                       // 29 für margin
                    );
                }
            }
            case MUTTER -> {
                final var elternteilToUse = eltern.stream()
                    .filter(
                        elternteil -> elternteil.getElternTyp() == ElternTyp.MUTTER
                    )
                    .toList()
                    .get(0);
                wohnkosten += elternteilToUse.getWohnkosten();
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    elternteilToUse.getGeburtsdatum(),
                    gesuchsperiode
                );
                if (Boolean.TRUE.equals(familiensituation.getMutterWiederverheiratet())) {
                    medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                        LocalDate.now().minusYears(29),
                        gesuchsperiode // Wir gehen davon aus, dass der Partner eines Elternteils älter als 25 ist.
                                       // 29 für margin
                    );
                }
            }
            case FAMILIE -> {
                final var elternteilToUse = eltern.stream().findFirst().get();
                wohnkosten += elternteilToUse.getWohnkosten();
            }
        }

        builder.medizinischeGrundversorgung(medizinischeGrundversorgung);

        builder.integrationszulage(
            Integer.min(
                gesuchsperiode.getIntegrationszulage() * (anzahlGeschwisterInNachobligatorischerAusbildung + 1),
                gesuchsperiode.getLimiteEkFreibetragIntegrationszulage() - gesuchsperiode.getEinkommensfreibetrag()
            )
        );

        builder.effektiveWohnkosten(
            BerechnungRequestV1.getEffektiveWohnkosten(
                wohnkosten,
                gesuchsperiode,
                anzahlPersonenImHaushalt
            )
        );

        builder.totalEinkuenfte(Objects.requireNonNullElse(steuerdaten.getTotalEinkuenfte(), 0));
        builder.eigenmietwert(Objects.requireNonNullElse(steuerdaten.getEigenmietwert(), 0));
        builder.alimente(Objects.requireNonNullElse(steuerdaten.getKinderalimente(), 0));
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
        final int anzahlGeschwisterInNachobligatorischerAusbildung,
        final ElternTyp elternTyp,
        final Familiensituation familiensituation
    ) {
        return builderFromDependants(
            gesuchsperiode,
            eltern,
            steuerdaten,
            anzahlPersonenImHaushalt,
            kinderDerElternInHaushalten,
            anzahlGeschwisterInAusbildung,
            anzahlGeschwisterInNachobligatorischerAusbildung,
            elternTyp,
            familiensituation
        ).build();
    }

    public static int getEffektiveWohnkosten(
        final int eingegebeneWohnkosten,
        final Gesuchsperiode gesuchsperiode,
        int anzahlPersonenImHaushalt
    ) {
        int maxWohnkosten = switch (anzahlPersonenImHaushalt) {
            case 0 -> throw new IllegalStateException("0 Personen im Haushalt");
            case 1 -> gesuchsperiode.getWohnkostenFam1pers();
            case 2 -> gesuchsperiode.getWohnkostenFam2pers();
            case 3 -> gesuchsperiode.getWohnkostenFam3pers();
            case 4 -> gesuchsperiode.getWohnkostenFam4pers();
            default -> gesuchsperiode.getWohnkostenFam5pluspers();
        };
        return Integer.min(eingegebeneWohnkosten, maxWohnkosten);
    }
}
