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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.repo.AdresseRepository;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.repo.AuszahlungRepository;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.sap.util.SapReturnCodeType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType.AUSZAHLUNG_INITIAL;
import static ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType.BUSINESSPARTNER_CREATE;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class SapService {
    public static final Integer HOURS_BETWEEN_SAP_TRIES = 24;

    private final SapEndpointService sapEndpointService;
    private final BuchhaltungService buchhaltungService;
    private final SapDeliveryRepository sapDeliveryRepository;
    private final AuszahlungRepository auszahlungRepository;
    private final BuchhaltungRepository buchhaltungRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final AdresseRepository adresseRepository;
    private final NotificationService notificationService;
    private final MailService mailService;

    @Transactional
    public void getBusinessPartnerCreateStatus(final UUID auszahlungId) {
        final var auszahlung = auszahlungRepository.requireById(auszahlungId);
        final var buchhaltung = auszahlung.getBuchhaltung();
        final SapDelivery sapDelivery = buchhaltung.getLatestSapDelivery();
        final BigDecimal deliveryid = sapDelivery.getSapDeliveryId();

        final var readImportResponse = sapEndpointService.readImportStatus(deliveryid);

        var status = SapStatus.FAILURE;
        if (SapReturnCodeType.isSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE())) {
            status = SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS());
        }
        if (status == SapStatus.SUCCESS) {
            final var readResponse = sapEndpointService.readBusinessPartner(deliveryid);
            SapReturnCodeType.assertSuccess(readResponse.getRETURNCODE().get(0).getTYPE());
            buchhaltung.getFall()
                .getAuszahlung()
                .setSapBusinessPartnerId(
                    Integer.valueOf(readResponse.getBUSINESSPARTNER().getHEADER().getBPARTNER())
                );
        }
        sapDelivery.setSapStatus(status);
    }

    @Transactional
    public void createBusinessPartnerOrGetStatus(
        final Gesuch gesuch
    ) {
        final var fall = gesuch.getAusbildung().getFall();
        final var auszahlung = fall.getAuszahlung();
        Buchhaltung businessPartnerCreateBuchhaltung = auszahlung.getBuchhaltung();
        if (
            Objects.isNull(businessPartnerCreateBuchhaltung)
            || businessPartnerCreateBuchhaltung.getSapStatus() == SapStatus.FAILURE
        ) {
            businessPartnerCreateBuchhaltung =
                buchhaltungService.createBuchhaltungForBusinessPartnerCreate(gesuch.getId());
            fall.setFailedBuchhaltungAuszahlungType(null);
        }

        if (
            !EnumSet.of(SapStatus.SUCCESS, SapStatus.IN_PROGRESS)
                .contains(businessPartnerCreateBuchhaltung.getSapStatus())
        ) {
            throw new IllegalStateException(
                String.format(
                    "buchhaltung status is not IN_PROGRESS or SUCCESS but %s",
                    businessPartnerCreateBuchhaltung.getSapStatus()
                )
            );
        }

        final var sapDeliverys = businessPartnerCreateBuchhaltung.getSapDeliverys();

        final var sapDeliveryInProgress = sapDeliverys
            .stream()
            .filter(
                sapDelivery1 -> sapDelivery1.getSapStatus() == SapStatus.IN_PROGRESS
            )
            .sorted(Comparator.comparing(SapDelivery::getTimestampErstellt).reversed())
            .findFirst();

        final var lastSapDelivery = sapDeliverys.stream().max(Comparator.comparing(SapDelivery::getTimestampErstellt));

        BigDecimal deliveryid = null;
        if (sapDeliveryInProgress.isEmpty()) {
            final var lastTryWasBeforeRetryPeriod = lastSapDelivery.isPresent()
            && lastSapDelivery.get()
                .getTimestampErstellt()
                .plusHours(HOURS_BETWEEN_SAP_TRIES)
                .isBefore(LocalDateTime.now());
            if (lastSapDelivery.isEmpty() || lastTryWasBeforeRetryPeriod) {
                deliveryid = SapEndpointService.generateDeliveryId();

                var sapDelivery = new SapDelivery().setSapDeliveryId(deliveryid);
                sapDeliveryRepository.persistAndFlush(sapDelivery);
                sapDelivery = sapDeliveryRepository.requireById(sapDelivery.getId());

                boolean success = false;
                try {
                    final var createResponse = sapEndpointService.createBusinessPartner(fall, deliveryid);
                    SapReturnCodeType.assertSuccess(createResponse.getRETURNCODE().get(0).getTYPE());
                    success = true;
                } catch (Exception e) {
                    LOG.error("Failed to send createBusinessPartner action", e);
                    sapDelivery.setSapStatus(SapStatus.FAILURE);
                }

                if (success) {
                    final var readImportResponse = sapEndpointService.readImportStatus(deliveryid);
                    SapReturnCodeType.assertSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE());

                    sapDelivery
                        .setSapStatus(SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS()));
                }

                sapDelivery.setBuchhaltung(businessPartnerCreateBuchhaltung);
                businessPartnerCreateBuchhaltung.getSapDeliverys().add(sapDelivery);
            }
        }
        try {
            getBusinessPartnerCreateStatus(auszahlung.getId());
        } catch (Exception e) {
            LOG.error("Failed to read BusinessPartnerCreateStatus", e);
        }

        if (businessPartnerCreateBuchhaltung.getSapStatus() == SapStatus.FAILURE) {
            fall.setFailedBuchhaltungAuszahlungType(BUSINESSPARTNER_CREATE);
            notificationService.createFailedAuszahlungBuchhaltungNotification(gesuch);
            mailService.sendStandardNotificationEmailForGesuch(gesuch);
        } else if (businessPartnerCreateBuchhaltung.getSapStatus() == SapStatus.SUCCESS) {
            // gesuch.getAusbildung()
            // .getFall()
            // .getAuszahlung()
            // .setSapBusinessPartnerId(
            // businessPartnerCreateBuchhaltung.getZahlungsverbindung().getSapBusinessPartnerId()
            // );
        }
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
    public void getVendorPostingCreateStatus(final Buchhaltung buchhaltung) {
        final var sapDeliveryOpt = buchhaltung.getSapDeliverys()
            .stream()
            .filter(
                sapDelivery1 -> sapDelivery1.getSapStatus() == SapStatus.IN_PROGRESS
            )
            .findFirst();
        if (sapDeliveryOpt.isEmpty()) {
            return;
        }
        final var sapDelivery = sapDeliveryOpt.get();
        final var deliveryid = sapDelivery.getSapDeliveryId();
        final var readImportResponse = sapEndpointService.readImportStatus(deliveryid);
        SapReturnCodeType.assertSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE());

        sapDelivery
            .setSapStatus(SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS()));

    }

    @Transactional
    public void createVendorPostingOrGetStatus(
        final Gesuch gesuch,
        final Auszahlung auszahlung,
        final Buchhaltung buchhaltung
    ) {
        if (Objects.isNull(auszahlung.getSapBusinessPartnerId())) {
            throw new IllegalStateException("Cannot create vendor posting without existing businessPartnerId");
        }
        if (buchhaltung.getSapStatus() != SapStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                String.format("buchhaltung status is not IN_PROGRESS but %s", buchhaltung.getSapStatus())
            );
        }

        final var sapDeliverys = buchhaltung.getSapDeliverys();

        final var sapDeliveryInProgress = sapDeliverys
            .stream()
            .filter(
                sapDelivery1 -> sapDelivery1.getSapStatus() == SapStatus.IN_PROGRESS
            )
            .sorted(Comparator.comparing(SapDelivery::getTimestampErstellt).reversed())
            .findFirst();

        final var lastSapDelivery = sapDeliverys.stream().max(Comparator.comparing(SapDelivery::getTimestampErstellt));

        BigDecimal deliveryid = null;
        if (sapDeliveryInProgress.isEmpty()) {
            final var lastTryWasBeforeRetryPeriod = lastSapDelivery.isPresent()
            && lastSapDelivery.get()
                .getTimestampErstellt()
                .plusHours(HOURS_BETWEEN_SAP_TRIES)
                .isBefore(LocalDateTime.now());

            if (lastSapDelivery.isEmpty() || lastTryWasBeforeRetryPeriod) {
                deliveryid = SapEndpointService.generateDeliveryId();

                final var newSapDelivery = new SapDelivery().setSapDeliveryId(deliveryid)
                    .setSapBusinessPartnerId(auszahlung.getSapBusinessPartnerId());
                newSapDelivery.setSapStatus(SapStatus.IN_PROGRESS);
                newSapDelivery.setBuchhaltung(buchhaltung);
                sapDeliveryRepository.persistAndFlush(newSapDelivery);
                buchhaltung.getSapDeliverys().add(newSapDelivery);

                try {
                    final var vendorPostingCreateResponse =
                        sapEndpointService.createVendorPosting(
                            gesuch.getAusbildung().getFall(),
                            buchhaltung.getBetrag(),
                            deliveryid,
                            getQrIbanAddlInfoString(gesuch),
                            String.valueOf(Math.abs(newSapDelivery.getId().getMostSignificantBits()))
                        );
                    SapReturnCodeType.assertSuccess(vendorPostingCreateResponse.getRETURNCODE().get(0).getTYPE());
                } catch (Exception e) {
                    LOG.error("Failed to send createVendorPosting action", e);
                    newSapDelivery.setSapStatus(SapStatus.FAILURE);
                }
            }
        }
        try {
            getVendorPostingCreateStatus(buchhaltung);
        } catch (Exception e) {
            LOG.error("Failed to read VendorPostingCreateStatus", e);
        }

        if (buchhaltung.getSapStatus() == SapStatus.FAILURE) {
            gesuch.getAusbildung().getFall().setFailedBuchhaltungAuszahlungType(buchhaltung.getBuchhaltungType());
            notificationService.createFailedAuszahlungBuchhaltungNotification(gesuch);
            mailService.sendStandardNotificationEmailForGesuch(gesuch);
        }
    }

    public Buchhaltung retryAuszahlungBuchhaltung(final Fall fall) {
        final var gesuch = fall.getAusbildungs()
            .stream()
            .sorted(
                Comparator.comparing(Ausbildung::getTimestampErstellt).reversed()
            )
            .findFirst()
            .get()
            .getGesuchs()
            .stream()
            .sorted(
                Comparator.comparing(Gesuch::getTimestampErstellt).reversed()
            )
            .findFirst()
            .get();

        switch (fall.getFailedBuchhaltungAuszahlungType()) {
            case AUSZAHLUNG_INITIAL -> createInitialAuszahlungOrGetStatus(gesuch.getId());
            case AUSZAHLUNG_REMAINDER -> createRemainderAuszahlungOrGetStatus(gesuch.getId());
            case BUSINESSPARTNER_CREATE -> {
                gesuch.getAusbildung().getFall().getAuszahlung().setBuchhaltung(null);
                createBusinessPartnerOrGetStatus(gesuch);
            }
            case null, default -> throw new BadRequestException();
        }

        return buchhaltungService.getLatestBuchhaltungEntry(fall.getId());
    }

    @Transactional
    public Buchhaltung retryAuszahlungBuchhaltung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        switch (gesuch.getAusbildung().getFall().getFailedBuchhaltungAuszahlungType()) {
            case AUSZAHLUNG_INITIAL -> createInitialAuszahlungOrGetStatus(gesuchId);
            case AUSZAHLUNG_REMAINDER -> createRemainderAuszahlungOrGetStatus(gesuchId);
            case BUSINESSPARTNER_CREATE -> {
                gesuch.getAusbildung().getFall().getAuszahlung().setBuchhaltung(null);
                createBusinessPartnerOrGetStatus(gesuch);
            }
            default -> throw new BadRequestException();
        }

        final var buchhaltung = buchhaltungService.getLatestBuchhaltungEntry(gesuch.getAusbildung().getFall().getId());
        buchhaltung.getZahlungsverbindung()
            .setAdresse(adresseRepository.requireById(buchhaltung.getZahlungsverbindung().getAdresse().getId()));
        return buchhaltung;
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
            .isAfter(LocalDate.now());
    }

    @Transactional
    public void createInitialAuszahlungOrGetStatus(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();
        final var zahlungsverbindung = fall.getRelevantZahlungsverbindung();
        fall.setFailedBuchhaltungAuszahlungType(null);

        if (Objects.isNull(fall.getAuszahlung().getSapBusinessPartnerId())) {
            createBusinessPartnerOrGetStatus(gesuch);
            gesuch.setPendingSapAction(AUSZAHLUNG_INITIAL);
            return;
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
                buchhaltungService.getLatestNotFailedBuchhaltungEntry(gesuch.getAusbildung().getFall().getId());

            var auszahlungsBetrag = relevantStipendienBuchhaltung.getBetrag() / 2;
            if (isPastSecondPaymentDate(gesuch)) {
                auszahlungsBetrag = relevantStipendienBuchhaltung.getBetrag();
            }

            auszahlungsBetrag = Integer.min(auszahlungsBetrag, lastBuchhaltungEntry.getSaldo());

            if (auszahlungsBetrag <= 0) {
                return;
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
        createVendorPostingOrGetStatus(gesuch, fall.getAuszahlung(), relevantBuchhaltung);
    }

    @Transactional
    public void createRemainderAuszahlungOrGetStatus(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();
        final var zahlungsverbindung = fall.getRelevantZahlungsverbindung();
        fall.setFailedBuchhaltungAuszahlungType(null);

        if (Objects.isNull(fall.getAuszahlung().getSapBusinessPartnerId())) {
            createBusinessPartnerOrGetStatus(gesuch);
            gesuch.setPendingSapAction(BuchhaltungType.AUSZAHLUNG_REMAINDER);
            return;
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
                buchhaltungService.getLatestNotFailedBuchhaltungEntry(gesuch.getAusbildung().getFall().getId());
            if (lastBuchhaltungEntry.getSaldo() <= 0) {
                return;
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
        createVendorPostingOrGetStatus(gesuch, fall.getAuszahlung(), relevantBuchhaltung);
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void processPendingSapAction(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();

        if (!fall.getAuszahlung().getBuchhaltung().getSapStatus().equals(SapStatus.SUCCESS)) {
            return;
        }

        if (Objects.isNull(fall.getAuszahlung().getSapBusinessPartnerId())) {
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
        final var pendingBusinessPartnerCreateBuchhaltungs =
            buchhaltungRepository.findPendingBusinesspartnerCreateBuchhaltung().toList();

        for (final var pendingBusinessPartnerCreateBuchhaltung : pendingBusinessPartnerCreateBuchhaltungs) {
            try {
                LOG.info(
                    String.format(
                        "Processing pendingBusinessPartnerCreateBuchhaltung: %s",
                        pendingBusinessPartnerCreateBuchhaltung.getId()
                    )
                );
                final var gesuch = pendingBusinessPartnerCreateBuchhaltung.getGesuch();
                createBusinessPartnerOrGetStatus(gesuch);
            } catch (Exception e) {
                LOG.error(
                    String.format(
                        "processPendingCreateBusinessPartnerActions: Error during processing of pendingBusinessPartnerCreateBuchhaltung %s",
                        pendingBusinessPartnerCreateBuchhaltung.getId()
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
        final var pendingBuchhaltungs =
            buchhaltungRepository.findAuszahlungBuchhaltungWithPendingSapDelivery().toList();
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
                gesuchsperiode -> gesuchRepository
                    .findGesuchsByGesuchsperiodeIdWithPendingRemainderPayment(gesuchsperiode.getId())
                    .stream()
            )
            .filter(this::isPastSecondPaymentDate)
            .forEach(gesuch -> {
                try {
                    createRemainderAuszahlungOrGetStatus(
                        gesuch.getId()
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

    @Transactional(TxType.REQUIRES_NEW)
    void retryOngoingBuchhaltungAuszahlungWithFailures(final Buchhaltung buchhaltung) {
        assert buchhaltung.getZahlungsverbindung() != null;
        createVendorPostingOrGetStatus(buchhaltung.getGesuch(), buchhaltung.getFall().getAuszahlung(), buchhaltung);
    }

    @Transactional
    public void processRetryFailedAuszahlungsBuchhaltung() {
        buchhaltungRepository.findAuszahlungBuchhaltungWithFailedSapDelivery()
            .toList()
            .forEach(
                buchhaltung -> {
                    try {
                        retryOngoingBuchhaltungAuszahlungWithFailures(buchhaltung);
                    } catch (Exception e) {
                        LOG.error(
                            String.format(
                                "processRetryFailedAuszahlungsBuchhaltung: Error during processing of buchhaltung %s",
                                buchhaltung.getId()
                            ),
                            e
                        );
                    }
                }
            );
    }
}
