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

package ch.dvbern.stip.integration.zahlung.domain.port;

import java.util.UUID;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.fall.entity.Fall;

public interface ZahlungPort {
    Buchhaltung retryAuszahlungBuchhaltung(final UUID gesuchId);

    default Buchhaltung retryAuszahlungBuchhaltung(final Fall fall) {
        return retryAuszahlungBuchhaltung(fall.getLatestGesuch().getId());
    }

    void processPendingCreateVendorPostingActions();

    void processRemainderAuszahlungActions();

    void processRetryFailedAuszahlungsBuchhaltung();

    void createInitialAuszahlungOrGetStatus(final UUID gesuchId);

    void createRemainderAuszahlungOrGetStatus(final UUID gesuchId);

    void processPendingBusinessPartnerActions();
}
