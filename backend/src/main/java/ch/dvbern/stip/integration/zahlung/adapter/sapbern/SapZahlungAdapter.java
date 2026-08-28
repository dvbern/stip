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

package ch.dvbern.stip.integration.zahlung.adapter.sapbern;

import java.util.UUID;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.integration.zahlung.adapter.sapbern.service.SapService;
import ch.dvbern.stip.integration.zahlung.domain.model.ZahlungAdapterType;
import ch.dvbern.stip.integration.zahlung.domain.port.ZahlungPort;
import ch.dvbern.stip.integration.zahlung.domain.qualifier.ZahlungQualifier;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@ZahlungQualifier(ZahlungAdapterType.SAP_BERN)
@RequiredArgsConstructor(onConstructor_ = @Inject)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class SapZahlungAdapter implements ZahlungPort {
    final SapService sapService;

    @Override
    public Buchhaltung retryAuszahlungBuchhaltung(UUID gesuchId) {
        return sapService.retryAuszahlungBuchhaltung(gesuchId);
    }

    @Override
    public void processPendingCreateVendorPostingActions() {
        sapService.processPendingCreateVendorPostingActions();
    }

    @Override
    public void processRemainderAuszahlungActions() {
        sapService.processRemainderAuszahlungActions();
    }

    @Override
    public void processRetryFailedAuszahlungsBuchhaltung() {
        sapService.processRetryFailedAuszahlungsBuchhaltung();
    }

    @Override
    public void createInitialAuszahlungOrGetStatus(UUID gesuchId) {
        sapService.createInitialAuszahlungOrGetStatus(gesuchId);
    }

    @Override
    public void createRemainderAuszahlungOrGetStatus(UUID gesuchId) {
        sapService.createRemainderAuszahlungOrGetStatus(gesuchId);
    }

    @Override
    public void processPendingBusinessPartnerActions() {
        sapService.processPendingBusinessPartnerActions();
    }
}
