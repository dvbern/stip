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

package ch.dvbern.stip.api.delegieren.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.entity.QDelegierung;
import ch.dvbern.stip.api.delegieren.type.DelegierungStatus;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.generated.dto.SozDashboardColumnDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DelegierungRepository implements BaseRepository<Delegierung> {
    private final EntityManager entityManager;
    private static final QDelegierung qDelegierung = QDelegierung.delegierung;

    public record DelegierungEntry(
    String fallNummer,
    UUID fallId,
    String nachname,
    String vorname,
    LocalDate geburtsdatum,
    String wohnort,
    DelegierungStatus status,
    double totalCount,
    UUID id,
    Gesuchstatus gesuchStatus,
    GesuchTrancheStatus aenderungStatus
    ) {
    }

    public List<DelegierungEntry> getFilteredAndOrderedDelegierungEntrys(
        final UUID sozialdienstId,
        final UUID sozialdienstBenutzerId,
        final String fallNummer,
        final String nachname,
        final String vorname,
        final LocalDate geburtsdatum,
        final String ort,
        final DelegierungStatus delegierungStatus,
        final SozDashboardColumnDto sortColumn,
        final SortOrder sortOrder,
        final int page,
        final int pageSize
    ) {
        return getEntityManager()
            .createQuery(
                """
                    select
                        fall.fallNummer,
                        fall.id as fallId,
                        delegierung.persoenlicheAngaben.nachname,
                        delegierung.persoenlicheAngaben.vorname,
                        delegierung.persoenlicheAngaben.geburtsdatum,
                        delegierung.persoenlicheAngaben.adresse.ort,
                        delegierung.status,
                        count(*) over() as totalCount,
                        delegierung.id,
                        latestGesuchStatus.gesuchStatus as gesuchstatus,
                        latestAenderungStatus.aenderungStatus as aenderungStatus
                    from Delegierung as delegierung
                    join delegierung.sozialdienst as sozialdienst
                    join delegierung.fall as fall
                    left join lateral (
                        select gesuch.gesuchStatus as gesuchStatus
                        from Ausbildung as ausbildung
                        left join ausbildung.gesuchs as gesuch
                        where ausbildung.fall.id = fall.id
                        order by gesuch.timestampMutiert desc
                        limit 1
                    ) as latestGesuchStatus
                    left join lateral (
                        select aenderung.status as aenderungStatus
                        from Ausbildung as ausbildung
                        left join ausbildung.gesuchs as gesuch
                        left join gesuch.gesuchTranchen as aenderung on (
                            aenderung.typ = :trancheTypAenderung
                            and aenderung.status in (:editableTrancheStatus)
                        )
                        where ausbildung.fall.id = fall.id
                        order by aenderung.timestampMutiert desc
                        limit 1
                    ) as latestAenderungStatus
                    where
                        sozialdienst.id = :sozialdienstId
                        and (:sozialdienstBenutzerId is null or delegierung.delegierterMitarbeiter.id = :sozialdienstBenutzerId)
                        and (:fallNummer is null or fall.fallNummer ilike :fallNummer)
                        and (:nachname is null or delegierung.persoenlicheAngaben.nachname ilike :nachname)
                        and (:vorname is null or delegierung.persoenlicheAngaben.vorname ilike :vorname)
                        and (cast(:geburtsdatum as date) is null or delegierung.persoenlicheAngaben.geburtsdatum = :geburtsdatum)
                        and (:ort is null or delegierung.persoenlicheAngaben.adresse.ort ilike :ort)
                        and (:delegierungStatus is null or delegierung.status = : delegierungStatus)
                    order by
                        case
                            when :sortColumn = 'FALLNUMMER' then fall.fallNummer
                            when :sortColumn = 'NACHNAME' then delegierung.persoenlicheAngaben.nachname
                            when :sortColumn = 'VORNAME' then delegierung.persoenlicheAngaben.vorname
                            when :sortColumn = 'GEBURTSDATUM' then to_char(delegierung.persoenlicheAngaben.geburtsdatum, 'yyyy-mm-dd')
                            when :sortColumn = 'WOHNORT' then delegierung.persoenlicheAngaben.adresse.ort
                        end %s,
                        delegierung.timestampMutiert desc
                    limit :pageSize offset :page
                """.formatted(
                    getSortOrder(sortOrder)
                ),
                DelegierungEntry.class
            )
            .setParameter("sozialdienstId", sozialdienstId)
            .setParameter("sozialdienstBenutzerId", sozialdienstBenutzerId)
            .setParameter("trancheTypAenderung", GesuchTrancheTyp.AENDERUNG)
            .setParameter("editableTrancheStatus", GesuchTrancheStatus.GESUCHSTELLER_CAN_MODIFY_DOKUMENT)
            .setParameter("fallNummer", asFuzzy(fallNummer))
            .setParameter("nachname", asFuzzy(nachname))
            .setParameter("vorname", asFuzzy(vorname))
            .setParameter("geburtsdatum", geburtsdatum)
            .setParameter("ort", asFuzzy(ort))
            .setParameter("delegierungStatus", delegierungStatus)
            .setParameter("pageSize", pageSize)
            .setParameter("page", page * pageSize)
            .setParameter("sortColumn", getSortColumn(sortColumn))
            .getResultList();
    }

    public long reassignAllOfSozialdienstBenutzerTo(
        final SozialdienstBenutzer sozialdienstBenutzerFrom,
        final SozialdienstBenutzer sozialdienstBenutzerTo
    ) {
        return new JPAQueryFactory(entityManager).update(qDelegierung)
            .set(qDelegierung.delegierterMitarbeiter, sozialdienstBenutzerTo)
            .where(qDelegierung.delegierterMitarbeiter.eq(sozialdienstBenutzerFrom))
            .execute();
    }

    private String getSortOrder(final SortOrder sortOrder) {
        if (sortOrder == null) {
            return "desc";
        }
        return switch (sortOrder) {
            case ASCENDING -> "asc";
            case DESCENDING -> "desc";
        };
    }

    private String getSortColumn(SozDashboardColumnDto sortColumn) {
        if (Objects.isNull(sortColumn)) {
            return null;
        }
        return sortColumn.toString();
    }

    private String asFuzzy(final String value) {
        if (Objects.isNull(value)) {
            return null;
        }

        return "%%%s%%".formatted(value);
    }
}
