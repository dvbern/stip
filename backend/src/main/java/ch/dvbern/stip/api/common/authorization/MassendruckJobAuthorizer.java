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

package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.massendruck.entity.MassendruckJob;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobRepository;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Authorizer
public class MassendruckJobAuthorizer extends BaseAuthorizer {
    private final MassendruckJobRepository massendruckJobRepository;

    public void canCreateMassendruckJob(final GetGesucheSBQueryType getGesucheSBQueryType) {
        final var isAllowed = switch (getGesucheSBQueryType) {
            case ALLE_DRUCKBAR_VERFUEGUNGEN, MEINE_DRUCKBAR_VERFUEGUNGEN, ALLE_DRUCKBAR_DATENSCHUTZBRIEFE, MEINE_DRUCKBAR_DATENSCHUTZBRIEFE -> true;
            default -> false;
        };

        if (!isAllowed) {
            forbidden();
        }
    }

    public void canDeleteMassendruckJob(final UUID massendruckId) {
        final var massendruckJob = massendruckJobRepository.requireById(massendruckId);
        assertNotFailed(massendruckJob);
    }

    public void canRetryMassendruckJob(final UUID massendruckId) {
        final var massendruckJob = massendruckJobRepository.requireById(massendruckId);
        assertNotFailed(massendruckJob);
    }

    public void permitAll() {
        super.permitAll();
    }

    private void assertNotFailed(final MassendruckJob massendruckJob) {
        if (massendruckJob.getStatus() != MassendruckJobStatus.FAILED) {
            forbidden();
        }
    }
}
