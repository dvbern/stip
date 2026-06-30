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

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.statistik.entity.Statistik;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class StatistikRepository implements BaseRepository<Statistik> {

    public record StatistikOfYear(
    /*
     * While projecting, the error handling can be very troublesome.
     * There are no indications which fields are involved if the values cannot be mapped.
     *
     * My best working approach so far is:
     * 1. Delete Half of the properties
     * 2. Check if it works
     * 3. Add half of the deleted ones again
     * 4. Check if it works
     * 5. Repeat
     */
    // spotless:off
        int year,
        UUID gesuchId,
        /* PersDto */
        String sozialversicherungsnummer,
        Anrede anrede,
        LocalDate geburtsdatum,
        String nationalitaetBfs,
        Niederlassungsstatus niederlassungsstatus,
        Integer gemeindeBfsNr,
        Adresse piaAdresse,
        Land piaAdresseLand,
        /* FormDto */
        UUID ausbildungId,
        boolean isAusbildungAusland,
        String ausbildungLandBfs,
        String ausbildungPlz,
        /* FormationDto */
        Integer bfsKategorie,
        boolean besuchtBMS,
        Integer bfsStudienStufe,
        AusbildungsPensum ausbildungspensum,
        boolean isFirstAusbildung,
        /* InstIdentificationRootDto */
        String ausbildungsstaetteNameDe,
        AusbildungsstaetteNummerTyp ausbildungsstaetteNummerTyp,
        String ausbildungsstaetteNummer,
        /* SumDto */
        String buchhaltungTyp,
        LocalDate ausbildungBegin,
        LocalDate ausbildungEnd,
        Long sumId,
        Integer sumTotal
        // spotless:on
    ) {
    }

    @WithSpan
    public List<StatistikOfYear> getStatistikValuesFor(final int year) {
        return getEntityManager().createQuery("""
            /* Union the Buchhaltungen from the Gesuch and the Darlehen sharing:
             - gesuchId
             - betrag
             - typ
             */
            with buchhaltungUnion as (
                select distinct buchhaltungGesuch.gesuch.id as gesuchId, betrag as betrag, 'STIPENDIUM' as typ
                from Buchhaltung buchhaltungGesuch
                join SapDelivery sapDelivery on (
                    sapDelivery.buchhaltung = buchhaltungGesuch
                        and sapDelivery.sapStatus = :sapStatus
                )
                where
                    year(buchhaltungGesuch.timestampErstellt) = :year
                    and buchhaltungGesuch.buchhaltungType in (:auszahlungBuchhaltungTypes)
                group by buchhaltungGesuch.gesuch.id, buchhaltungGesuch.id, betrag
                having count(sapDelivery.id) > 0

                union all

                select gesuch.id as gesuchId, betrag as betrag, 'DARLEHEN' as typ
                from DarlehenBuchhaltungEntry buchhaltungDarlehen
                where
                    year(buchhaltungDarlehen.timestampErstellt) = :year
                    and buchhaltungDarlehen.kategorie in (:darlehenBuchhaltungType)
            )
            select
                :year,
                gesuch.id,
                /* PersDto */
                pia.sozialversicherungsnummer,
                pia.anrede,
                pia.geburtsdatum,
                piaNationalitaet.laendercodeBfs,
                pia.niederlassungsstatus,
                statisticsdata.gemeindeBfsNr,
                piaAdresse,
                piaAdresseLand,
                /* FormDto */
                ausbildung.id,
                ausbildung.isAusbildungAusland,
                ausbildungLand.laendercodeBfs,
                ausbildungPlz.kantonskuerzel,
                /* FormationDto */
                abschluss.bfsKategorie,
                ausbildung.besuchtBMS,
                abschluss.bfsStudienStufe,
                ausbildung.pensum,
                count(lebenslaufAbschluss) = 0,
                /* InstIdentificationRootDto */
                ausbildungsstaette.nameDe,
                ausbildungsstaette.nummerTyp,
                ausbildungsstaette.nummer,
                /* SumDto */
                buchhaltungUnion.typ,
                ausbildung.ausbildungBegin,
                ausbildung.ausbildungEnd,
                row_number() over (order by pia.id),
                buchhaltungUnion.betrag
            from buchhaltungUnion buchhaltungUnion
            join Gesuch gesuch on (buchhaltungUnion.gesuchId = gesuch.id)

            /*
            LATERAL Joins help to boost performance by allowing the subqueried data to be related to the FROM data
            Something like a correlated subquery: https://en.wikipedia.org/wiki/Correlated_subquery

            https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-LATERAL
            */
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
            left join gesuchFormular.lebenslaufItems lebenslaufItems on (
                lebenslaufItems.abschluss is not null
            )
            left join lebenslaufItems.abschluss lebenslaufAbschluss on (
                lebenslaufAbschluss.berufsbefaehigenderAbschluss = true
            )
            left join gesuch.statisticsdata statisticsdata
            join gesuch.ausbildung ausbildung
            join ausbildung.ausbildungsgang ausbildungsgang
            join ausbildungsgang.abschluss abschluss
            join ausbildungsgang.ausbildungsstaette ausbildungsstaette
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
                ausbildungsstaette.id,
                latestGesuchTranche.gueltigkeit.gueltigBis
            order by latestGesuchTranche.gueltigkeit.gueltigBis
        """, StatistikOfYear.class)
            .setParameter("year", year)
            .setParameter("trancheTypTranche", GesuchTrancheTyp.TRANCHE)
            .setParameter("auszahlungBuchhaltungTypes", BuchhaltungType.AUSZAHLUNGS)
            .setParameter("darlehenBuchhaltungType", DarlehenBuchhaltungEntryKategorie.DARLEHEN)
            .setParameter("sapStatus", SapStatus.SUCCESS)
            .getResultList();
    }
}
