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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.type.GesuchsperiodeSelectErrorType;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.api.datenschutzbrief.service.DatenschutzbriefService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentKommentarService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbGesucheDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuch.util.GesuchStatusUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.validation.EinnahmenKostenPageValidation;
import ch.dvbern.stip.api.gesuchhistory.repo.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheCopyService;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheStatusService;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheValidatorService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.notiz.service.GesuchNotizService;
import ch.dvbern.stip.api.notiz.type.GesuchNotizTyp;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp;
import ch.dvbern.stip.api.steuerdaten.validation.SteuerdatenPageValidation;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.zuordnung.service.ZuordnungService;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.EinreichedatumAendernRequestDto;
import ch.dvbern.stip.generated.dto.EinreichedatumStatusDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateResponseDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchNotizCreateDto;
import ch.dvbern.stip.generated.dto.GesuchNotizDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.GesuchZurueckweisenResponseDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeSelectErrorDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.PaginatedSbGesucheDashboardDto;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import ch.dvbern.stip.stipdecision.service.StipDecisionService;
import ch.dvbern.stip.stipdecision.type.StipDeciderResult;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.Constants.VERANLAGUNGSSTATUS_DEFAULT_VALUE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DOCUMENTS_NACHFRIST_NOT_FUTURE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_UNTERSCHRIFTENBLAETTER_NOT_PRESENT;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchService {
    private final GesuchRepository gesuchRepository;
    private final GesuchMapper gesuchMapper;
    private final GesuchTrancheMapper gesuchTrancheMapper;
    private final Validator validator;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchTrancheStatusService gesuchTrancheStatusService;
    private final GesuchsperiodenService gesuchsperiodeService;
    private final BenutzerService benutzerService;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final GesuchDokumentService gesuchDokumentService;
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final NotificationService notificationService;
    private final BerechnungService berechnungService;
    private final GesuchMapperUtil gesuchMapperUtil;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final GesuchTrancheService gesuchTrancheService;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final GesuchTrancheValidatorService gesuchTrancheValidatorService;
    private final GesuchNummerService gesuchNummerService;
    private final FallRepository fallRepository;
    private final FallDashboardItemMapper fallDashboardItemMapper;
    private final ConfigService configService;
    private final GesuchNotizService gesuchNotizService;
    private final SbDashboardQueryBuilder sbDashboardQueryBuilder;
    private final SbDashboardGesuchMapper sbDashboardGesuchMapper;
    private final AusbildungRepository ausbildungRepository;
    private final StipDecisionService stipDecisionService;
    private final ZuordnungService zuordnungService;
    private final StipDecisionTextRepository stipDecisionTextRepository;
    private final GesuchHistoryRepository gesuchHistoryRepository;
    private final UnterschriftenblattService unterschriftenblattService;
    private final BuchhaltungService buchhaltungService;
    private final MailService mailService;
    private final DokumentRepository dokumentRepository;
    private final GesuchDokumentKommentarService gesuchDokumentKommentarService;
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;
    private final GesuchDokumentKommentarHistoryRepository gesuchDokumentKommentarHistoryRepository;
    private final CustomDokumentTypRepository customDokumentTypRepository;
    private final VerfuegungService verfuegungService;
    private final StatusprotokollService statusprotokollService;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchTrancheCopyService gesuchTrancheCopyService;
    private final DatenschutzbriefService datenschutzbriefService;

    public Gesuch getGesuchById(final UUID gesuchId) {
        return gesuchRepository.requireById(gesuchId);
    }

    @Transactional
    public GesuchDto getGesuchGS(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(gesuchTrancheId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche, false);
    }

    @Transactional
    public GesuchWithChangesDto getGesuchSB(UUID gesuchId, UUID gesuchTrancheId) {
        final var actualGesuch = gesuchRepository.requireById(gesuchId);
        final var targetGueltigAb = gesuchTrancheService.getGesuchTrancheOrHistorical(gesuchTrancheId)
            .getGueltigkeit()
            .getGueltigAb();
        Optional<GesuchTranche> changes = Optional.empty();
        if (GesuchStatusUtil.sbReceivesChanges(actualGesuch)) {
            changes = gesuchTrancheHistoryRepository
                .getLatestWhereGesuchStatusChangedToVerfuegt(gesuchId, targetGueltigAb)
                .or(
                    () -> gesuchTrancheHistoryRepository
                        .getLatestWhereGesuchStatusChangedToEingereicht(gesuchId, targetGueltigAb)
                );
        }
        // bis eingereicht: changes: empty/null
        // ab eingereicht bis verfügt: tranche: db, changes: envers changedToEingereicht
        // ab verfügt: changes: empty/null
        return gesuchMapperUtil.toWithChangesDto(
            actualGesuch,
            gesuchTrancheRepository.requireById(gesuchTrancheId),
            changes.orElse(null),
            true
        );
    }

    @Transactional
    public void setAndValidateEinnahmenkostenUpdateLegality(
        final EinnahmenKostenUpdateDto einnahmenKostenUpdateDto,
        final GesuchTranche trancheToUpdate,
        final EinnahmenKosten einnahmenKostenToUpdate
    ) {
        final var benutzerRollenIdentifiers = benutzerService
            .getCurrentBenutzer()
            .getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());

        final var gesuchsjahr = trancheToUpdate.getGesuch().getGesuchsperiode().getGesuchsjahr();
        Integer steuerjahrToSet = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);

        String veranlagungsStatusToSet = VERANLAGUNGSSTATUS_DEFAULT_VALUE;

        if (einnahmenKostenToUpdate != null) {
            final Integer steuerjahrDtoValue = einnahmenKostenUpdateDto.getSteuerjahr();
            final Integer steuerjahrExistingValue = einnahmenKostenToUpdate.getSteuerjahr();
            final Integer steuerjahrDefaultValue = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
            steuerjahrToSet = ValidateUpdateLegalityUtil.getAndValidateLegalityValue(
                benutzerRollenIdentifiers,
                steuerjahrDtoValue,
                steuerjahrExistingValue,
                steuerjahrDefaultValue
            );

            final String veranlagungsStatusDtoValue = einnahmenKostenUpdateDto.getVeranlagungsStatus();
            final String veranlagungsStatusExistingValue = einnahmenKostenToUpdate.getVeranlagungsStatus();
            veranlagungsStatusToSet = ValidateUpdateLegalityUtil.getAndValidateLegalityValue(
                benutzerRollenIdentifiers,
                veranlagungsStatusDtoValue,
                veranlagungsStatusExistingValue,
                VERANLAGUNGSSTATUS_DEFAULT_VALUE
            );
        }
        einnahmenKostenUpdateDto.setSteuerjahr(steuerjahrToSet);
        einnahmenKostenUpdateDto.setVeranlagungsStatus(veranlagungsStatusToSet);
    }

    @Transactional
    public void updateGesuch(
        final UUID gesuchId,
        final GesuchUpdateDto gesuchUpdateDto,
        final boolean overrideIncomingVersteckteEltern
    ) throws ValidationsException {
        var gesuch = gesuchRepository.requireById(gesuchId);
        var trancheToUpdate = gesuch
            .getGesuchTrancheById(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId())
            .orElseThrow(NotFoundException::new);

        if (gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten() != null) {
            setAndValidateEinnahmenkostenUpdateLegality(
                gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten(),
                trancheToUpdate,
                trancheToUpdate.getGesuchFormular().getEinnahmenKosten()
            );
        }

        if (gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKostenPartner() != null) {
            setAndValidateEinnahmenkostenUpdateLegality(
                gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKostenPartner(),
                trancheToUpdate,
                trancheToUpdate.getGesuchFormular().getEinnahmenKostenPartner()
            );
        }

        updateGesuchTranche(
            gesuchUpdateDto.getGesuchTrancheToWorkWith(),
            trancheToUpdate,
            overrideIncomingVersteckteEltern
        );

        final var newFormular = trancheToUpdate.getGesuchFormular();
        gesuchTrancheService.removeSuperfluousDokumentsForGesuch(newFormular);

        final var updatePia = gesuchUpdateDto.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung();
        if (updatePia != null) {
            zuordnungService.updateZuordnungOnGesuch(gesuch);
        }
    }

    private void updateGesuchTranche(
        final GesuchTrancheUpdateDto trancheUpdate,
        final GesuchTranche trancheToUpdate,
        final boolean overrideIncomingVersteckteEltern
    ) {
        gesuchTrancheMapper.partialUpdate(trancheUpdate, trancheToUpdate, overrideIncomingVersteckteEltern);
        Set<ConstraintViolation<GesuchTranche>> violations = validator.validate(trancheToUpdate);
        if (!violations.isEmpty()) {
            throw new ValidationsException(ValidationsException.ENTITY_NOT_VALID_MESSAGE, violations);
        }
    }

    @Transactional
    public GesuchCreateResponseDto createGesuch(final GesuchCreateDto gesuchCreateDto) {
        final var result = createGesuchForAusbildung(gesuchCreateDto);
        if (result.getLeft() != null) {
            return new GesuchCreateResponseDto(result.getLeft().getId(), null);
        }

        LocalDate contextDate = null;
        if (result.getRight().getLeft() != null) {
            contextDate = result.getRight().getLeft().getAufschaltterminStart();
        }

        return new GesuchCreateResponseDto(
            null,
            new GesuchsperiodeSelectErrorDto(
                result.getRight().getRight(),
                contextDate
            )
        );
    }

    @Transactional
    public Pair<GesuchDto, Pair<Gesuchsperiode, GesuchsperiodeSelectErrorType>> createGesuchForAusbildung(
        GesuchCreateDto gesuchCreateDto
    ) {
        Gesuch gesuch = gesuchMapper.toNewEntity(gesuchCreateDto);
        gesuch.setAusbildung(ausbildungRepository.requireById(gesuch.getAusbildung().getId()));
        final var potential = gesuchsperiodeService.getGesuchsperiodeForAusbildung(
            gesuch.getAusbildung()
        );

        if (potential.getRight() != null) {
            return Pair.of(null, potential);
        }

        final var gesuchsperiode = potential.getLeft();

        gesuch.setGesuchsperiode(gesuchsperiode);
        createInitialGesuchTranche(gesuch);
        gesuch.setGesuchNummer(gesuchNummerService.createGesuchNummer(gesuch.getGesuchsperiode().getId()));
        gesuch.getAusbildung().getGesuchs().add(gesuch);
        Set<ConstraintViolation<Ausbildung>> violations = validator.validate(gesuch.getAusbildung());
        if (!violations.isEmpty()) {
            throw new ValidationsException(ValidationsException.ENTITY_NOT_VALID_MESSAGE, violations);
        }

        gesuchRepository.persistAndFlush(gesuch);
        statusprotokollService.createStatusprotokoll(
            Gesuchstatus.IN_BEARBEITUNG_GS.toString(),
            null,
            StatusprotokollEntryTyp.GESUCH,
            null,
            gesuch
        );

        return Pair.of(
            gesuchMapperUtil.mapWithTranche(
                gesuch,
                gesuch.getNewestGesuchTranche().orElseThrow(IllegalStateException::new),
                false
            ),
            null
        );
    }

    private void createInitialGesuchTranche(Gesuch gesuch) {
        var periode = gesuchsperiodeService
            .getGesuchsperiode(gesuch.getGesuchsperiode().getId())
            .orElseThrow(NotFoundException::new);

        var ausbildungsstart = gesuch
            .getAusbildung()
            .getAusbildungBegin()
            .withYear(periode.getGesuchsperiodeStart().getYear());
        if (ausbildungsstart.isAfter(periode.getGesuchsperiodeStopp())) {
            ausbildungsstart = ausbildungsstart.minusYears(1);
        }

        var tranche = new GesuchTranche()
            .setGueltigkeit(new DateRange(ausbildungsstart, ausbildungsstart.plusYears(1).minusDays(1)))
            .setGesuch(gesuch)
            .setGesuchFormular(new GesuchFormular())
            .setTyp(GesuchTrancheTyp.TRANCHE);

        tranche.getGesuchFormular().setTranche(tranche);

        gesuch.getGesuchTranchen().add(tranche);
    }

    @Transactional
    public PaginatedSbGesucheDashboardDto findGesucheSB(
        final GetGesucheSBQueryType queryType,
        final String fallNummer,
        final String piaNachname,
        final String piaVorname,
        final LocalDate piaGeburtsdatum,
        final String status,
        final String bearbeiter,
        final LocalDate letzteAktivitaetFrom,
        final LocalDate letzteAktivitaetTo,
        final GesuchTrancheTyp typ,
        final int page,
        final int pageSize,
        final SbGesucheDashboardColumn sortColumn,
        final SortOrder sortOrder
    ) {
        if (pageSize > configService.getMaxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var baseQuery = sbDashboardQueryBuilder.baseQuery(queryType, typ);

        if (fallNummer != null) {
            sbDashboardQueryBuilder.fallNummer(baseQuery, fallNummer);
        }

        if (piaNachname != null) {
            sbDashboardQueryBuilder.piaNachname(baseQuery, piaNachname);
        }

        if (piaVorname != null) {
            sbDashboardQueryBuilder.piaVorname(baseQuery, piaVorname);
        }

        if (piaGeburtsdatum != null) {
            sbDashboardQueryBuilder.piaGeburtsdatum(baseQuery, piaGeburtsdatum);
        }

        if (status != null) {
            sbDashboardQueryBuilder.status(baseQuery, typ, status);
        }

        if (bearbeiter != null) {
            sbDashboardQueryBuilder.bearbeiter(baseQuery, bearbeiter);
        }

        if (letzteAktivitaetFrom != null && letzteAktivitaetTo != null) {
            sbDashboardQueryBuilder.letzteAktivitaet(baseQuery, letzteAktivitaetFrom, letzteAktivitaetTo);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = sbDashboardQueryBuilder.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            sbDashboardQueryBuilder.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            sbDashboardQueryBuilder.defaultOrder(baseQuery);
        }

        sbDashboardQueryBuilder.paginate(baseQuery, page, pageSize);
        final var results = baseQuery.stream()
            .map(gesuch -> sbDashboardGesuchMapper.toDto(gesuch, typ))
            .toList();

        return new PaginatedSbGesucheDashboardDto(
            page,
            results.size(),
            Math.toIntExact(countQuery.fetchFirst()),
            results
        );
    }

    @Transactional
    public List<GesuchDto> findGesucheGs() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        return gesuchRepository.findForGs(benutzer.getId())
            .map(gesuch -> gesuchMapperUtil.mapWithNewestTranche(gesuch, false))
            .toList();
    }

    @Transactional
    public FallDashboardItemDto getFallDashboardItemDtos() {
        final var benutzer = benutzerService.getCurrentBenutzer();
        final var fall = fallRepository.findFallForGsOptional(benutzer.getId()).orElseThrow(NotFoundException::new);
        return fallDashboardItemMapper.toDto(fall);
    }

    @Transactional
    public FallDashboardItemDto getSozialdienstMitarbeiterFallDashboardItemDtos(UUID fallId) {
        final var fall = fallRepository.requireById(fallId);
        return fallDashboardItemMapper.toDto(fall);
    }

    public GesuchInfoDto getGesuchInfo(UUID gesuchId) {
        return gesuchMapper.toInfoDto(gesuchRepository.requireById(gesuchId));
    }

    @Transactional
    public void deleteGesuch(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var ausbildung = gesuch.getAusbildung();
        gesuchDokumentService.removeAllGesuchDokumentsForGesuch(gesuchId);
        notificationService.deleteNotificationsForFall(ausbildung.getFall().getId());
        buchhaltungService.deleteBuchhaltungsForGesuch(gesuchId);
        gesuchNotizService.deleteAllByGesuchId(gesuchId);
        statusprotokollService.deleteAllByGesuchId(gesuchId);
        gesuchRepository.delete(gesuch);
        ausbildung.getGesuchs().remove(gesuch);
        gesuch.getDatenschutzbriefs().clear();

        if (ausbildung.getGesuchs().isEmpty()) {
            ausbildungRepository.delete(ausbildung);
        }
    }

    @Transactional
    public void gesuchEinreichen(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuch.getGesuchTranchen().size() != 1) {
            throw new IllegalStateException("Gesuch kann only be eingereicht with exactly 1 Tranche");
        }

        final var gesuchTranche = gesuch.getGesuchTranchen().get(0);
        // No need to validate the entire Gesuch here, as it's done in the state machine
        gesuchTrancheValidatorService.validateAdditionalEinreichenCriteria(gesuchTranche);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.EINGEREICHT);
    }

    @Transactional
    public void setGesuchStatusToAnspruchPruefen(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.ANSPRUCH_PRUEFEN);
    }

    @Transactional
    public void stipendienAnspruchPruefen(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final var decision = getAnspruchDecision(gesuch);

        final var gesuchStatusChangeEvent = stipDecisionService.getGesuchStatusChangeEvent(decision);
        KommentarDto kommentarDto = null;
        if (decision != StipDeciderResult.GESUCH_VALID) {
            kommentarDto = new KommentarDto();
            kommentarDto.setText(
                stipDecisionService.getTextForDecision(
                    decision,
                    LocaleUtil.getKorrespondenzSprache(gesuch)
                )
            );
        }

        gesuchStatusService.triggerStateMachineEventWithComment(
            gesuch,
            gesuchStatusChangeEvent,
            kommentarDto,
            false
        );
    }

    @Transactional
    public StipDeciderResult getAnspruchDecision(final Gesuch gesuch) {
        final var gesuchTranchen = gesuch.getTranchenTranchen().toList();
        StipDeciderResult finalDecision = StipDeciderResult.GESUCH_VALID;

        for (GesuchTranche tranche : gesuchTranchen) {
            final var trancheDecision = stipDecisionService.decide(tranche);
            if (trancheDecision != StipDeciderResult.GESUCH_VALID) {
                finalDecision = trancheDecision;
                break;
            }
        }

        return finalDecision;
    }

    @Transactional
    public void bearbeitungAbschliessen(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final var stipendien = berechnungService.getBerechnungsresultatFromGesuch(
            gesuch,
            configService.getCurrentDmnMajorVersion(),
            configService.getCurrentDmnMinorVersion()
        );

        if (stipendien.getBerechnungTotal() <= 0) {
            // Keine Stipendien, next Status = Verfuegt
            gesuchStatusToVerfuegt(gesuchId);
        } else {
            // Yes Stipendien, next Status = In Freigabe
            gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.IN_FREIGABE);
        }
    }

    @Transactional
    public GesuchZurueckweisenResponseDto gesuchZurueckweisen(final UUID gesuchId, final KommentarDto kommentarDto) {
        // TODO KSTIP-1130: Juristische GesuchNotiz erstellen anhand Kommentar
        final var gesuch = gesuchRepository.requireById(gesuchId);
        var gesuchStatusChangeEvent = GesuchStatusChangeEvent.GESUCH_ZURUECKWEISEN;
        if (gesuch.isVerfuegt()) {
            var verfuegtGesuch = gesuchHistoryRepository
                .getLatestWhereStatusChangedToOneOf(gesuchId, Gesuchstatus.GESUCH_VERFUEGUNG_ABGESCHLOSSEN)
                .orElseThrow(NotFoundException::new);
            gesuchStatusChangeEvent =
                GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_STIPENDIENANSPRUCH;
            if (verfuegtGesuch.getGesuchStatus() == Gesuchstatus.KEIN_STIPENDIENANSPRUCH) {
                gesuchStatusChangeEvent =
                    GesuchStatusChangeEvent.GESUCH_AENDERUNG_ZURUECKWEISEN_KEIN_STIPENDIENANSPRUCH;
            }
        }
        gesuchStatusService
            .triggerStateMachineEventWithComment(gesuch, gesuchStatusChangeEvent, kommentarDto, true);

        // After zurueckweisen we now have only 1 GesuchTranche left, the Frontend should redirect there
        return new GesuchZurueckweisenResponseDto()
            .gesuchId(gesuchId)
            .gesuchTrancheId(gesuch.getGesuchTranchen().get(0).getId());
    }

    @Transactional
    public void juristischAbklaeren(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG);
    }

    @Transactional
    public void gesuchStatusToInBearbeitung(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.IN_BEARBEITUNG_SB);
    }

    @Transactional
    public void gesuchStatusToBereitFuerBearbeitung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        var changeEvent = GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG;
        if (
            gesuch.getGesuchStatus() == Gesuchstatus.JURISTISCHE_ABKLAERUNG && !haveAllDatenschutzbriefeBeenSent(gesuch)
        ) {
            changeEvent = GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT;
        }

        gesuchStatusService.triggerStateMachineEvent(
            gesuch,
            changeEvent
        );
    }

    @Transactional
    public void gesuchStatusToDatenschutzbriefDruckbereit(final UUID gesuchId, final KommentarDto kommentar) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEventWithComment(
            gesuch,
            GesuchStatusChangeEvent.DATENSCHUTZBRIEF_DRUCKBEREIT,
            kommentar,
            false
        );
    }

    @Transactional
    public void gesuchStatusToVerfuegt(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        verfuegungService.createVerfuegung(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.VERFUEGT);
    }

    @Transactional
    public void gesuchStatusCheckUnterschriftenblatt(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        if (gesuch.getGesuchStatus() != Gesuchstatus.VERFUEGT) {
            return;
        }

        if (unterschriftenblattService.requiredUnterschriftenblaetterExistOrWasAlreadyVerfuegtOnceBefore(gesuch)) {
            gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT);
        } else {
            gesuchStatusService.triggerStateMachineEvent(
                gesuch,
                GesuchStatusChangeEvent.WARTEN_AUF_UNTERSCHRIFTENBLATT
            );
        }
    }

    @Transactional
    public void changeGesuchStatusToVerfuegungDruckbereit(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.VERFUEGUNG_DRUCKBEREIT);
    }

    @Transactional
    public void gesuchStatusToStipendienanspruch(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        var relevantVerfuegung = gesuch.getVerfuegungs()
            .stream()
            .max(Comparator.comparing(Verfuegung::getTimestampErstellt))
            .orElseThrow(NotFoundException::new);

        var status = GesuchStatusChangeEvent.KEIN_STIPENDIENANSPRUCH;

        if (Objects.isNull(relevantVerfuegung.getStipDecision())) {
            final var stipendien = berechnungService.getBerechnungsresultatFromGesuch(
                gesuch,
                configService.getCurrentDmnMajorVersion(),
                configService.getCurrentDmnMinorVersion()
            );

            if (stipendien.getBerechnungTotal() > 0) {
                status = GesuchStatusChangeEvent.STIPENDIENANSPRUCH;
            }
        }

        gesuchStatusService.triggerStateMachineEvent(gesuch, status);
    }

    @Transactional
    public void gesuchFehlendeDokumenteUebermitteln(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        ValidatorUtil.throwIfEntityNotValid(validator, gesuch);
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE);
    }

    @Transactional
    public void changeGesuchStatusToNegativeVerfuegungWithDecision(
        final UUID gesuchId,
        final AusgewaehlterGrundDto ausgewaehlterGrundDto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var decisionId = ausgewaehlterGrundDto.getDecisionId();
        var decision = stipDecisionTextRepository.requireById(decisionId);
        verfuegungService
            .createNegativeVerfuegungWithDecision(
                gesuchId,
                decisionId,
                Optional.ofNullable(ausgewaehlterGrundDto.getKanton())
            );
        var kommentarTxt = decision.getTitleDe();
        var kommentarDto = new KommentarDto(kommentarTxt);
        gesuchStatusService.triggerStateMachineEventWithComment(
            gesuch,
            GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG,
            kommentarDto,
            false
        );
    }

    @Transactional
    public void changeGesuchStatusToNegativeVerfuegungManuell(
        final UUID gesuchId,
        final FileUpload fileUpload,
        final String kommentar
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final Locale locale = LocaleUtil.getLocale(gesuch);
        final TL translator = TLProducer.defaultBundle().forAppLanguage(AppLanguages.fromLocale(locale));

        KommentarDto kommentarDto;
        if (kommentar.isBlank()) {
            kommentarDto = new KommentarDto(translator.translate("stip.verfuegung.manuell"));
        } else {
            kommentarDto = new KommentarDto(translator.translate("stip.verfuegung.manuell") + ", " + kommentar);
        }

        verfuegungService.createNegativeVerfuegungManuell(gesuchId, fileUpload);

        gesuchStatusService.triggerStateMachineEventWithComment(
            gesuch,
            GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG,
            kommentarDto,
            false
        );
    }

    @Transactional
    public void changeGesuchStatusToVersandbereit(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var latestVerfuegung = getLatestVerfuegungForGesuch(gesuchId);

        if (
            !latestVerfuegung.isNegativeVerfuegung()
            && !unterschriftenblattService.areRequiredUnterschriftenblaetterUploaded(gesuch)
        ) {
            throw new CustomValidationsException(
                "Required Unterschriftenblaetter are not uploaded",
                new CustomConstraintViolation(
                    VALIDATION_UNTERSCHRIFTENBLAETTER_NOT_PRESENT,
                    "unterschriftenblaetter"
                )
            );
        }

        gesuchStatusService.triggerStateMachineEvent(
            gesuch,
            GesuchStatusChangeEvent.VERFUEGUNG_VERSANDBEREIT
        );
    }

    @Transactional
    public List<GesuchDokumentDto> getGesuchDokumenteForGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        return gesuch
            .getGesuchTranchen()
            .stream()
            .filter(
                gesuchTranche -> !(gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG
                && gesuchTranche.getStatus() != GesuchTrancheStatus.UEBERPRUEFEN)
            )
            .flatMap(
                gesuchTranche -> gesuchDokumentRepository.findAllForGesuchTranche(gesuchTranche.getId())
            )
            .map(gesuchDokumentMapper::toDto)
            .toList();
    }

    @Transactional
    public void updateNachfristDokumente(final UUID gesuchId, LocalDate nachfristDokumente) {
        if (nachfristDokumente.isBefore(LocalDate.now())) {
            throw new CustomValidationsException(
                "Nachfrist muss in der Zukunft liegen",
                new CustomConstraintViolation(
                    VALIDATION_DOCUMENTS_NACHFRIST_NOT_FUTURE,
                    "nachfristDokumente"
                )
            );
        }
        var gesuch = gesuchRepository.requireById(gesuchId);
        gesuch.setNachfristDokumente(nachfristDokumente);
        notificationService.createGesuchNachfristDokumenteChangedNotification(gesuch);
    }

    public BerechnungsresultatDto getBerechnungsresultat(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);
    }

    public Verfuegung getLatestVerfuegungForGesuch(final UUID gesuchId) {
        return verfuegungService.getLatestVerfuegung(gesuchId);
    }

    @Transactional
    public GesuchWithChangesDto getChangesByInitialTrancheId(UUID trancheId) {
        final var tranche = gesuchTrancheHistoryService.getLatestTranche(trancheId);
        final var gesuch = tranche.getGesuch();

        final var requestedTrancheFromGesuchInStatusEingereicht =
            gesuchHistoryRepository.getLatestWhereStatusChangedTo(gesuch.getId(), Gesuchstatus.EINGEREICHT)
                .orElseThrow(ForbiddenException::new)
                .getGesuchTranchen()
                .stream()
                .filter(trancheToFind -> trancheToFind.getId().equals(trancheId))
                .findFirst();

        var requestedTrancheFromGesuchInStatusVerfuegt =
            gesuchHistoryRepository.getFirstWhereStatusChangedTo(gesuch.getId(), Gesuchstatus.VERFUEGT)
                .orElseThrow(NotFoundException::new)
                .getGesuchTranchen()
                .stream()
                .filter(trancheToFind -> trancheToFind.getId().equals(trancheId))
                .findFirst()
                .orElseThrow(BadRequestException::new);

        return gesuchMapperUtil.toWithChangesDto(
            requestedTrancheFromGesuchInStatusVerfuegt.getGesuch(),
            // tranche to work with -> findByTrancheId
            requestedTrancheFromGesuchInStatusVerfuegt,
            // changes
            requestedTrancheFromGesuchInStatusEingereicht.orElse(null),
            // make sure this flag is true whenever especially this endpoint is called
            true,
            // this is the implementation of an SB Endpoint, send versteckte Eltern
            true
        );
    }

    @Transactional
    public GesuchWithChangesDto getGsTrancheChangesInBearbeitung(final UUID aenderungId) {
        var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);

        final var statesWhereCurrentIsReturned = List.of(
            GesuchTrancheStatus.IN_BEARBEITUNG_GS,
            GesuchTrancheStatus.FEHLENDE_DOKUMENTE
        );
        if (!statesWhereCurrentIsReturned.contains(aenderung.getStatus())) {
            aenderung = gesuchTrancheHistoryRepository.getLatestWhereStatusChangedToUeberpruefen(aenderungId);
        }

        final var initialRevision = gesuchTrancheHistoryRepository.getInitialRevision(aenderungId);
        return gesuchMapperUtil.toWithChangesDto(aenderung.getGesuch(), aenderung, initialRevision, false);
    }

    @Transactional
    public GesuchWithChangesDto getSbTrancheChanges(final UUID aenderungId) {
        final var aenderung = gesuchTrancheRepository.requireAenderungById(aenderungId);
        final var initialRevision = gesuchTrancheHistoryRepository.getInitialRevision(aenderungId);
        final var latestWhereStatusChanged =
            gesuchTrancheHistoryRepository.getLatestWhereStatusChangedToUeberpruefen(aenderungId);
        return gesuchMapperUtil.toWithChangesDto(
            aenderung.getGesuch(),
            aenderung,
            List.of(initialRevision, latestWhereStatusChanged)
        );
    }

    @Transactional
    public GesuchWithChangesDto getSbTrancheChangesWithRevision(final UUID aenderungId, final Integer revision) {
        final var gesuch = gesuchTrancheRepository.requireAenderungById(aenderungId).getGesuch();
        final var initialRevision = gesuchTrancheHistoryRepository.getInitialRevision(aenderungId);
        final var aenderung = gesuchTrancheHistoryRepository.getByRevisionId(aenderungId, revision);
        return gesuchMapperUtil.toWithChangesDto(
            gesuch,
            aenderung,
            List.of(initialRevision)
        );
    }

    @Transactional
    public GesuchDto gesuchFehlendeDokumenteEinreichen(final UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        ValidatorUtil.throwIfEntityNotValid(validator, gesuchTranche);
        gesuchStatusService.triggerStateMachineEvent(
            gesuchTranche.getGesuch(),
            GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_EINREICHEN
        );
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche, false);
    }

    @Transactional
    public GesuchNotizDto createGesuchNotiz(final GesuchNotizCreateDto createDto) {
        final var gesuch = gesuchRepository.requireById(createDto.getGesuchId());
        final var notiz = gesuchNotizService.create(gesuch, createDto);

        if (createDto.getNotizTyp() == GesuchNotizTyp.JURISTISCHE_NOTIZ) {
            juristischAbklaeren(gesuch.getId());
        }

        return notiz;
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void checkForFehlendeDokumenteOnAllGesuche() {
        final var gesuchsToCheck = gesuchRepository.getAllFehlendeDokumente();
        final var toUpdate = gesuchsToCheck
            .stream()
            .filter(gesuch -> gesuch.getNachfristDokumente().isBefore(LocalDate.now()))
            .toList();
        final var toUpdateEingereicht = toUpdate.stream().filter(gesuch -> !gesuch.isVerfuegt()).toList();
        final var toUpdateVerfuegt = toUpdate.stream().filter(Gesuch::isVerfuegt).toList();

        if (!toUpdateEingereicht.isEmpty()) {
            gesuchStatusService.bulkTriggerStateMachineEvent(
                toUpdateEingereicht,
                GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT
            );
        }
        if (!toUpdateVerfuegt.isEmpty()) {
            gesuchStatusService.bulkTriggerStateMachineEvent(
                toUpdateVerfuegt,
                GesuchStatusChangeEvent.AENDERUNG_FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT
            );
        }
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void checkForFehlendeDokumenteOnAllAenderungen() {
        final var gesuchTranchenToCheck = gesuchTrancheRepository.getAllFehlendeDokumente();

        final var toUpdate = gesuchTranchenToCheck
            .stream()
            .filter(gesuchTranche -> gesuchTranche.getGesuch().getNachfristDokumente().isBefore(LocalDate.now()))
            .toList();
        if (!toUpdate.isEmpty()) {
            gesuchTrancheStatusService.bulkTriggerStateMachineEvent(
                toUpdate,
                GesuchTrancheStatusChangeEvent.IN_BEARBEITUNG_GS
            );
        }
    }

    @Transactional
    public GesuchDto einreichedatumManuellAendern(
        final UUID gesuchId,
        final EinreichedatumAendernRequestDto dto
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        gesuch.setEinreichedatum(dto.getNewEinreichedatum());
        gesuchNotizService.createGesuchNotiz(gesuch, dto.getBetreff(), dto.getText());

        return gesuchMapperUtil.mapWithNewestTranche(gesuch, true);
    }

    @Transactional
    public EinreichedatumStatusDto canUpdateEinreichedatum(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return new EinreichedatumStatusDto(canUpdateEinreichedatum(gesuch));
    }

    @Transactional
    public boolean canUpdateEinreichedatum(final Gesuch gesuch) {
        final var currentBenutzer = benutzerService.getCurrentBenutzer();

        return currentBenutzer
            .hasOneOfRoles(Set.of(OidcConstants.ROLE_SACHBEARBEITER_ADMIN, OidcConstants.ROLE_SACHBEARBEITER))
        && Gesuchstatus.SACHBEARBEITER_CAN_EDIT.contains(gesuch.getGesuchStatus()) && !gesuch.isVerfuegt();
    }

    public Optional<Gesuch> getLatestEingereichtVersion(final UUID gesuchId) {
        return gesuchHistoryRepository.getLatestWhereStatusChangedTo(gesuchId, Gesuchstatus.EINGEREICHT);
    }

    public void sendFehlendeDokumenteNotifications(Gesuch gesuch) {
        notificationService.createMissingDocumentNotification(gesuch);
        mailService.sendStandardNotificationEmailForGesuch(gesuch);
    }

    public void setDefaultNachfristDokumente(Gesuch gesuch) {
        if (Objects.isNull(gesuch.getNachfristDokumente())) {
            gesuch.setNachfristDokumente(
                LocalDate.now().plusDays(gesuch.getGesuchsperiode().getFristNachreichenDokumente())
            );
        }
    }

    @Transactional
    public void resetGesuchTrancheToTranche(final GesuchTranche fromTranche, final GesuchTranche toTranche) {
        final var formularOfFromTranche = fromTranche.getGesuchFormular();

        gesuchDokumentKommentarService.deleteForGesuchTrancheId(toTranche.getId());

        toTranche.setGueltigkeit(fromTranche.getGueltigkeit());

        gesuchTrancheCopyService.overrideGesuchFormular(toTranche.getGesuchFormular(), formularOfFromTranche);

        // Dokumente
        // Remove doks that exist now but didn't exist then (i.e. past)
        final var dokumentIdsNow = toTranche
            .getGesuchDokuments()
            .stream()
            .flatMap(gesuchDokument -> gesuchDokument.getDokumente().stream().map(Dokument::getId))
            .toList();

        final var dokumentIdsThen = fromTranche
            .getGesuchDokuments()
            .stream()
            .flatMap(gesuchDokument -> gesuchDokument.getDokumente().stream().map(Dokument::getId))
            .toList();

        final var dokumentIdsNowButNotThen = dokumentIdsNow.stream().filter(s -> !dokumentIdsThen.contains(s)).toList();

        dokumentIdsNowButNotThen.forEach(gesuchDokumentService::deleteDokument);

        // Remove doks that existed then (i.e. past) but not now
        toTranche
            .getGesuchDokuments()
            .removeIf(gesuchDokument -> !fromTranche.getGesuchDokuments().contains(gesuchDokument));

        final var targetGesuchDokumente = toTranche.getGesuchDokuments();

        for (var sourceGesuchDokument : fromTranche.getGesuchDokuments()) {
            if (targetGesuchDokumente.contains(sourceGesuchDokument)) {
                final var replacement = targetGesuchDokumente
                    .stream()
                    .filter(gesuchDokument -> sourceGesuchDokument.getId().equals(gesuchDokument.getId()))
                    .findFirst();
                replacement.ifPresent(gesuchDokument -> {
                    GesuchDokumentCopyUtil.copyValues(sourceGesuchDokument, gesuchDokument, toTranche);
                    if (Objects.nonNull(gesuchDokument.getCustomDokumentTyp())) {
                        customDokumentTypRepository.persist(gesuchDokument.getCustomDokumentTyp());
                    }
                    gesuchDokumentRepository.persist(gesuchDokument);
                    sourceGesuchDokument
                        .getDokumente()
                        .forEach(dokument -> {
                            if (!gesuchDokument.getDokumente().contains(dokument)) {
                                final var newDokument = new Dokument();
                                GesuchDokumentCopyUtil.copyValues(dokument, newDokument);
                                gesuchDokument.addDokument(newDokument);
                                dokumentRepository.persist(newDokument);
                            }
                        });
                });
            } else {
                final var newGesuchDokument = GesuchDokumentCopyUtil.createCopy(sourceGesuchDokument, toTranche);
                gesuchDokumentRepository.persist(newGesuchDokument);
                sourceGesuchDokument
                    .getDokumente()
                    .forEach(dokument -> {
                        final var newDokument = new Dokument();
                        GesuchDokumentCopyUtil.copyValues(dokument, newDokument);
                        newGesuchDokument.addDokument(newDokument);
                        dokumentRepository.persist(newDokument);
                    });
            }
        }
    }

    @Transactional
    public void resetGesuchZurueckweisenToEingereicht(Gesuch gesuch) {
        final var gesuchOfStateEingereicht = getLatestEingereichtVersion(gesuch.getId())
            .orElseThrow(NotFoundException::new);

        if (gesuchOfStateEingereicht.getGesuchTranchen().size() != 1) {
            throw new IllegalStateException("Trying to reset to a Gesuch which has more than 1 Tranchen");
        }

        final var trancheOfStateEingereicht = gesuchOfStateEingereicht.getGesuchTranchen().get(0);

        final var trancheToReset = gesuch
            .getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getId().equals(trancheOfStateEingereicht.getId()))
            .findFirst()
            .orElseGet(gesuch::getLatestGesuchTranche);

        datenschutzbriefService.deleteDatenschutzbriefeOfGesuch(gesuch.getId());

        resetGesuchTrancheToTranche(trancheOfStateEingereicht, trancheToReset);
        if (gesuch.hasNeverBeenVerfuegt()) {
            gesuchTrancheCopyService
                .overrideAusbildung(gesuchOfStateEingereicht.getAusbildung(), gesuch.getAusbildung());
        }

        final var allOtherTranchen = gesuch
            .getGesuchTranchen()
            .stream()
            .filter(tranche -> !tranche.getId().equals(trancheToReset.getId()))
            .toList();

        for (final var trancheToDrop : allOtherTranchen) {
            gesuchTrancheService.dropGesuchTranche(trancheToDrop);
        }
    }

    @Transactional
    public void resetGesuchZurueckweisenToVerfuegt(Gesuch gesuch) {
        final var tranchenIdsToDrop = doResetGesuchZurueckweisenToEingereicht(gesuch.getId());
        for (final var trancheIdToDrop : tranchenIdsToDrop) {
            gesuchTrancheService.dropGesuchTranche(trancheIdToDrop);
        }
    }

    @Transactional
    public List<UUID> doResetGesuchZurueckweisenToEingereicht(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var relevantAenderungId = gesuch
            .getGesuchTranchen()
            .stream()
            .filter(gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG)
            .max(Comparator.comparing(GesuchTranche::getTimestampErstellt))
            .orElseThrow(NotFoundException::new)
            .getId();

        final Integer revisionToResetTo =
            gesuchTrancheHistoryRepository
                .getEarliestRevisionWhereStatusChangedTo(relevantAenderungId, GesuchTrancheStatus.UEBERPRUEFEN)
                .orElseThrow()
            - 1;

        // Select the gesuch just before it changes from STIPENDIENANSPRUCH/KEIN_STIPENDIENANSPRUCH to IN_BEARBEITUNG_SB
        final var gesuchToRevertTo = gesuchHistoryRepository
            .getGesuchAtRevision(gesuch.getId(), revisionToResetTo)
            .orElseThrow(NotFoundException::new);

        Map<UUID, List<GesuchDokumentKommentar>> trancheIdGesuchDokumentKommentarsMap = new HashMap<>();

        // We need to fetch comments before making changes to the gesuch as otherwise hibernate would commit those
        // changes at the getGesuchDokumentKommentarOfGesuchDokumentAtRevision calls
        for (var gesuchTranche : gesuchToRevertTo.getGesuchTranchen()) {
            List<GesuchDokumentKommentar> gesuchDokumentKommentars = gesuchTranche
                .getGesuchDokuments()
                .stream()
                .flatMap(
                    gesuchDokument -> gesuchDokumentKommentarHistoryRepository
                        .getGesuchDokumentKommentarOfGesuchDokumentAtRevision(gesuchDokument.getId(), revisionToResetTo)
                        .stream()
                )
                .toList();
            trancheIdGesuchDokumentKommentarsMap.put(gesuchTranche.getId(), gesuchDokumentKommentars);
        }

        final var tranchenIdsToDrop = gesuch.getGesuchTranchen().stream().map(AbstractEntity::getId).toList();

        for (var gesuchTrancheToRevertTo : gesuchToRevertTo.getGesuchTranchen()) {
            final var newTranche = gesuchTrancheCopyService.copyTrancheExceptGesuchDokuments(
                gesuchTrancheToRevertTo,
                gesuchTrancheToRevertTo.getGueltigkeit(),
                gesuchTrancheToRevertTo.getComment()
            );

            newTranche.setGesuch(gesuch);
            newTranche.setStatus(gesuchTrancheToRevertTo.getStatus());
            newTranche.setTyp(gesuchTrancheToRevertTo.getTyp());

            gesuch.getGesuchTranchen().add(newTranche);

            gesuchTrancheRepository.persist(newTranche);

            for (var sourceGesuchDokument : gesuchTrancheToRevertTo.getGesuchDokuments()) {
                final var newGesuchDokument = GesuchDokumentCopyUtil.createCopy(sourceGesuchDokument, newTranche);
                newTranche.getGesuchDokuments().add(newGesuchDokument);
                if (Objects.nonNull(newGesuchDokument.getCustomDokumentTyp())) {
                    newGesuchDokument.getCustomDokumentTyp().setGesuchDokument(newGesuchDokument);
                }
                gesuchDokumentRepository.persist(newGesuchDokument);
                sourceGesuchDokument
                    .getDokumente()
                    .forEach(dokument -> {
                        final var newDokument = new Dokument();
                        GesuchDokumentCopyUtil.copyValues(dokument, newDokument);
                        newGesuchDokument.addDokument(newDokument);
                        dokumentRepository.persist(newDokument);
                    });
            }

            gesuchDokumentKommentarService.copyKommentareToTranche(
                trancheIdGesuchDokumentKommentarsMap.get(gesuchTrancheToRevertTo.getId()),
                newTranche
            );
        }

        gesuch.setNachfristDokumente(gesuchToRevertTo.getNachfristDokumente());
        gesuch.setEinreichedatum(gesuchToRevertTo.getEinreichedatum());

        gesuchRepository.persistAndFlush(gesuch);

        return tranchenIdsToDrop;
    }

    @Transactional
    public void resetGesuchZurueckweisen(Gesuch gesuch) {
        if (gesuch.isVerfuegt()) {
            resetGesuchZurueckweisenToVerfuegt(gesuch);
        } else {
            resetGesuchZurueckweisenToEingereicht(gesuch);
        }
    }

    @Transactional
    public GesuchDto setGesuchsperiodeForGesuch(final UUID gesuchTrancheId, final UUID gesuchsperiodeId) {
        final var gesuchsperiode = gesuchsperiodeRepository.findByIdOptional(gesuchsperiodeId).orElseThrow();

        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        final var gesuch = gesuchTranche.getGesuch();
        final var potentialGesuchsperioden = gesuchsperiodeService.getAllAssignableGesuchsperioden(gesuch.getId());

        final var foundGesuchsperiode = potentialGesuchsperioden.stream()
            .filter(potentialGesuchsperiode -> potentialGesuchsperiode.getId().equals(gesuchsperiodeId))
            .findFirst();

        if (foundGesuchsperiode.isEmpty()) {
            throw new BadRequestException("Gesuchsperiode is not assignable");
        }

        gesuch.setGesuchsperiode(gesuchsperiode);

        ValidatorUtil
            .validate(
                validator,
                gesuchTranche.getGesuchFormular(),
                List.of(EinnahmenKostenPageValidation.class, SteuerdatenPageValidation.class)
            );

        if (gesuch.getGesuchTranchen().size() != 1) {
            LOG.error(
                "Tried to reassign Gesuchsperiode for Gesuch with id {} that has more than 1 tranche",
                gesuch.getId()
            );
            throw new BadRequestException("Tried to reassign Gesuchsperiode for Gesuch with more than 1 tranche");
        }

        final var tranche = gesuch.getGesuchTranchen().getFirst();
        final var oldGueltigkeit = tranche.getGueltigkeit();
        final var newGueltigkeit = new DateRange(
            oldGueltigkeit.getGueltigAb().withYear(gesuchsperiode.getGesuchsperiodeStart().getYear()),
            oldGueltigkeit.getGueltigBis().withYear(gesuchsperiode.getGesuchsperiodeStopp().getYear())
        );

        tranche.setGueltigkeit(newGueltigkeit);

        gesuchRepository.persistAndFlush(gesuch);

        return gesuchMapperUtil.mapWithTranche(gesuch, gesuchTranche, true);
    }

    public boolean haveAllDatenschutzbriefeBeenSent(final Gesuch gesuch) {
        return gesuch.getDatenschutzbriefs().stream().allMatch(Datenschutzbrief::isVersendet);
    }

    @Transactional
    public void changeToVersendentAndAnspruchOrKeinAnspruch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        bulkChangeToVersendentAndAnspruchOrKeinAnspruch(List.of(gesuch));
    }

    @Transactional
    public void bulkChangeToVersendentAndAnspruchOrKeinAnspruch(final List<Gesuch> gesuche) {
        gesuchStatusService.bulkTriggerStateMachineEvent(gesuche, GesuchStatusChangeEvent.VERFUEGUNG_VERSENDET);

        final var anspruch = new ArrayList<Gesuch>();
        final var keinAnspruch = new ArrayList<Gesuch>();
        for (final var gesuch : gesuche) {
            final var latestVerfuegung = verfuegungService.getLatestVerfuegung(gesuch.getId());
            if (latestVerfuegung.isNegativeVerfuegung()) {
                keinAnspruch.add(gesuch);
            } else {
                anspruch.add(gesuch);
            }
        }

        gesuchStatusService.bulkTriggerStateMachineEvent(anspruch, GesuchStatusChangeEvent.STIPENDIENANSPRUCH);
        gesuchStatusService.bulkTriggerStateMachineEvent(keinAnspruch, GesuchStatusChangeEvent.KEIN_STIPENDIENANSPRUCH);
    }
}
