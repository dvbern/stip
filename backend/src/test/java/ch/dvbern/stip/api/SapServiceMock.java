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

package ch.dvbern.stip.api;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.repo.AuszahlungRepository;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.sap.service.SapEndpointService;
import ch.dvbern.stip.api.sap.service.SapService;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Mock
@RequestScoped
public class SapServiceMock extends SapService {

    public SapServiceMock() {
        super(
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Inject
    public SapServiceMock(
    SapEndpointService sapEndpointService,
    BuchhaltungService buchhaltungService,
    SapDeliveryRepository sapDeliveryRepository,
    AuszahlungRepository auszahlungRepository,
    BuchhaltungRepository buchhaltungRepository,
    GesuchRepository gesuchRepository,
    GesuchsperiodeRepository gesuchsperiodeRepository
    ) {
        super(
            sapEndpointService,
            buchhaltungService,
            sapDeliveryRepository,
            auszahlungRepository,
            buchhaltungRepository,
            gesuchRepository,
            gesuchsperiodeRepository
        );
    }

    @Override
    public SapStatus getOrCreateBusinessPartner(final Auszahlung auszahlung) {
        return SapStatus.SUCCESS;
    }

    @Override
    public SapStatus getVendorPostingCreateStatus(final Buchhaltung buchhaltung) {
        return SapStatus.SUCCESS;
    }

    @Override
    public boolean isPastSecondPaymentDate(final Gesuch gesuch) {
        return true;
    }

    @Override
    public void processPendingCreateBusinessPartnerActions() {}

    @Override
    public void processPendingCreateVendorPostingActions() {}

    @Override
    public void processRemainderAuszahlungActions() {}
}
