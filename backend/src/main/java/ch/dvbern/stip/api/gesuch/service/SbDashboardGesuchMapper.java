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

package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.SbDashboardGesuchDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SbDashboardGesuchMapper {
    public SbDashboardGesuchDto toDto(final Gesuch gesuch, final GesuchTrancheTyp typ) {
        return switch (typ) {
            case TRANCHE -> toDto(gesuch, gesuch.getLatestGesuchTranche());
            case AENDERUNG -> toDto(gesuch, gesuch.getAenderungZuUeberpruefen().orElseThrow());
        };
    }

    SbDashboardGesuchDto toDto(final Gesuch gesuch, final GesuchTranche gesuchTranche) {
        final var target = new SbDashboardGesuchDto();
        target.setId(gesuch.getId());
        target.setGesuchTrancheId(gesuchTranche.getId());
        target.setFallNummer(gesuch.getAusbildung().getFall().getFallNummer());
        target.setTyp(gesuchTranche.getTyp());

        final var pia = gesuchTranche.getGesuchFormular().getPersonInAusbildung();
        if (pia != null) {
            target.setPiaVorname(pia.getVorname());
            target.setPiaNachname(pia.getNachname());
            target.setPiaGeburtsdatum(pia.getGeburtsdatum());
        } else {
            LOG.warn("Gesuch was loaded to SB Dashboard that had no PiA");
        }

        target.setStatus(gesuch.getGesuchStatus());
        final var zuordnung = gesuch.getAusbildung().getFall().getSachbearbeiterZuordnung();
        if (zuordnung != null) {
            target.setBearbeiter(zuordnung.getSachbearbeiter().getFullName());
        }
        target.setLetzteAktivitaet(gesuch.getTimestampMutiert().toLocalDate());

        return target;
    }
}
