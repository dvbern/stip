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
import ch.dvbern.stip.berechnung.dto.InputUtils;
import ch.dvbern.stip.berechnung.dto.PersonValueList;
import ch.dvbern.stip.generated.dto.PersonValueItemDto;
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
    int elternhaushalt;
    boolean isInitialized;
    String vorname;
    String nachname;
    String vornamePartner;
    String nachnamePartner;
    String sozialversicherungsnummer;
    String sozialversicherungsnummerPartner;
    LocalDate geburtsdatum;
    LocalDate geburtsdatumPartner;
    SteuerdatenTyp steuerdatenTyp;
    boolean selbststaendigErwerbend;
    int anzahlPersonenImHaushalt;
    int anzahlGeschwisterInAusbildung;
    int steuerjahr;
    String veranlagungscode;

    // Einnahmen
    int totalEinkuenfte;
    Integer einnahmenBGSA;
    Integer ergaenzungsleistungen;
    Integer andereEinnahmen;
    int eigenmietwert;
    Integer unterhaltsbeitraege;
    Integer einzahlungSaeule3a;
    Integer einzahlungSaeule2;
    Integer renten;
    int vermoegen;

    // Kosten
    int grundbedarf;
    int effektiveWohnkosten;
    int medizinischeGrundversorgung;
    int integrationszulage;
    int integrationszulageAnzahl;
    int integrationszulageTotal;
    int steuernKantonGemeinde;
    int steuernBund;
    List<PersonValueItemDto> fahrkostens;
    List<PersonValueItemDto> verpflegungskostens;

    public static ElternteilV1Builder builderWithDefaults() {
        return new ElternteilV1Builder().isInitialized(false);
    }

    public static ElternteilV1Builder builderFromDependants(
        final int elternhaushalt,
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
        final var steuernElternTyp = InputUtils.fromSteuerdatenTyp(steuerdaten.getSteuerdatenTyp());
        final var elternteilsToUse =
            InputUtils.getElterteileToUse(eltern, familiensituation.getElternVerheiratetZusammen(), steuernElternTyp);
        final var elternteil = elternteilsToUse.getLeft();
        final var elternteilPartner = elternteilsToUse.getRight();
        final var elternteilPartnerName = elternteilPartner.map(Eltern::getVorname);

        final var verpflegungskostens = new PersonValueList();
        final var fahrkostens = new PersonValueList();

        builder.steuerdatenTyp(steuerdaten.getSteuerdatenTyp());
        verpflegungskostens.setPersonValue(elternteil.getVorname(), steuerdaten.getVerpflegung());
        fahrkostens.setPersonValue(elternteil.getVorname(), steuerdaten.getFahrkosten());
        elternteilPartnerName.ifPresent(partnerName -> {
            verpflegungskostens.setPartnerValue(partnerName, steuerdaten.getVerpflegungPartner());
            fahrkostens.setPartnerValue(partnerName, steuerdaten.getFahrkostenPartner());
        });

        builder
            .isInitialized(true)
            .elternhaushalt(elternhaushalt)
            .vorname(elternteil.getVorname())
            .nachname(elternteil.getNachname())
            .vornamePartner(elternteilPartnerName.orElse(null))
            .nachnamePartner(elternteilPartner.map(Eltern::getNachname).orElse(null))
            .sozialversicherungsnummer(elternteil.getSozialversicherungsnummer())
            .sozialversicherungsnummerPartner(elternteilPartner.map(Eltern::getSozialversicherungsnummer).orElse(null))
            .geburtsdatum(elternteil.getGeburtsdatum())
            .steuerjahr(steuerdaten.getSteuerjahr())
            .veranlagungscode(steuerdaten.getVeranlagungsStatus())
            .grundbedarf(
                BerechnungRequestV1.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt, false)
            )
            .steuernKantonGemeinde(steuerdaten.getSteuernKantonGemeinde())
            .steuernBund(steuerdaten.getSteuernBund())
            .ergaenzungsleistungen(steuererklaerung.getErgaenzungsleistungen());
        int medizinischeGrundversorgung = 0;
        if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
            for (final var e : eltern) {
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    e.getGeburtsdatum(),
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

        int wohnkosten = elternteil.getWohnkosten();
        if (steuerdaten.getSteuerdatenTyp() != SteuerdatenTyp.FAMILIE) {
            medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                elternteil.getGeburtsdatum(),
                ausbildungsBegin,
                gesuchsperiode
            );
            final var wiederverheiratet = switch (steuerdaten.getSteuerdatenTyp()) {
                case MUTTER -> familiensituation.getMutterWiederverheiratet();
                case VATER -> familiensituation.getVaterWiederverheiratet();
                case FAMILIE -> Boolean.FALSE;
            };
            if (Boolean.TRUE.equals(wiederverheiratet)) {
                // Wir gehen davon aus, dass der Partner eines Elternteils erwachsen ist
                medizinischeGrundversorgung += gesuchsperiode.getErwachsene2599();
            }
        }

        builder.medizinischeGrundversorgung(medizinischeGrundversorgung);

        final var integrationzulageAnzahl = anzahlGeschwisterInNachobligatorischerAusbildung + InputUtils.PIA_COUNT;
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
        builder.einnahmenBGSA(steuererklaerung.getEinnahmenBGSA());
        builder.andereEinnahmen(steuererklaerung.getAndereEinnahmen());
        builder.renten(steuererklaerung.getRenten());

        builder.totalEinkuenfte(steuerdaten.getTotalEinkuenfte());
        builder.eigenmietwert(steuerdaten.getEigenmietwert());
        builder.unterhaltsbeitraege(
            toJahresWert(Objects.requireNonNullElse(steuererklaerung.getUnterhaltsbeitraege(), 0))
        );
        builder.einzahlungSaeule2(steuerdaten.getSaeule2());
        builder.einzahlungSaeule3a(steuerdaten.getSaeule3a());
        builder.vermoegen(steuerdaten.getVermoegen());
        builder.selbststaendigErwerbend(steuerdaten.getIsArbeitsverhaeltnisSelbstaendig());
        builder.anzahlPersonenImHaushalt(anzahlPersonenImHaushalt);
        builder.anzahlGeschwisterInAusbildung(anzahlGeschwisterInAusbildung);
        builder.verpflegungskostens(verpflegungskostens.toList());
        builder.fahrkostens(fahrkostens.toList());

        return builder;
    }

    public static ElternteilV1 buildFromDependants(
        final int elternhaushalt,
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
        return builderFromDependants(
            elternhaushalt,
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
