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

package ch.dvbern.stip.api.sap.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.auszahlung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.auszahlung.repo.AuszahlungRepository;
import ch.dvbern.stip.api.auszahlung.repo.ZahlungsverbindungRepository;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.sap.util.SapReturnCodeType;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType.AUSZAHLUNG_INITIAL;

@Slf4j
@RequestScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class SapService {
    private final SapEndpointService sapEndpointService;
    private final BuchhaltungService buchhaltungService;
    private final SapDeliveryRepository sapDeliveryRepository;
    private final AuszahlungRepository auszahlungRepository;
    private final ZahlungsverbindungRepository zahlungsverbindungRepository;
    private final BuchhaltungRepository buchhaltungRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;

    @Transactional
    public void doOrReadChangeBusinessPartner(final Zahlungsverbindung zahlungsverbindung) {
        BigDecimal deliveryid = null;
        if (Objects.isNull(zahlungsverbindung.getSapDelivery())) {
            deliveryid = SapEndpointService.generateDeliveryId();
            final var changeResponse = sapEndpointService.changeBusinessPartner(zahlungsverbindung, deliveryid);
            SapReturnCodeType.assertSuccess(changeResponse.getRETURNCODE().get(0).getTYPE());
            final var sapStatus = new SapDelivery().setSapDeliveryId(deliveryid);
            sapDeliveryRepository.persistAndFlush(sapStatus);
            zahlungsverbindung.setSapDelivery(sapStatus);
        }
        deliveryid = zahlungsverbindung.getSapDelivery().getSapDeliveryId();
        final var readImportResponse = sapEndpointService.readImportStatus(deliveryid);
        SapReturnCodeType.assertSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE());

        zahlungsverbindung.getSapDelivery()
            .setSapStatus(SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS()));
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void getBusinessPartnerCreateStatus(final UUID zahlungsverbindungId) {
        final var zahlungsverbindung = zahlungsverbindungRepository.requireById(zahlungsverbindungId);

        final BigDecimal deliveryid = zahlungsverbindung.getSapDelivery().getSapDeliveryId();
        final var readImportResponse = sapEndpointService.readImportStatus(deliveryid);
        SapReturnCodeType.assertSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE());

        final var sapDelivery = sapDeliveryRepository.requireById(zahlungsverbindung.getSapDelivery().getId());
        final var status = SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS());
        if (status == SapStatus.SUCCESS) {
            final var readResponse = sapEndpointService.readBusinessPartner(zahlungsverbindung);
            SapReturnCodeType.assertSuccess(readResponse.getRETURNCODE().get(0).getTYPE());
            zahlungsverbindung.setSapBusinessPartnerId(
                Integer.valueOf(readResponse.getBUSINESSPARTNER().getHEADER().getBPARTNER())
            );
            zahlungsverbindungRepository.persist(zahlungsverbindung);
            sapDelivery.setSapStatus(status);
        }
    }

    @Transactional
    public void createBusinessPartner(final Gesuch gesuch, final Zahlungsverbindung zahlungsverbindung) {
        final BigDecimal deliveryid = SapEndpointService.generateDeliveryId();
        final var createResponse = sapEndpointService.createBusinessPartner(zahlungsverbindung, deliveryid);
        SapReturnCodeType.assertSuccess(createResponse.getRETURNCODE().get(0).getTYPE());

        var sapDelivery = new SapDelivery().setSapDeliveryId(deliveryid);
        sapDeliveryRepository.persistAndFlush(sapDelivery);
        zahlungsverbindung.setSapDelivery(sapDelivery);
        sapDelivery = sapDeliveryRepository.requireById(sapDelivery.getId());

        final var readImportResponse = sapEndpointService.readImportStatus(deliveryid);
        SapReturnCodeType.assertSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE());

        zahlungsverbindung.getSapDelivery()
            .setSapStatus(SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS()));
        final var businessPartnerCreateBuchhaltung =
            buchhaltungService.createBuchhaltungForBusinessPartnerCreate(gesuch.getId());
        businessPartnerCreateBuchhaltung.setSapDelivery(sapDelivery);
    }

    public SapStatus getOrCreateBusinessPartner(final Gesuch gesuch) {
        final var zahlungsverbindung = gesuch.getAusbildung().getFall().getRelevantZahlungsverbindung();

        if (Objects.nonNull(zahlungsverbindung.getSapBusinessPartnerId())) {
            // Sap BusinessPartnerId exists
            doOrReadChangeBusinessPartner(zahlungsverbindung);
        } else if (Objects.nonNull(zahlungsverbindung.getSapDelivery())) {
            // Sap BusinessPartner was created but hasn't been retrieved yet
            getBusinessPartnerCreateStatus(zahlungsverbindung.getId());
        } else {
            // Sap BusinessPartner was never created and will be now. Note it will take a while (~48h) until it is
            // created after the call
            createBusinessPartner(gesuch, zahlungsverbindung);
        }
        return zahlungsverbindung.getSapDelivery().getSapStatus();
    }

    private String getQrIbanAddlInfoString(final Gesuch gesuch) {
        final var pia = gesuch.getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var language = AppLanguages.fromLocale(pia.getKorrespondenzSprache().getLocale());
        return TLProducer.defaultBundle()
            .forAppLanguage(language)
            .translate(
                "stip.auszahlung.sap.soap.qriban.addinfo",
                "vorname",
                pia.getVorname(),
                "nachname",
                pia.getNachname()
            );
    }

    @Transactional
    public SapStatus getVendorPostingCreateStatus(final Buchhaltung buchhaltung) {
        final var deliveryid = buchhaltung.getSapDelivery().getSapDeliveryId();
        final var readImportResponse = sapEndpointService.readImportStatus(deliveryid);
        SapReturnCodeType.assertSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE());

        buchhaltung.getSapDelivery()
            .setSapStatus(SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS()));
        return buchhaltung.getSapDelivery().getSapStatus();
    }

    @Transactional
    public SapStatus createVendorPostingOrGetStatus(
        final Gesuch gesuch,
        final Zahlungsverbindung zahlungsverbindung,
        final Buchhaltung buchhaltung
    ) {
        if (Objects.isNull(zahlungsverbindung.getSapBusinessPartnerId())) {
            throw new IllegalStateException("Cannot create vendor posting without existing businessPartnerId");
        }
        BigDecimal deliveryid = null;
        if (Objects.isNull(buchhaltung.getSapDelivery())) {
            deliveryid = SapEndpointService.generateDeliveryId();

            final var sapDelivery = new SapDelivery().setSapDeliveryId(deliveryid)
                .setSapBusinessPartnerId(zahlungsverbindung.getSapBusinessPartnerId());
            sapDeliveryRepository.persistAndFlush(sapDelivery);
            buchhaltung.setSapDelivery(sapDelivery);

            final var vendorPostingCreateResponse =
                sapEndpointService.createVendorPosting(
                    zahlungsverbindung,
                    buchhaltung.getBetrag(),
                    deliveryid,
                    getQrIbanAddlInfoString(gesuch),
                    String.valueOf(Math.abs(buchhaltung.getId().getMostSignificantBits()))
                );
            SapReturnCodeType.assertSuccess(vendorPostingCreateResponse.getRETURNCODE().get(0).getTYPE());
        }
        return getVendorPostingCreateStatus(buchhaltung);
    }

    public boolean isPastSecondPaymentDate(final Gesuch gesuch) {
        final var startDateFirstTranche = gesuch.getGesuchTranchen()
            .stream()
            .min(Comparator.comparing(gesuchTranche -> gesuchTranche.getGueltigkeit().getGueltigAb()))
            .orElseThrow(NotFoundException::new)
            .getGueltigkeit()
            .getGueltigAb();

        return startDateFirstTranche.plusMonths(gesuch.getGesuchsperiode().getZweiterAuszahlungsterminMonat())
            .minusDays(1)
            .isBefore(LocalDate.now());
    }

    @Transactional
    public SapStatus createInitialAuszahlungOrGetStatus(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();
        final var zahlungsverbindung = fall.getRelevantZahlungsverbindung();

        if (Objects.isNull(zahlungsverbindung.getSapBusinessPartnerId())) {
            final var createBusinessPartnerStatus = getOrCreateBusinessPartner(gesuch);
            gesuch.setPendingSapAction(AUSZAHLUNG_INITIAL);
            return createBusinessPartnerStatus;
        }
        final var pendingAuszahlungOpt =
            buchhaltungService
                .findLatestPendingBuchhaltungAuszahlungOpt(
                    gesuch.getAusbildung().getFall().getId(),
                    AUSZAHLUNG_INITIAL
                );
        Buchhaltung relevantBuchhaltung = null;

        gesuch.setPendingSapAction(null);

        if (pendingAuszahlungOpt.isEmpty()) {
            final var relevantStipendienBuchhaltung =
                buchhaltungService.getLastEntryStipendiumOpt(gesuch.getId()).orElseThrow(NotFoundException::new);
            final var lastBuchhaltungEntry =
                buchhaltungService.getLatestBuchhaltungEntry(gesuch.getAusbildung().getFall().getId());

            var auszahlungsBetrag = relevantStipendienBuchhaltung.getBetrag() / 2;
            if (isPastSecondPaymentDate(gesuch)) {
                auszahlungsBetrag = relevantStipendienBuchhaltung.getBetrag();
            }

            auszahlungsBetrag = Integer.min(auszahlungsBetrag, lastBuchhaltungEntry.getSaldo());

            if (auszahlungsBetrag <= 0) {
                return SapStatus.SUCCESS;
            }

            relevantBuchhaltung =
                buchhaltungService.createAuszahlungBuchhaltungForGesuch(
                    gesuch,
                    auszahlungsBetrag,
                    AUSZAHLUNG_INITIAL
                );
        } else {
            relevantBuchhaltung = pendingAuszahlungOpt.get();
        }
        return createVendorPostingOrGetStatus(gesuch, zahlungsverbindung, relevantBuchhaltung);
    }

    @Transactional
    public SapStatus createRemainderAuszahlungOrGetStatus(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();
        final var zahlungsverbindung = fall.getRelevantZahlungsverbindung();

        if (Objects.isNull(zahlungsverbindung.getSapBusinessPartnerId())) {
            final var createBusinessPartnerStatus = getOrCreateBusinessPartner(gesuch);
            gesuch.setPendingSapAction(BuchhaltungType.AUSZAHLUNG_REMAINDER);
            return createBusinessPartnerStatus;
        }
        gesuch.setRemainderPaymentExecuted(true);

        final var pendingAuszahlungOpt =
            buchhaltungService
                .findLatestPendingBuchhaltungAuszahlungOpt(
                    gesuch.getAusbildung().getFall().getId(),
                    BuchhaltungType.AUSZAHLUNG_REMAINDER
                );
        Buchhaltung relevantBuchhaltung = null;

        gesuch.setPendingSapAction(null);

        if (pendingAuszahlungOpt.isEmpty()) {
            final var lastBuchhaltungEntry =
                buchhaltungService.getLatestBuchhaltungEntry(gesuch.getAusbildung().getFall().getId());
            if (lastBuchhaltungEntry.getSaldo() <= 0) {
                return SapStatus.SUCCESS;
            }
            relevantBuchhaltung =
                buchhaltungService.createAuszahlungBuchhaltungForGesuch(
                    gesuch,
                    lastBuchhaltungEntry.getBetrag(),
                    BuchhaltungType.AUSZAHLUNG_REMAINDER
                );
        } else {
            relevantBuchhaltung = pendingAuszahlungOpt.get();
        }
        return createVendorPostingOrGetStatus(gesuch, zahlungsverbindung, relevantBuchhaltung);
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void processPendingSapAction(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var zahlungsverbindung = gesuch.getAusbildung().getFall().getRelevantZahlungsverbindung();

        if (!zahlungsverbindung.getSapDelivery().getSapStatus().equals(SapStatus.SUCCESS)) {
            return;
        }

        if (Objects.isNull(zahlungsverbindung.getSapBusinessPartnerId())) {
            return;
        }

        switch (gesuch.getPendingSapAction()) {
            case AUSZAHLUNG_INITIAL -> createInitialAuszahlungOrGetStatus(gesuch.getId());
            case AUSZAHLUNG_REMAINDER -> createRemainderAuszahlungOrGetStatus(gesuch.getId());
            case null -> throw new IllegalStateException("Invalid pending action: null");
            default -> throw new IllegalStateException(
                "Invalid pending action: " + gesuch.getPendingSapAction().name()
            );
        }
    }

    public void processPendingCreateBusinessPartnerActions() {
        final var pendingZahlungsverbindungs =
            zahlungsverbindungRepository.findZahlungsverbindungsWithPendingSapDelivery().toList();
        for (var zahlungsverbindung : pendingZahlungsverbindungs) {
            try {
                LOG.info(
                    String.format(
                        "Processing Zahlungsverbindung: %s, %s",
                        zahlungsverbindung.getId(),
                        zahlungsverbindung.getIban()
                    )
                );
                getBusinessPartnerCreateStatus(zahlungsverbindung.getId());
            } catch (Exception e) {
                LOG.error(
                    String.format(
                        "processPendingCreateBusinessPartnerActions: Error during processing of Zahlungsverbindung %s",
                        zahlungsverbindung.getId()
                    ),
                    e
                );
            }
        }

        final var gesuchsWithPendingSapActions = gesuchRepository.findGesuchWithPendingSapAction().toList();
        for (var gesuch : gesuchsWithPendingSapActions) {
            try {
                LOG.info(
                    String.format("processPendingCreateBusinessPartnerActions: for gesuchId: %s", gesuch.getId())
                );
                processPendingSapAction(gesuch.getId());
            } catch (Exception e) {
                LOG.error(
                    String.format(
                        "processPendingCreateBusinessPartnerActions: Error during processing of Pending SAP action Gesuch %s",
                        gesuch.getId()
                    ),
                    e
                );
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processPendingCreateVendorPostingAction(Buchhaltung buchhaltung) {
        getVendorPostingCreateStatus(buchhaltungRepository.requireById(buchhaltung.getId()));
    }

    @Transactional
    public void processPendingCreateVendorPostingActions() {
        final var pendingBuchhaltungs = buchhaltungRepository.findBuchhaltungWithPendingSapDelivery().toList();
        for (var buchhaltung : pendingBuchhaltungs) {
            try {
                processPendingCreateVendorPostingAction(buchhaltung);
            } catch (Exception e) {
                LOG.error(
                    String.format(
                        "processPendingCreateVendorPostingActions: Error during processing of Buchaltung %s",
                        buchhaltung.getId()
                    ),
                    e
                );
            }
        }
    }

    @Transactional
    public void processRemainderAuszahlungActions() {
        gesuchsperiodeRepository.listAll()
            .stream()
            .filter(
                gesuchsperiode -> gesuchsperiode.getZweiterAuszahlungsterminTag() == LocalDate.now().getDayOfMonth()
            )
            .flatMap(
                gesuchsperiode -> gesuchRepository.findGesuchsByGesuchsperiodeId(gesuchsperiode.getId()).stream()
            )
            .filter(gesuch -> !gesuch.isRemainderPaymentExecuted())
            .filter(this::isPastSecondPaymentDate)
            .forEach(gesuch -> {
                try {
                    createRemainderAuszahlungOrGetStatus(
                        gesuch.getAusbildung().getFall().getAuszahlung().getId()
                    );
                } catch (Exception e) {
                    LOG.error(
                        String.format(
                            "processRemainderAuszahlungActions: Error during processing of gesuch %s",
                            gesuch.getId()
                        ),
                        e
                    );
                }
            }
            );
    }
}
