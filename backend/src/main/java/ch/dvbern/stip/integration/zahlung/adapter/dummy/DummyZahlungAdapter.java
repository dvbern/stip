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

package ch.dvbern.stip.integration.zahlung.adapter.dummy;

import java.math.BigDecimal;
import java.util.UUID;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.integration.zahlung.domain.model.ZahlungAdapterType;
import ch.dvbern.stip.integration.zahlung.domain.port.ZahlungPort;
import ch.dvbern.stip.integration.zahlung.domain.qualifier.ZahlungQualifier;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType.AUSZAHLUNG_INITIAL;
import static ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType.BUSINESSPARTNER_CREATE;

@Slf4j
@RequestScoped
@ZahlungQualifier(ZahlungAdapterType.DUMMY)
@RequiredArgsConstructor(onConstructor_ = @Inject)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class DummyZahlungAdapter implements ZahlungPort {
    final GesuchService gesuchService;
    final GesuchRepository gesuchRepository;
    final BuchhaltungService buchhaltungService;
    final BuchhaltungRepository buchhaltungRepository;
    final SapDeliveryRepository sapDeliveryRepository;

    @Override
    public Buchhaltung retryAuszahlungBuchhaltung(UUID gesuchId) {
        final var gesuch = gesuchService.getGesuchById(gesuchId);
        return buchhaltungService.getLatestBuchhaltungEntry(gesuch.getAusbildung().getFall().getId());
    }

    @Override
    public void processPendingCreateVendorPostingActions() {}

    @Override
    public void processPendingBusinessPartnerActions() {}

    @Override
    public void processRemainderAuszahlungActions() {}

    @Override
    public void processRetryFailedAuszahlungsBuchhaltung() {}

    @Override
    public void createInitialAuszahlungOrGetStatus(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();
        fall.setFailedBuchhaltungAuszahlungType(null);
        gesuch.setPendingSapAction(null);

        final var businesParnterBuchhaltung = buchhaltungService
            .createBuchhaltungForBusinessPartnerAction(gesuch.getId(), BUSINESSPARTNER_CREATE);
        createAndAppendSuccessfullSapDelivery(fall.getAuszahlung(), businesParnterBuchhaltung);

        final var relevantStipendienBuchhaltung =
            buchhaltungService.getLastEntryStipendiumOpt(gesuch.getId()).orElseThrow(NotFoundException::new);
        final var lastBuchhaltungEntry =
            buchhaltungService.getLatestNotFailedBuchhaltungEntry(gesuch.getAusbildung().getFall().getId());
        var auszahlungsBetrag = relevantStipendienBuchhaltung.getSaldo();

        auszahlungsBetrag = Integer.min(auszahlungsBetrag, lastBuchhaltungEntry.getSaldo());

        if (auszahlungsBetrag <= 0) {
            return;
        }

        final var auszahlungBuchhaltung =
            buchhaltungService.createAuszahlungBuchhaltungForGesuch(
                gesuch,
                auszahlungsBetrag,
                AUSZAHLUNG_INITIAL
            );
        createAndAppendSuccessfullSapDelivery(fall.getAuszahlung(), auszahlungBuchhaltung);
    }

    @Override
    public void createRemainderAuszahlungOrGetStatus(UUID gesuchId) {}

    private void createAndAppendSuccessfullSapDelivery(Auszahlung auszahlung, Buchhaltung buchhaltung) {
        final var newSapDelivery =
            new SapDelivery().setSapDeliveryId(BigDecimal.valueOf(UUID.randomUUID().getMostSignificantBits()))
                .setSapBusinessPartnerId(auszahlung.getSapBusinessPartnerId());
        newSapDelivery.setSapStatus(SapStatus.SUCCESS);
        newSapDelivery.setBuchhaltung(buchhaltung);
        sapDeliveryRepository.persist(newSapDelivery);
        buchhaltung.getSapDeliverys().add(newSapDelivery);
        buchhaltungRepository.persist(buchhaltung);
    }
}
