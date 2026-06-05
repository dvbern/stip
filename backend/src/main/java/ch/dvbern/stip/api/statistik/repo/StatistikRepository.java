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

package ch.dvbern.stip.api.statistik.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.statistik.entity.Statistik;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class StatistikRepository implements BaseRepository<Statistik> {

    public record TestTable(
    UUID gesuchId,
    /* PersDto */
    String svnNummer,
    Anrede anrede,
    LocalDate geburtsdatum,
    String nationalitaetBfs,
    Integer gemeindeBfsNr,
    String piaAdresseLandBfs,
    /* FormDto */
    boolean isAusbildungAusland,
    String ausbildungLandBfs,
    String ausbildungKanton,
    /* FormationDto */
    /* why opt? */ Integer bfsKategorie,
    boolean besuchtBMS,
    /* why opt? */ Integer bfsStudienStufe,
    AusbildungsPensum ausbildungspensum,
    boolean isFirstAusbildung,
    /* InstIdentificationRootDto */
    AusbildungsstaetteNummerTyp ausbildungsstaetteNummerTyp,
    /* SumDto */
    Object buchhaltungTyp,
    Object sumId,
    Integer sumTotal
    ) {
    }

    public List<TestTable> getTestList(final int year) {
        return getEntityManager().createQuery("""
            with buchhaltungUnion as (
                select gesuch.id as gesuchId, betrag as betrag, 'gesuch' as typ
                from Buchhaltung buchhaltungGesuch
                where
                    year(buchhaltungGesuch.timestampErstellt) = :year
                    and buchhaltungGesuch.buchhaltungType in (:auszahlungBuchhaltungTypes)
                    and buchhaltungGesuch is not null
                union all
                select gesuch.id as gesuchId, betrag as betrag, 'darlehen' as typ
                from DarlehenBuchhaltungEntry buchhaltungDarlehen
                where
                    year(buchhaltungDarlehen.timestampErstellt) = :year
                    and buchhaltungDarlehen is not null
            )
            select
                gesuch.id,
                /* PersDto */
                pia.sozialversicherungsnummer,
                pia.anrede,
                pia.geburtsdatum,
                piaNationalitaet.laendercodeBfs,
                statisticsdata.gemeindeBfsNr,
                piaAdresseLand.laendercodeBfs,
                /* FormDto */
                ausbildung.isAusbildungAusland,
                ausbildungLand.laendercodeBfs,
                ausbildungPlz.kantonskuerzel,
                /* FormationDto */
                abschluss.bfsKategorie,
                ausbildung.besuchtBMS,
                abschluss.bfsStudienStufe,
                ausbildung.pensum,
                count(abgeschlosseneAusbildungen) = 0,
                /* InstIdentificationRootDto */
                ausbildungsstatte.nummerTyp,
                /* SumDto */
                buchhaltungUnion.typ,
                row_number() over (order by pia.id),
                buchhaltungUnion.betrag
            from buchhaltungUnion buchhaltungUnion
            join Gesuch gesuch on (buchhaltungUnion.gesuchId = gesuch.id)
            join lateral (
                select
                    gesuchTranche.gesuchFormular as gesuchFormular,
                    gesuchTranche.gueltigkeit as gueltigkeit,
                    gesuchTranche.timestampErstellt as timestampErstellt
                from GesuchTranche gesuchTranche
                where gesuchTranche.gesuch = gesuch
                    and gesuchTranche.typ = :trancheTypTranche
                    and (
                        year(gesuchTranche.gueltigkeit.gueltigAb) = :year
                        or year(gesuchTranche.gueltigkeit.gueltigBis) = :year
                    )
                order by gesuchTranche.gueltigkeit.gueltigBis
                limit 1
            ) latestGesuchTranche
            join latestGesuchTranche.gesuchFormular gesuchFormular
            join gesuchFormular.personInAusbildung pia
            join pia.adresse piaAdresse
            join piaAdresse.land piaAdresseLand
            join pia.nationalitaet piaNationalitaet
            left join gesuchFormular.lebenslaufItems abgeschlosseneAusbildungen on (
                abgeschlosseneAusbildungen.ausbildungAbgeschlossen
            )
            left join gesuch.statisticsdata statisticsdata
            join gesuch.ausbildung ausbildung
            join ausbildung.ausbildungsgang ausbildungsgang
            join ausbildungsgang.abschluss abschluss
            join ausbildungsgang.ausbildungsstaette ausbildungsstatte
            left join ausbildung.land ausbildungLand
            left join Plz ausbildungPlz on (ausbildungPlz.plz = ausbildung.ausbildungsortPLZ)
            group by
                buchhaltungUnion.gesuchId,
                buchhaltungUnion.typ,
                buchhaltungUnion.betrag,
                gesuch.id,
                pia.id,
                piaAdresse.id,
                piaNationalitaet.id,
                statisticsdata.id,
                piaAdresseLand.id,
                ausbildung.id,
                ausbildungLand.id,
                ausbildungPlz.id,
                abschluss.id,
                ausbildung.id,
                abschluss.id,
                ausbildung.id,
                ausbildungsstatte.id,
                latestGesuchTranche.gueltigkeit.gueltigBis
            order by latestGesuchTranche.gueltigkeit.gueltigBis
        """, TestTable.class)
            .setParameter("year", year)
            .setParameter("trancheTypTranche", GesuchTrancheTyp.TRANCHE)
            .setParameter("auszahlungBuchhaltungTypes", BuchhaltungType.AUSZAHLUNGS)
            .getResultList();
    }

    public List<TestTable> getTestListCopy(final int year) {
        return getEntityManager().createQuery("""
            select
                gesuch.id,
                max(gesuchTranche.timestampErstellt),
                /* PersDto */
                pia.sozialversicherungsnummer,
                pia.anrede,
                pia.geburtsdatum,
                piaNationalitaet.laendercodeBfs,
                statisticsdata.gemeindeBfsNr,
                piaAdresseLand.laendercodeBfs,
                /* FormDto */
                ausbildung.isAusbildungAusland,
                ausbildungLand.laendercodeBfs,
                ausbildungPlz.kantonskuerzel,
                /* FormationDto */
                abschluss.bfsKategorie,
                ausbildung.besuchtBMS,
                abschluss.bfsStudienStufe,
                ausbildung.pensum,
                count(abgeschlosseneAusbildungen) = 0,
                /* InstIdentificationRootDto */
                ausbildungsstatte.nummerTyp,
                /* SumDto */
                /* row_number() over (order by pia.id) */
                buchhaltungGesuch.id,
                buchhaltungGesuch.betrag,
                buchhaltungDarlehen.betrag
            from PersonInAusbildung pia
            join pia.adresse piaAdresse
            join piaAdresse.land piaAdresseLand
            join pia.nationalitaet piaNationalitaet
            join GesuchFormular gesuchFormular on (gesuchFormular.personInAusbildung = pia)
            join gesuchFormular.lebenslaufItems abgeschlosseneAusbildungen on (
                abgeschlosseneAusbildungen.ausbildungAbgeschlossen
            )
            /*join GesuchTranche gesuchTranche on (
                gesuchTranche.gesuchFormular = gesuchFormular
                    and (
                        year(gesuchTranche.gueltigkeit.gueltigAb) = :year
                        or year(gesuchTranche.gueltigkeit.gueltigBis) = :year
                    )
            )*/
            join lateral (
                select latestTranche
                from GesuchTranche latestTranche
                where latestTranche.gesuchFormular = gesuchFormular
            ) as gesuchTranche
            join Gesuch gesuch on (gesuchTranche.gesuch = gesuch and gesuchTranche.typ = :trancheTypTranche)
            left join gesuch.statisticsdata statisticsdata
            join gesuch.ausbildung ausbildung
            join ausbildung.ausbildungsgang ausbildungsgang
            join ausbildungsgang.abschluss abschluss
            join ausbildungsgang.ausbildungsstaette ausbildungsstatte
            left join ausbildung.land ausbildungLand
            left join Plz ausbildungPlz on (ausbildungPlz.plz = ausbildung.ausbildungsortPLZ)
            left join Buchhaltung buchhaltungGesuch on (
                buchhaltungGesuch.gesuch = gesuch
                and year(buchhaltungGesuch.timestampErstellt) = :year
                and buchhaltungGesuch.buchhaltungType in (:auszahlungBuchhaltungTypes)
            )
            left join DarlehenBuchhaltungEntry buchhaltungDarlehen on (
                buchhaltungDarlehen.gesuch = gesuch
                and year(buchhaltungDarlehen.timestampErstellt) = :year
            )
            where buchhaltungGesuch is not null or buchhaltungDarlehen is not null
            group by
                buchhaltungGesuch.id,
                buchhaltungDarlehen.id,
                gesuch.id,
                pia.id,
                piaAdresse.id,
                piaNationalitaet.id,
                statisticsdata.id,
                piaAdresseLand.id,
                ausbildung.id,
                ausbildungLand.id,
                ausbildungPlz.id,
                abschluss.id,
                ausbildung.id,
                abschluss.id,
                ausbildung.id,
                ausbildungsstatte.id
            order by max(gesuchTranche.gueltigkeit.gueltigBis)
        """, TestTable.class)
            .setParameter("year", year)
            .setParameter("trancheTypTranche", GesuchTrancheTyp.TRANCHE)
            .setParameter("auszahlungBuchhaltungTypes", BuchhaltungType.AUSZAHLUNGS)
            .getResultList();
    }
}
