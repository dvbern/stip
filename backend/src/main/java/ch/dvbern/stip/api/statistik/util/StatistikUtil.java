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

package ch.dvbern.stip.api.statistik.util;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.common.util.KantonUtil;
import ch.dvbern.stip.api.darlehen.entity.DarlehenBuchhaltungEntry;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.Statisticsdata;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.statistik.type.StatistikBuchhaltungType;
import ch.dvbern.stip.api.statistik.type.StatistikBuchhaltungUnion;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeData;
import ch.dvbern.stip.integration.gemeindelookup.domain.model.GemeindeLookupRequest;
import ch.dvbern.stip.integration.gemeindelookup.domain.port.GemeindeLookupPortFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class StatistikUtil {
    public static int booleanToBfsCode(final boolean value) {
        return value ? 2 : 1;
    }

    public int getBfsCodeFromTenantIdentifier(final TenantIdentifier tenantIdentifier) {
        return KantonUtil.getByTenantIdentifier(tenantIdentifier).getBfsCode();
    }

    public static GesuchTranche getLatestGesuchTrancheFromFallByYear(final Fall fall, final int year) {
        final var trancheStream = fall.getAusbildungs()
            .stream()
            .flatMap(ausbildung -> ausbildung.getGesuchs().stream())
            .flatMap(gesuch -> gesuch.getGesuchTranchen().stream());

        return getLatestGesuchTrancheByYear(trancheStream, year);
    }

    public static GesuchTranche getLatestGesuchTrancheByYear(
        final Stream<GesuchTranche> gesuchTrancheStream,
        final int year
    ) {
        return gesuchTrancheStream
            .filter(tranche -> {
                final var gueltigkeit = tranche.getGueltigkeit();
                final var gueltigAb = gueltigkeit.getGueltigAb();
                final var gueltigBis = gueltigkeit.getGueltigBis();
                return (gueltigAb != null && gueltigAb.getYear() == year)
                || (gueltigBis != null && gueltigBis.getYear() == year);
            })
            .max(Comparator.comparing(tranche -> tranche.getGueltigkeit().getGueltigBis()))
            .orElse(null);
    }

    public static Integer getBfsGemeindeNrFromGesuch(
        final GesuchTranche gesuchTranche,
        final TenantIdentifier tenantIdentifier,
        final GemeindeLookupPortFactory gemeindeLookupPortFactory
    ) {
        final var gesuch = gesuchTranche.getGesuch();
        final var statisticsdata = Optional.ofNullable(gesuch.getStatisticsdata());

        if (statisticsdata.isEmpty()) {
            final var address = gesuchTranche.getGesuchFormular().getPersonInAusbildung().getAdresse();
            final var gemeindeLookupRequest = GemeindeLookupRequest.builder()
                .gesuchId(gesuch.getId())
                .tenantIdentifier(tenantIdentifier)
                .strasse(address.getStrasse())
                .hausnummer(address.getHausnummer())
                .plz(address.getPlz())
                .ort(address.getOrt())
                .build();

            return gemeindeLookupPortFactory.getGemeindeLookupAdapter()
                .findGemeindeData(gemeindeLookupRequest)
                .map(GemeindeData::bfsNummer)
                .orElse(null);
        }

        return statisticsdata
            .map(Statisticsdata::getGemeindeBfsNr)
            .orElse(null);
    }

    public static boolean isFirstAusbildung(final GesuchTranche gesuchTranche) {
        final var lebenslaufItems = gesuchTranche.getGesuchFormular().getLebenslaufItems();

        return lebenslaufItems.stream()
            .filter(LebenslaufItem::isAusbildung)
            .anyMatch(LebenslaufItem::isAusbildungAbgeschlossen);
    }

    public static List<StatistikBuchhaltungUnion> combineBuchhaltungs(
        final List<Buchhaltung> buchhaltungs,
        final List<DarlehenBuchhaltungEntry> darlehenBuchhaltungs,
        int year
    ) {
        final var stipendiumStream = buchhaltungs.stream()
            .map(
                buchhaltung -> StatistikBuchhaltungUnion.builder()
                    .gesuch(buchhaltung.getGesuch())
                    .type(StatistikBuchhaltungType.STIPENDIUM)
                    .betrag(buchhaltung.getBetrag())
                    .anzahlSemester(getSemesterCount(buchhaltung.getGesuch(), year))
                    .build()
            );

        final var darlehenStream = darlehenBuchhaltungs.stream()
            .map(
                darlehenBuchhaltung -> StatistikBuchhaltungUnion.builder()
                    .gesuch(darlehenBuchhaltung.getGesuch())
                    .type(StatistikBuchhaltungType.DARLEHEN)
                    .betrag(darlehenBuchhaltung.getBetrag())
                    .anzahlSemester(getSemesterCount(darlehenBuchhaltung.getGesuch(), year))
                    .build()
            );

        return Stream.concat(stipendiumStream, darlehenStream)
            .toList();
    }

    public static Integer getSemesterCount(final Gesuch gesuch, int year) {
        final var ausbildung = gesuch.getAusbildung();
        final LocalDate begin = ausbildung.getAusbildungBegin();
        final LocalDate end = ausbildung.getAusbildungEnd();

        if (begin.getYear() == year) {
            if (DateUtil.isFruehling(begin)) {
                return 2;
            }
            if (DateUtil.isHerbst(begin)) {
                return 1;
            }
            return null;
        }

        if (end.getYear() == year) {
            if (DateUtil.isFruehling(end)) {
                return 1;
            }
            if (DateUtil.isHerbst(end)) {
                return 2;
            }
            return null;
        }

        return 2;
    }
}
