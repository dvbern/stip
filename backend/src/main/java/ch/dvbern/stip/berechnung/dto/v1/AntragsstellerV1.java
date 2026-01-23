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
import ch.dvbern.stip.berechnung.dto.PersonValueList;
import ch.dvbern.stip.generated.dto.PersonValueItemDto;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.berechnung.dto.InputUtils.toJahresWert;

@Data
@Builder
@Jacksonized
@Value
@Slf4j
public class AntragsstellerV1 {
    String vorname;
    String nachname;
    String vornamePartner;
    String nachnamePartner;
    String sozialversicherungsnummer;
    LocalDate geburtsdatum;
    int alter;
    int piaWohntInElternHaushalt;
    int anzahlPersonenImHaushalt;
    int anteilFamilienbudget;
    boolean verheiratetKonkubinat;
    boolean tertiaerstufe;
    boolean lehre;
    boolean eigenerHaushalt;
    boolean halbierungElternbeitrag;

    // Einkommen
    List<PersonValueItemDto> einkommens;
    List<PersonValueItemDto> einnahmenBGSAs;
    List<PersonValueItemDto> kinderAusbildungszulagens;
    List<PersonValueItemDto> unterhaltsbeitraeges;
    List<PersonValueItemDto> eoLeistungens;
    List<PersonValueItemDto> taggelds;
    List<PersonValueItemDto> rentens;
    List<PersonValueItemDto> ergaenzungsleistungens;
    Integer gemeindeInstitutionen;
    List<PersonValueItemDto> andereEinnahmens;
    Integer vermoegen;

    // Kosten
    int ausbildungskosten;
    int fahrkosten;
    int auswaertigeMittagessenProWoche;
    int grundbedarf;
    int wohnkosten;
    List<PersonValueItemDto> medizinischeGrundversorgungs;
    Integer fahrkostenPartner;
    Integer verpflegungskostenPartner;
    int fremdbetreuung;
    int steuern;
    Integer steuernPartner;

    public static AntragsstellerV1 buildFromDependants(
        final GesuchFormular gesuchFormular,
        final int piaWohntInElternHaushalt
    ) {
        final var personInAusbildung = gesuchFormular.getPersonInAusbildung();
        final var piaName = personInAusbildung.getVorname();
        final var partner = gesuchFormular.getPartner();
        final var einnahmenKosten = gesuchFormular.getEinnahmenKosten();
        final var gesuchsperiode = gesuchFormular.getTranche().getGesuch().getGesuchsperiode();
        final var ausbildung = gesuchFormular.getAusbildung();

        final var einkommens = new PersonValueList();
        final var medizinischeGrundversorgungs = new PersonValueList();
        final var kinderAusbildungszulagens = new PersonValueList();
        final var unterhaltsbeitraeges = new PersonValueList();
        final var rentens = new PersonValueList();
        final var eoLeistungens = new PersonValueList();
        final var taggelds = new PersonValueList();
        final var ergaenzungsleistungens = new PersonValueList();
        final var andereEinnahmens = new PersonValueList();
        final var einnahmenBGSAs = new PersonValueList();

        int alter = DateUtil.getAgeInYears(personInAusbildung.getGeburtsdatum());
        final AntragsstellerV1Builder builder = new AntragsstellerV1Builder();
        builder
            .vorname(personInAusbildung.getVorname())
            .nachname(personInAusbildung.getNachname())
            .sozialversicherungsnummer(personInAusbildung.getSozialversicherungsnummer())
            .geburtsdatum(personInAusbildung.getGeburtsdatum())
            .alter(alter)
            .piaWohntInElternHaushalt(piaWohntInElternHaushalt)
            .tertiaerstufe(
                ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie().isTertiaerstufe()
            )
            .vermoegen(einnahmenKosten.getVermoegen())
            .gemeindeInstitutionen(einnahmenKosten.getBeitraege());

        var nettoerwerbseinkommen = einnahmenKosten.getNettoerwerbseinkommen();

        if (ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie().isTertiaerstufe()) {
            nettoerwerbseinkommen = Math.max(nettoerwerbseinkommen - gesuchsperiode.getEinkommensfreibetrag(), 0);
        }

        einkommens.setPersonValue(piaName, nettoerwerbseinkommen);
        kinderAusbildungszulagens.setPersonValue(piaName, einnahmenKosten.getZulagen());
        andereEinnahmens.setPersonValue(piaName, einnahmenKosten.getAndereEinnahmen());
        taggelds.setPersonValue(piaName, einnahmenKosten.getTaggelderAHVIV());
        unterhaltsbeitraeges.setPersonValue(piaName, toJahresWert(einnahmenKosten.getUnterhaltsbeitraege()));
        rentens.setPersonValue(piaName, einnahmenKosten.getRenten());
        ergaenzungsleistungens.setPersonValue(piaName, einnahmenKosten.getErgaenzungsleistungen());
        eoLeistungens.setPersonValue(piaName, einnahmenKosten.getEoLeistungen());
        einnahmenBGSAs.setPersonValue(piaName, einnahmenKosten.getEinnahmenBGSA());

        int anzahlPersonenImHaushalt = 0;
        if (personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT) {
            anzahlPersonenImHaushalt = 1;
            medizinischeGrundversorgungs.setPersonValue(
                piaName,
                BerechnungRequestV1
                    .getMedizinischeGrundversorgung(
                        personInAusbildung.getGeburtsdatum(),
                        ausbildung.getAusbildungBegin(),
                        gesuchsperiode
                    )
            );

            if (partner != null) {
                anzahlPersonenImHaushalt += 1;
            }
            for (final var kind : gesuchFormular.getKinds()) {
                if (kind.getWohnsitzAnteilPia() > 0) {
                    anzahlPersonenImHaushalt += 1;
                }
            }

            final var isWgWohnend = Boolean.TRUE.equals(einnahmenKosten.getWgWohnend());
            final var isAlternativeWgWohnend = Boolean.TRUE.equals(einnahmenKosten.getAlternativeWohnformWohnend());
            builder.grundbedarf(
                BerechnungRequestV1.getGrundbedarf(
                    gesuchsperiode,
                    isAlternativeWgWohnend ? 1 : anzahlPersonenImHaushalt,
                    isWgWohnend || isAlternativeWgWohnend
                )
            );

        } else {
            builder.grundbedarf(0);
        }

        builder.wohnkosten(0);
        if (einnahmenKosten.getWohnkosten() != null && anzahlPersonenImHaushalt > 0) {
            builder.wohnkosten(
                getEffektiveWohnkosten(
                    toJahresWert(einnahmenKosten.getWohnkosten()),
                    gesuchsperiode,
                    anzahlPersonenImHaushalt
                )
            );
        }

        builder.ausbildungskosten(
            getAusbildungskosten(
                einnahmenKosten,
                gesuchsperiode,
                ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie()
            )
        );
        final var isPiaQuellenbesteuert = EinnahmenKostenMappingUtil.isQuellenBesteuert(personInAusbildung);
        builder.steuern(
            EinnahmenKostenMappingUtil.calculateSteuern(einnahmenKosten, isPiaQuellenbesteuert)
        );
        builder.fahrkosten(Objects.requireNonNullElse(einnahmenKosten.getFahrkosten(), 0));
        builder.auswaertigeMittagessenProWoche(
            Objects.requireNonNullElse(einnahmenKosten.getAuswaertigeMittagessenProWoche(), 0)
        );
        builder.fremdbetreuung(Objects.requireNonNullElse(einnahmenKosten.getBetreuungskostenKinder(), 0));
        final var abschluss = gesuchFormular.getAusbildung().getAusbildungsgang().getAbschluss();

        final boolean isLehre = abschluss.getBildungsrichtung()
            .equals(
                Bildungsrichtung.BERUFLICHE_GRUNDBILDUNG
            );
        builder.lehre(isLehre);
        builder.eigenerHaushalt(personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT);

        builder.halbierungElternbeitrag(
            getHalbierungElternbeitrag(
                personInAusbildung.getGeburtsdatum(),
                gesuchFormular.getLebenslaufItems(),
                gesuchFormular.getTranche().getGesuch()
            )
        );

        final var ekPartner = gesuchFormular.getEinnahmenKostenPartner();
        if (Objects.nonNull(partner) && Objects.nonNull(ekPartner)) {
            final var partnerName = partner.getVorname();
            builder.vornamePartner(partnerName);
            builder.nachnamePartner(partner.getNachname());
            kinderAusbildungszulagens.setPartnerValue(partnerName, ekPartner.getZulagen());
            andereEinnahmens.setPartnerValue(partnerName, ekPartner.getAndereEinnahmen());
            taggelds.setPartnerValue(partnerName, ekPartner.getTaggelderAHVIV());
            einkommens.setPartnerValue(partnerName, ekPartner.getNettoerwerbseinkommen());
            unterhaltsbeitraeges.setPartnerValue(partnerName, toJahresWert(ekPartner.getUnterhaltsbeitraege()));
            builder.steuernPartner(
                // Not required according to https://support.dvbern.ch/browse/ATSTIP-559?focusedId=320460
                EinnahmenKostenMappingUtil.calculateSteuern(ekPartner, false)
            );
            builder.fahrkostenPartner(ekPartner.getFahrkosten());
            builder.verpflegungskostenPartner(ekPartner.getVerpflegungskosten());
            rentens.setPartnerValue(partnerName, ekPartner.getRenten());
            ergaenzungsleistungens.setPartnerValue(partnerName, ekPartner.getErgaenzungsleistungen());
            eoLeistungens.setPartnerValue(partnerName, ekPartner.getEoLeistungen());
            einnahmenBGSAs.setPartnerValue(partnerName, ekPartner.getEinnahmenBGSA());
            if (personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT) {
                medizinischeGrundversorgungs.setPartnerValue(
                    partner.getVorname(),
                    BerechnungRequestV1.getMedizinischeGrundversorgung(
                        partner.getGeburtsdatum(),
                        ausbildung.getAusbildungBegin(),
                        gesuchsperiode
                    )
                );
            }
        }

        for (var kind : gesuchFormular.getKinds()) {
            if (kind.getWohnsitzAnteilPia() > 0) {
                kinderAusbildungszulagens.addKindValue(kind, toJahresWert(kind.getKinderUndAusbildungszulagen()));
                unterhaltsbeitraeges.addKindValue(kind, toJahresWert(kind.getUnterhaltsbeitraege()));
                rentens.addKindValue(kind, kind.getRenten());
                ergaenzungsleistungens.addKindValue(kind, kind.getErgaenzungsleistungen());
                andereEinnahmens.addKindValue(kind, kind.getAndereEinnahmen());
                if (personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT) {
                    medizinischeGrundversorgungs.addKindValue(
                        kind,
                        BerechnungRequestV1.getMedizinischeGrundversorgung(
                            kind.getGeburtsdatum(),
                            ausbildung.getAusbildungBegin(),
                            gesuchsperiode
                        )
                    );
                }
            }
        }

        builder.verheiratetKonkubinat(
            List.of(Zivilstand.EINGETRAGENE_PARTNERSCHAFT, Zivilstand.VERHEIRATET, Zivilstand.KONKUBINAT)
                .contains(personInAusbildung.getZivilstand())
        );

        builder.anzahlPersonenImHaushalt(anzahlPersonenImHaushalt);

        builder.einkommens = einkommens.toList();
        builder.medizinischeGrundversorgungs = medizinischeGrundversorgungs.toList();
        builder.kinderAusbildungszulagens = kinderAusbildungszulagens.toList();
        builder.unterhaltsbeitraeges = unterhaltsbeitraeges.toList();
        builder.eoLeistungens = eoLeistungens.toList();
        builder.taggelds = taggelds.toList();
        builder.rentens = rentens.toList();
        builder.ergaenzungsleistungens = ergaenzungsleistungens.toList();
        builder.andereEinnahmens = andereEinnahmens.toList();
        builder.einnahmenBGSAs = einnahmenBGSAs.toList();

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
                && Objects.nonNull(lebenslaufItem.getAbschluss())
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
