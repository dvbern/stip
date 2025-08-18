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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMappingUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Jacksonized
@Value
@Slf4j
public class AntragsstellerV1 {
    int piaWohntInElternHaushalt;
    boolean tertiaerstufe;
    int einkommen;
    int einkommenPartner;
    int vermoegen;
    int alimente;
    int rente;
    int kinderAusbildungszulagen;
    int ergaenzungsleistungen;
    int leistungenEO;
    int gemeindeInstitutionen;
    int alter;
    int grundbedarf;
    int wohnkosten;
    int medizinischeGrundversorgung;
    int ausbildungskosten;
    int steuern;
    int steuernKonkubinatspartner;
    int fahrkosten;
    int fahrkostenPartner;
    int verpflegung;
    int verpflegungPartner;
    int fremdbetreuung;
    int anteilFamilienbudget;
    boolean lehre;
    boolean eigenerHaushalt;
    boolean halbierungElternbeitrag;
    int anzahlPersonenImHaushalt;
    boolean verheiratetKonkubinat;

    public static AntragsstellerV1 buildFromDependants(
        final GesuchFormular gesuchFormular,
        final int piaWohntInElternHaushalt
    ) {
        final var personInAusbildung = gesuchFormular.getPersonInAusbildung();
        final var partner = gesuchFormular.getPartner();
        final var einnahmenKosten = gesuchFormular.getEinnahmenKosten();
        final var gesuchsperiode = gesuchFormular.getTranche().getGesuch().getGesuchsperiode();
        final var ausbildung = gesuchFormular.getAusbildung();

        final AntragsstellerV1Builder builder = new AntragsstellerV1Builder();
        builder
            .piaWohntInElternHaushalt(piaWohntInElternHaushalt)
            .tertiaerstufe(
                ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie().isTertiaerstufe()
            )
            .einkommen(einnahmenKosten.getNettoerwerbseinkommen())
            .vermoegen(Objects.requireNonNullElse(einnahmenKosten.getVermoegen(), 0))
            .alimente(Objects.requireNonNullElse(einnahmenKosten.getAlimente(), 0))
            .rente(Objects.requireNonNullElse(einnahmenKosten.getRenten(), 0))
            .kinderAusbildungszulagen(Objects.requireNonNullElse(einnahmenKosten.getZulagen(), 0))
            .ergaenzungsleistungen(Objects.requireNonNullElse(einnahmenKosten.getErgaenzungsleistungen(), 0))
            .leistungenEO(Objects.requireNonNullElse(einnahmenKosten.getEoLeistungen(), 0))
            .gemeindeInstitutionen(Objects.requireNonNullElse(einnahmenKosten.getBeitraege(), 0));

        int alter = DateUtil.getAgeInYears(personInAusbildung.getGeburtsdatum());
        builder.alter(alter);

        int medizinischeGrundversorgung = 0;
        int anzahlPersonenImHaushalt = 0;
        if (personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT) {
            anzahlPersonenImHaushalt = 1;
            medizinischeGrundversorgung +=
                BerechnungRequestV1
                    .getMedizinischeGrundversorgung(
                        personInAusbildung.getGeburtsdatum(),
                        ausbildung.getAusbildungBegin(),
                        gesuchsperiode
                    );
            if (partner != null) {
                anzahlPersonenImHaushalt += 1;
                medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                    partner.getGeburtsdatum(),
                    ausbildung.getAusbildungBegin(),
                    gesuchsperiode
                );
            }
            for (final var kind : gesuchFormular.getKinds()) {
                // if child does still live with the parents/ a parent
                if (kind.getWohnsitzAnteilPia() > 0) {
                    anzahlPersonenImHaushalt += 1;
                    medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                        kind.getGeburtsdatum(),
                        ausbildung.getAusbildungBegin(),
                        gesuchsperiode
                    );
                }
            }

            builder.grundbedarf(
                BerechnungRequestV1.getGrundbedarf(
                    gesuchsperiode,
                    anzahlPersonenImHaushalt,
                    Boolean.TRUE.equals(einnahmenKosten.getWgWohnend())
                )
            );
        } else {
            builder.grundbedarf(0);
        }
        builder.medizinischeGrundversorgung(medizinischeGrundversorgung);

        builder.wohnkosten(0);
        if (einnahmenKosten.getWohnkosten() != null && anzahlPersonenImHaushalt > 0) {
            builder.wohnkosten(
                getEffektiveWohnkosten(
                    einnahmenKosten.getWohnkosten(),
                    gesuchsperiode,
                    anzahlPersonenImHaushalt
                )
            );
        }

        builder.anzahlPersonenImHaushalt(anzahlPersonenImHaushalt);

        builder.ausbildungskosten(
            getAusbildungskosten(
                einnahmenKosten,
                gesuchsperiode,
                ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie()
            )
        );
        builder.steuern(
            EinnahmenKostenMappingUtil.calculateSteuern(gesuchFormular)
            // TODO: + einnahmenKosten.getSteuernStaat() + einnahmenKosten.getSteuernBund()
        );
        builder.fahrkosten(Objects.requireNonNullElse(einnahmenKosten.getFahrkosten(), 0));
        builder.verpflegung(Objects.requireNonNullElse(einnahmenKosten.getAuswaertigeMittagessenProWoche(), 0));
        builder.fremdbetreuung(Objects.requireNonNullElse(einnahmenKosten.getBetreuungskostenKinder(), 0));
        // TODO: builder.anteilFamilienbudget(Objects.requireNonNullElse());
        final var abschluss = gesuchFormular.getAusbildung().getAusbildungsgang().getAbschluss();

        final boolean isLehre = abschluss.getBildungsrichtung()
            .equals(
                Bildungsrichtung.BERUFLICHE_GRUNDBILDUNG
            )
        && abschluss.isBerufsbefaehigenderAbschluss();
        builder.lehre(isLehre);
        builder.eigenerHaushalt(personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT);

        builder.halbierungElternbeitrag(
            getHalbierungElternbeitrag(
                personInAusbildung.getGeburtsdatum(),
                gesuchFormular.getLebenslaufItems(),
                gesuchFormular.getTranche().getGesuch()
            )
        );

        if (partner != null) {
            builder.einkommenPartner(Objects.requireNonNullElse(partner.getJahreseinkommen(), 0));
            // TODO: builder.steuernKonkubinatspartner();
            builder.fahrkostenPartner(Objects.requireNonNullElse(partner.getFahrkosten(), 0));
            builder.verpflegungPartner(Objects.requireNonNullElse(partner.getVerpflegungskosten(), 0));
        }
        builder.verheiratetKonkubinat(
            List.of(Zivilstand.EINGETRAGENE_PARTNERSCHAFT, Zivilstand.VERHEIRATET, Zivilstand.KONKUBINAT)
                .contains(personInAusbildung.getZivilstand())
        );

        return builder.build();
    }

    private static int getAusbildungskosten(
        final EinnahmenKosten einnahmenKosten,
        final Gesuchsperiode gesuchsperiode,
        final Bildungskategorie bildungskategorie
    ) {
        if (bildungskategorie.isTertiaerstufe()) {
            return Integer.min(
                Objects.requireNonNullElse(einnahmenKosten.getAusbildungskosten(), 0),
                gesuchsperiode.getAusbKostenTertiaer()
            );
        }
        return Integer.min(
            Objects.requireNonNullElse(einnahmenKosten.getAusbildungskosten(), 0),
            gesuchsperiode.getAusbKostenSekII()
        );
    }

    static int getAlterForMedizinischeGrundversorgung(
        final LocalDate geburtsdatum,
        final LocalDate ausbildungsbegin,
        final Gesuchsperiode gesuchsperiode
    ) {
        final int yearOfAusbildungsbegin = ausbildungsbegin.getYear();
        final var stichtag =
            gesuchsperiode.getStichtagVolljaehrigkeitMedizinischeGrundversorgung().withYear(yearOfAusbildungsbegin);
        return DateUtil.getAgeInYearsAtDate(
            geburtsdatum,
            stichtag
        );
    }

    private static boolean getHalbierungElternbeitrag(
        final LocalDate geburtsdatumPia,
        final Set<LebenslaufItem> lebenslaufItemSet,
        final Gesuch gesuch
    ) {
        final var endOfAusbildungsjahr = gesuch.getLatestGesuchTranche().getGueltigkeit().getGueltigBis();
        final var beginOfAusbildungsjahr = gesuch.getEarliestGesuchTranche().getGueltigkeit().getGueltigAb();

        final var abgeschlosseneErstausbildungLebenslaufItem = lebenslaufItemSet.stream()
            .filter(
                lebenslaufItem -> lebenslaufItem.isAusbildung()
                && lebenslaufItem.getAbschluss().isBerufsbefaehigenderAbschluss()
                && lebenslaufItem.isAusbildungAbgeschlossen()
            )
            .findFirst();

        final boolean abgeschlosseneErstausbildung = abgeschlosseneErstausbildungLebenslaufItem.isPresent();

        boolean erstAusbildungWasCompletedBeforeAusbildungsjahr = false;
        if (abgeschlosseneErstausbildung) {
            erstAusbildungWasCompletedBeforeAusbildungsjahr =
                abgeschlosseneErstausbildungLebenslaufItem.get().getVon().isBefore(beginOfAusbildungsjahr);
        }

        var alterAtEndOfAusbildungsjahr =
            DateUtil.getAgeInYearsAtDate(geburtsdatumPia, endOfAusbildungsjahr);

        final boolean halbierungAbgeschlosseneErstausbildung =
            erstAusbildungWasCompletedBeforeAusbildungsjahr
            && (alterAtEndOfAusbildungsjahr >= gesuch.getGesuchsperiode()
                .getLimiteAlterAntragsstellerHalbierungElternbeitrag());
        final var beruftaetigkeiten = Set.of(
            Taetigkeitsart.ERWERBSTAETIGKEIT,
            Taetigkeitsart.BETREUUNG_FAMILIENMITGLIEDER_EIGENER_HAUSHALT
        );
        final var berufstaetigeItems = lebenslaufItemSet.stream()
            .filter(
                lebenslaufItem -> lebenslaufItem.getTaetigkeitsart() != null
            )
            .filter(
                lebenslaufItem -> beruftaetigkeiten.contains(
                    lebenslaufItem.getTaetigkeitsart()
                )
            );
        final int monthsBerufstaetig = berufstaetigeItems
            .mapToInt(lebenslaufItem -> (int) ChronoUnit.DAYS.between(lebenslaufItem.getVon(), lebenslaufItem.getBis()))
            .sum()
        / 30;
        final boolean halbierungBerufstaetig = monthsBerufstaetig >= 72;

        return halbierungAbgeschlosseneErstausbildung || halbierungBerufstaetig;
    }

    public static int getEffektiveWohnkosten(
        final int eingegebeneWohnkosten,
        final Gesuchsperiode gesuchsperiode,
        int anzahlPersonenImHaushalt
    ) {
        int maxWohnkosten = switch (anzahlPersonenImHaushalt) {
            case 0 -> throw new IllegalStateException("0 Personen im Haushalt");
            case 1 -> gesuchsperiode.getWohnkostenPersoenlich1pers();
            case 2 -> gesuchsperiode.getWohnkostenPersoenlich2pers();
            case 3 -> gesuchsperiode.getWohnkostenPersoenlich3pers();
            case 4 -> gesuchsperiode.getWohnkostenPersoenlich4pers();
            default -> gesuchsperiode.getWohnkostenPersoenlich5pluspers();
        };
        return Integer.min(eingegebeneWohnkosten, maxWohnkosten);
    }
}
