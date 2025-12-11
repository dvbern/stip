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
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.berechnung.util.MathUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static ch.dvbern.stip.berechnung.dto.InputUtils.toJahresWert;

@Data
@Builder
@Value
@Jacksonized
public class ElternteilV1 {
    int verpflegungskosten;
    int verpflegungskostenPartner;
    int grundbedarf;
    int fahrkosten;
    int fahrkostenPartner;
    int integrationszulage;
    int integrationszulageAnzahl;
    int integrationszulageTotal;
    int steuernKantonGemeinde;
    int steuernBund;
    int medizinischeGrundversorgung;
    int effektiveWohnkosten;
    int totalEinkuenfte;
    int ergaenzungsleistungen;
    int eigenmietwert;
    int unterhaltsbeitraege;
    int einzahlungSaeule3a;
    int einzahlungSaeule2;
    int vermoegen;
    boolean selbststaendigErwerbend;
    int anzahlPersonenImHaushalt;
    int anzahlGeschwisterInAusbildung;
    int einnahmenBGSA;
    int andereEinnahmen;
    int renten;

    SteuerdatenTyp steuerdatenTyp;

    public static ElternteilV1Builder builderWithDefaults() {
        return new ElternteilV1Builder();
    }

    public static ElternteilV1Builder builderFromDependants(
        final Gesuchsperiode gesuchsperiode,
        final List<Eltern> eltern,
        final Steuererklaerung steuererklaerung,
        final Steuerdaten steuerdaten,
        final int anzahlPersonenImHaushalt,
        final List<AbstractFamilieEntity> kinderDerElternInHaushalten,
        final int anzahlGeschwisterInAusbildung,
        final int anzahlGeschwisterInNachobligatorischerAusbildung,
        final ElternTyp elternTyp,
        final Familiensituation familiensituation,
        final LocalDate ausbildungsBegin
    ) {
        final ElternteilV1Builder builder = new ElternteilV1Builder();

        builder.steuerdatenTyp(steuerdaten.getSteuerdatenTyp());
        builder.verpflegungskosten(steuerdaten.getVerpflegung());
        builder.verpflegungskostenPartner(Objects.requireNonNullElse(steuerdaten.getVerpflegungPartner(), 0));

        builder.grundbedarf(
            BerechnungRequestV1.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt, false)
        );

        builder.fahrkosten(steuerdaten.getFahrkosten());
        builder.fahrkostenPartner(Objects.requireNonNullElse(steuerdaten.getFahrkostenPartner(), 0));

        builder.steuernKantonGemeinde(steuerdaten.getSteuernKantonGemeinde());
        builder.steuernBund(steuerdaten.getSteuernBund());
        int medizinischeGrundversorgung = 0;
        if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {

            for (final var elternteil : eltern) {
                builder
                    .ergaenzungsleistungen(Objects.requireNonNullElse(steuererklaerung.getErgaenzungsleistungen(), 0));
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    elternteil.getGeburtsdatum(),
                    ausbildungsBegin,
                    gesuchsperiode
                );
            }
            for (final var kindDerElternInHaushalten : kinderDerElternInHaushalten) {
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    kindDerElternInHaushalten.getGeburtsdatum(),
                    ausbildungsBegin,
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
            builder.ergaenzungsleistungen(Objects.requireNonNullElse(steuererklaerung.getErgaenzungsleistungen(), 0));

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
                    ausbildungsBegin,
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
                        ausbildungsBegin,
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
                    ausbildungsBegin,
                    gesuchsperiode
                );
                if (Boolean.TRUE.equals(familiensituation.getVaterWiederverheiratet())) {
                    medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                        LocalDate.now().minusYears(29),
                        ausbildungsBegin,
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
                    ausbildungsBegin,
                    gesuchsperiode
                );
                if (Boolean.TRUE.equals(familiensituation.getMutterWiederverheiratet())) {
                    medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                        LocalDate.now().minusYears(29),
                        ausbildungsBegin,
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

        final var integrationzulageAnzahl = anzahlGeschwisterInNachobligatorischerAusbildung + MathUtil.PIA_COUNT;
        builder.integrationszulage(gesuchsperiode.getIntegrationszulage());
        builder.integrationszulageAnzahl(integrationzulageAnzahl);
        builder.integrationszulageTotal(
            Integer.min(
                gesuchsperiode.getIntegrationszulage() * integrationzulageAnzahl,
                gesuchsperiode.getLimiteEkFreibetragIntegrationszulage() - gesuchsperiode.getEinkommensfreibetrag()
            )
        );

        builder.effektiveWohnkosten(
            BerechnungRequestV1.getEffektiveWohnkosten(
                toJahresWert(wohnkosten),
                gesuchsperiode,
                anzahlPersonenImHaushalt
            )
        );
        builder.einnahmenBGSA(Objects.requireNonNullElse(steuererklaerung.getEinnahmenBGSA(), 0));
        builder.andereEinnahmen(Objects.requireNonNullElse(steuererklaerung.getAndereEinnahmen(), 0));
        builder.renten(Objects.requireNonNullElse(steuererklaerung.getRenten(), 0));

        builder.totalEinkuenfte(Objects.requireNonNullElse(steuerdaten.getTotalEinkuenfte(), 0));
        builder.eigenmietwert(Objects.requireNonNullElse(steuerdaten.getEigenmietwert(), 0));
        builder.unterhaltsbeitraege(
            toJahresWert(Objects.requireNonNullElse(steuererklaerung.getUnterhaltsbeitraege(), 0))
        );
        builder.einzahlungSaeule2(Objects.requireNonNullElse(steuerdaten.getSaeule2(), 0));
        builder.einzahlungSaeule3a(Objects.requireNonNullElse(steuerdaten.getSaeule3a(), 0));
        builder.vermoegen(Objects.requireNonNullElse(steuerdaten.getVermoegen(), 0));
        builder.selbststaendigErwerbend(steuerdaten.getIsArbeitsverhaeltnisSelbstaendig());
        builder.anzahlPersonenImHaushalt(anzahlPersonenImHaushalt);
        builder.anzahlGeschwisterInAusbildung(anzahlGeschwisterInAusbildung);

        return builder;
    }

    public static ElternteilV1 buildFromDependants(
        final Gesuchsperiode gesuchsperiode,
        final List<Eltern> eltern,
        final Steuerdaten steuerdaten,
        final Steuererklaerung steuererklaerung,
        final int anzahlPersonenImHaushalt,
        final List<AbstractFamilieEntity> kinderDerElternInHaushalten,
        final int anzahlGeschwisterInAusbildung,
        final int anzahlGeschwisterInNachobligatorischerAusbildung,
        final ElternTyp elternTyp,
        final Familiensituation familiensituation,
        final LocalDate ausbildungsBegin
    ) {
        return builderFromDependants(
            gesuchsperiode,
            eltern,
            steuererklaerung,
            steuerdaten,
            anzahlPersonenImHaushalt,
            kinderDerElternInHaushalten,
            anzahlGeschwisterInAusbildung,
            anzahlGeschwisterInNachobligatorischerAusbildung,
            elternTyp,
            familiensituation,
            ausbildungsBegin
        ).build();
    }
}
