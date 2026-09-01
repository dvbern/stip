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
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.repo.AuszahlungRepository;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerSearchResponse.BUSINESSPARTNER;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.sap.util.SapMapperUtil;
import ch.dvbern.stip.api.sap.util.SapReturnCodeType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType.AUSZAHLUNG_INITIAL;
import static ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType.BUSINESSPARTNER_CHANGE;
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
    private final BusinessPartnerChangeMapper businessPartnerChangeMapper;

    private boolean businessPartnerNeedsUpdate(
        final Gesuch gesuch,
        final Integer businessPartnerId
    ) {
        final var businessPartner =
            sapEndpointService
                .readBusinessPartnerByBusinessPartnerId(gesuch.getAusbildung().getFall(), businessPartnerId)
                .getBUSINESSPARTNER();
        final var businessPartnerChangeRequest =
            businessPartnerChangeMapper.toBusinessPartner(gesuch.getAusbildung().getFall());
        final var addressLocal = businessPartnerChangeRequest.getADDRESS().get(0);
        final var addressRemote = businessPartner.getADDRESS().get(0);

        if (
            !Objects.equals(addressLocal.getCOUNTRY(), addressRemote.getCOUNTRY())
            || !Objects.equals(addressLocal.getCITY(), addressRemote.getCITY())
            || !Objects.equals(addressLocal.getCONAME(), addressRemote.getCONAME())
            || !Objects.equals(addressLocal.getSTREET(), addressRemote.getSTREET())
            || !Objects.equals(addressLocal.getHOUSENO(), addressRemote.getHOUSENO())
            || !Objects.equals(addressLocal.getPOSTLCOD1(), addressRemote.getPOSTLCOD1())
        ) {
            return true;
        }

        final var persdataLocal = businessPartnerChangeRequest.getPERSDATA();
        final var persdataRemote = businessPartner.getPERSDATA();

        if (
            !Objects.equals(persdataLocal.getFIRSTNAME(), persdataRemote.getFIRSTNAME())
            || !Objects.equals(persdataLocal.getLASTNAME(), persdataRemote.getLASTNAME())
            || !Objects.equals(persdataLocal.getNATIONALITYISO(), persdataRemote.getNATIONALITYISO())
            || !Objects.equals(persdataLocal.getBIRTHDATE(), persdataRemote.getBIRTHDATE())
        ) {
            return true;
        }

        final var paymentdetailLocal = businessPartnerChangeRequest.getPAYMENTDETAIL();
        final var paymentdetailRemote = businessPartner.getPAYMENTDETAIL();

        if (
            !Objects.equals(
                SapMapperUtil.stripWhitespace(paymentdetailLocal.get(0).getIBAN()),
                SapMapperUtil.stripWhitespace(paymentdetailRemote.get(0).getIBAN())
            )
            || !Objects
                .equals(paymentdetailLocal.get(0).getACCOUNTHOLDER(), paymentdetailRemote.get(0).getACCOUNTHOLDER())
        ) {
            return true;
        }

        return false;
    }

    private BUSINESSPARTNER searchBusinessPartner(final Fall fall, final String sozialversicherungsnummer) {
        final var businessPartnerSearchResponse =
            sapEndpointService.searchBusinessPartner(fall, sozialversicherungsnummer);

        if (businessPartnerSearchResponse.getBUSINESSPARTNER().isEmpty()) {
            return null;
        }
        return businessPartnerSearchResponse.getBUSINESSPARTNER().get(0);
    }

    private void getBusinessPartnerActionStatus(final UUID auszahlungId) {
        final var auszahlung = auszahlungRepository.requireById(auszahlungId);
        final var buchhaltung = auszahlung.getBuchhaltung();
        final SapDelivery sapDelivery = buchhaltung.getLatestSapDelivery();
        final BigDecimal deliveryid = sapDelivery.getSapDeliveryId();

        final var readImportResponse = sapEndpointService.readImportStatus(buchhaltung.getFall(), deliveryid);

        var status = SapStatus.FAILURE;
        if (SapReturnCodeType.isSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE())) {
            status = SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS());
        }
        if (status == SapStatus.SUCCESS) {
            final var readResponse =
                sapEndpointService.readBusinessPartnerByDeliveryId(buchhaltung.getFall(), deliveryid);
            SapReturnCodeType.assertSuccess(readResponse.getRETURNCODE().get(0).getTYPE());
            buchhaltung.getFall()
                .getAuszahlung()
                .setSapBusinessPartnerId(
                    Integer.valueOf(readResponse.getBUSINESSPARTNER().getHEADER().getBPARTNER())
                );
        }
        sapDelivery.setSapStatus(status);
    }

    private Buchhaltung getBusinessPartnerActionBuchhaltung(
        final Gesuch gesuch,
        final BuchhaltungType buchhaltungBusinessPartnerType
    ) {
        if (
            !EnumSet.of(BuchhaltungType.BUSINESSPARTNER_CREATE, BuchhaltungType.BUSINESSPARTNER_CHANGE)
                .contains(buchhaltungBusinessPartnerType)
        ) {
            throw new IllegalStateException();
        }
        final var fall = gesuch.getAusbildung().getFall();
        final var auszahlung = fall.getAuszahlung();
        Buchhaltung businessPartnerActionBuchhaltung = auszahlung.getBuchhaltung();

        if (Objects.nonNull(businessPartnerActionBuchhaltung)) {
            if (businessPartnerActionBuchhaltung.getBuchhaltungType() != buchhaltungBusinessPartnerType) {
                throw new IllegalStateException();
            }
        }

        if (
            Objects.isNull(businessPartnerActionBuchhaltung)
            || businessPartnerActionBuchhaltung.getSapStatus() == SapStatus.FAILURE
        ) {
            businessPartnerActionBuchhaltung = buchhaltungService
                .createBuchhaltungForBusinessPartnerAction(gesuch.getId(), buchhaltungBusinessPartnerType);
            fall.setFailedBuchhaltungAuszahlungType(null);
        }

        if (
            !EnumSet.of(SapStatus.SUCCESS, SapStatus.IN_PROGRESS)
                .contains(businessPartnerActionBuchhaltung.getSapStatus())
        ) {
            throw new IllegalStateException(
                String.format(
                    "buchhaltung status is not IN_PROGRESS or SUCCESS but %s",
                    businessPartnerActionBuchhaltung.getSapStatus()
                )
            );
        }
        return businessPartnerActionBuchhaltung;
    }

    @Transactional
    public void doBusinessPartnerActionOrGetStatus(
        final Gesuch gesuch,
        final BuchhaltungType businessPartnerActionBuchhaltungType
    ) {
        if (
            !EnumSet.of(BuchhaltungType.BUSINESSPARTNER_CREATE, BuchhaltungType.BUSINESSPARTNER_CHANGE)
                .contains(businessPartnerActionBuchhaltungType)
        ) {
            throw new IllegalStateException();
        }
        Buchhaltung businessPartnerActionBuchhaltung =
            getBusinessPartnerActionBuchhaltung(gesuch, businessPartnerActionBuchhaltungType);

        final var sapDeliverys = businessPartnerActionBuchhaltung.getSapDeliverys();
        final var sapDeliveryInProgress = sapDeliverys
            .stream()
            .filter(
                sapDelivery1 -> sapDelivery1.getSapStatus() == SapStatus.IN_PROGRESS
            )
            .sorted(Comparator.comparing(SapDelivery::getTimestampErstellt).reversed())
            .findFirst();

        final var fall = gesuch.getAusbildung().getFall();
        BigDecimal deliveryid = null;
        if (sapDeliveryInProgress.isEmpty()) {
            final var lastSapDelivery =
                sapDeliverys.stream().max(Comparator.comparing(SapDelivery::getTimestampErstellt));
            final var lastTryWasBeforeRetryPeriod = lastSapDelivery.isPresent()
            && lastSapDelivery.get()
                .getTimestampErstellt()
                .plusHours(HOURS_BETWEEN_SAP_TRIES)
                .isBefore(LocalDateTime.now());
            if (lastSapDelivery.isEmpty() || lastTryWasBeforeRetryPeriod) {
                deliveryid = SapEndpointService.generateDeliveryId(sapEndpointService.getSystemid());

                var sapDelivery = new SapDelivery().setSapDeliveryId(deliveryid);
                sapDeliveryRepository.persistAndFlush(sapDelivery);
                sapDelivery = sapDeliveryRepository.requireById(sapDelivery.getId());

                try {
                    switch (businessPartnerActionBuchhaltungType) {
                        case BUSINESSPARTNER_CREATE -> {
                            final var response = sapEndpointService.createBusinessPartner(fall, deliveryid);
                            SapReturnCodeType.assertSuccess(response.getRETURNCODE().get(0).getTYPE());
                        }
                        case BUSINESSPARTNER_CHANGE -> {
                            final var response = sapEndpointService.changeBusinessPartner(fall, deliveryid);
                            SapReturnCodeType.assertSuccess(response.getRETURNCODE().get(0).getTYPE());
                        }
                        case null, default -> throw new IllegalStateException();
                    }
                } catch (Exception e) {
                    LOG.error(
                        String.format("Failed to send %s action", businessPartnerActionBuchhaltungType.name()),
                        e
                    );
                }

                sapDelivery.setBuchhaltung(businessPartnerActionBuchhaltung);
                businessPartnerActionBuchhaltung.getSapDeliverys().add(sapDelivery);
            } else {
                return;
            }
        }
        try {
            getBusinessPartnerActionStatus(fall.getAuszahlung().getId());
        } catch (Exception e) {
            LOG.error(String.format("Failed to read %s status", businessPartnerActionBuchhaltungType.name()), e);
        }

        if (businessPartnerActionBuchhaltung.getSapStatus() == SapStatus.FAILURE) {
            fall.setFailedBuchhaltungAuszahlungType(businessPartnerActionBuchhaltungType);
            notificationService.createFailedAuszahlungBuchhaltungNotificationAndSendStdMail(gesuch);
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

    private void getVendorPostingCreateStatus(final Buchhaltung buchhaltung) {
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
        final var readImportResponse = sapEndpointService.readImportStatus(buchhaltung.getFall(), deliveryid);
        SapReturnCodeType.assertSuccess(readImportResponse.getRETURNCODE().get(0).getTYPE());

        sapDelivery
            .setSapStatus(SapStatus.parse(readImportResponse.getDELIVERY().get(0).getSTATUS()));

    }

    private void createVendorPostingOrGetStatus(
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

        BigDecimal deliveryid = null;
        if (sapDeliveryInProgress.isEmpty()) {
            final var lastSapDelivery =
                sapDeliverys.stream().max(Comparator.comparing(SapDelivery::getTimestampErstellt));
            final var lastTryWasBeforeRetryPeriod = lastSapDelivery.isPresent()
            && lastSapDelivery.get()
                .getTimestampErstellt()
                .plusHours(HOURS_BETWEEN_SAP_TRIES)
                .isBefore(LocalDateTime.now());

            if (lastSapDelivery.isEmpty() || lastTryWasBeforeRetryPeriod) {
                deliveryid = SapEndpointService.generateDeliveryId(sapEndpointService.getSystemid());

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
            notificationService.createFailedAuszahlungBuchhaltungNotificationAndSendStdMail(gesuch);
        }
    }

    public Buchhaltung retryAuszahlungBuchhaltung(final Fall fall) {
        final var gesuch = fall.getLatestGesuch();

        return retryAuszahlungBuchhaltung(gesuch.getId());
    }

    @Transactional
    public Buchhaltung retryAuszahlungBuchhaltung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        switch (gesuch.getAusbildung().getFall().getFailedBuchhaltungAuszahlungType()) {
            case AUSZAHLUNG_INITIAL -> createInitialAuszahlungOrGetStatus(gesuchId);
            case AUSZAHLUNG_REMAINDER -> createRemainderAuszahlungOrGetStatus(gesuchId);
            case BUSINESSPARTNER_CREATE, BUSINESSPARTNER_CHANGE -> {
                gesuch.getAusbildung().getFall().getAuszahlung().setBuchhaltung(null);
                getUpdateOrCreateBusinessPartner(gesuch);
            }
            case null, default -> throw new BadRequestException();
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

        return LocalDate.now()
            .isAfter(
                startDateFirstTranche.plusMonths(gesuch.getGesuchsperiode().getZweiterAuszahlungsterminMonat())
                    .minusDays(1)
            );
    }

    private void getUpdateOrCreateBusinessPartner(final Gesuch gesuch) {
        final PersonInAusbildung pia = SapMapperUtil.getPia(gesuch.getAusbildung().getFall());
        BUSINESSPARTNER businesspartner = null;
        try {
            businesspartner =
                searchBusinessPartner(gesuch.getAusbildung().getFall(), pia.getSozialversicherungsnummer());
        } catch (Exception e) {
            LOG.error(String.format("Failed to searchBusinessPartner"), e);
        }

        if (Objects.nonNull(businesspartner)) {
            gesuch.getAusbildung()
                .getFall()
                .getAuszahlung()
                .setSapBusinessPartnerId(
                    Integer.valueOf(businesspartner.getHEADER().getBPARTNER())
                );
            if (businessPartnerNeedsUpdate(gesuch, Integer.valueOf(businesspartner.getHEADER().getBPARTNER()))) {
                doBusinessPartnerActionOrGetStatus(gesuch, BUSINESSPARTNER_CHANGE);
            }
        } else {
            doBusinessPartnerActionOrGetStatus(gesuch, BUSINESSPARTNER_CREATE);
        }
    }

    @Transactional
    public void createInitialAuszahlungOrGetStatus(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();
        fall.setFailedBuchhaltungAuszahlungType(null);

        if (Objects.isNull(fall.getAuszahlung().getSapBusinessPartnerId())) {
            gesuch.setPendingSapAction(AUSZAHLUNG_INITIAL);
            if (
                Objects.nonNull(fall.getAuszahlung().getBuchhaltung())
                && fall.getAuszahlung().getBuchhaltung().getSapStatus() == SapStatus.IN_PROGRESS
            ) {
                return;
            }
            getUpdateOrCreateBusinessPartner(gesuch);
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

            var auszahlungsBetrag = relevantStipendienBuchhaltung.getSaldo() / 2;
            if (isPastSecondPaymentDate(gesuch)) {
                auszahlungsBetrag = relevantStipendienBuchhaltung.getSaldo();
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
        fall.setFailedBuchhaltungAuszahlungType(null);

        if (Objects.isNull(fall.getAuszahlung().getSapBusinessPartnerId())) {
            gesuch.setPendingSapAction(BuchhaltungType.AUSZAHLUNG_REMAINDER);
            if (
                Objects.nonNull(fall.getAuszahlung().getBuchhaltung())
                && fall.getAuszahlung().getBuchhaltung().getSapStatus() == SapStatus.IN_PROGRESS
            ) {
                return;
            }
            getUpdateOrCreateBusinessPartner(gesuch);
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
                    lastBuchhaltungEntry.getSaldo(),
                    BuchhaltungType.AUSZAHLUNG_REMAINDER
                );
        } else {
            relevantBuchhaltung = pendingAuszahlungOpt.get();
        }
        createVendorPostingOrGetStatus(gesuch, fall.getAuszahlung(), relevantBuchhaltung);
    }

    @Transactional(TxType.REQUIRES_NEW)
    void processPendingBusinessPartnerAction(
        final UUID gesuchId,
        final BuchhaltungType businessPartnerActionBuchhaltungType
    ) {
        doBusinessPartnerActionOrGetStatus(
            gesuchRepository.requireById(gesuchId),
            businessPartnerActionBuchhaltungType
        );
    }

    @Transactional(TxType.REQUIRES_NEW)
    void processPendingSapAction(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var fall = gesuch.getAusbildung().getFall();

        if (
            Objects.nonNull(fall.getAuszahlung().getBuchhaltung()) &&
            !fall.getAuszahlung().getBuchhaltung().getSapStatus().equals(SapStatus.SUCCESS)
        ) {
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

    public void processPendingBusinessPartnerActions() {
        final var pendingBusinessPartnerActionBuchhaltungs =
            buchhaltungRepository.findPendingBusinesspartnerActionBuchhaltung().toList();

        for (final var pendingBusinessPartnerActionBuchhaltung : pendingBusinessPartnerActionBuchhaltungs) {
            try {
                LOG.info(
                    String.format(
                        "Processing pendingBusinessPartnerCreateBuchhaltung: %s",
                        pendingBusinessPartnerActionBuchhaltung.getId()
                    )
                );
                final var gesuch = pendingBusinessPartnerActionBuchhaltung.getGesuch();
                switch (pendingBusinessPartnerActionBuchhaltung.getBuchhaltungType()) {
                    case BUSINESSPARTNER_CREATE, BUSINESSPARTNER_CHANGE -> processPendingBusinessPartnerAction(
                        gesuch.getId(),
                        pendingBusinessPartnerActionBuchhaltung.getBuchhaltungType()
                    );
                    case null, default -> throw new IllegalStateException(
                        "Invalid pending action: " + pendingBusinessPartnerActionBuchhaltung.getBuchhaltungType().name()
                    );
                }
            } catch (Exception e) {
                LOG.error(
                    String.format(
                        "processPendingBusinessPartnerActions: Error during processing of pendingBusinessPartnerActionBuchhaltung %s",
                        pendingBusinessPartnerActionBuchhaltung.getId()
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

    @Transactional(TxType.REQUIRES_NEW)
    void processRemainderAuszahlungAction(final UUID gesuchId) {
        createRemainderAuszahlungOrGetStatus(gesuchId);
    }

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
            .filter(Gesuch::isVerfuegt)
            .filter(this::isPastSecondPaymentDate)
            .forEach(gesuch -> {
                try {
                    processRemainderAuszahlungAction(
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
