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

package ch.dvbern.stip.api.gesuchsperioden.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
import ch.dvbern.stip.api.common.validation.ValidationsConstant;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCH_NO_VALID_GESUCHSPERIODE;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchsperiodenService {
    private static final String PROPERTY_PATH = "gesuchsperiode";
    private final GesuchsperiodeMapper gesuchsperiodeMapper;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchsjahrRepository gesuchsjahrRepository;

    @Transactional
    public GesuchsperiodeWithDatenDto createGesuchsperiode(final GesuchsperiodeCreateDto createDto) {
        final var newEntity = gesuchsperiodeMapper.toEntity(createDto);
        newEntity.setGesuchsjahr(gesuchsjahrRepository.requireById(newEntity.getGesuchsjahr().getId()));
        gesuchsperiodeRepository.persistAndFlush(newEntity);
        return gesuchsperiodeMapper.toDatenDto(newEntity);
    }

    @Transactional
    public GesuchsperiodeWithDatenDto updateGesuchsperiode(
        final UUID gesuchsperiodeId,
        final GesuchsperiodeUpdateDto updateDto
    ) {
        final var gesuchsperiode = gesuchsperiodeRepository.requireById(gesuchsperiodeId);
        preventUpdateIfReadonly(gesuchsperiode);
        gesuchsperiodeMapper.partialUpdate(updateDto, gesuchsperiode);
        gesuchsperiode.setGesuchsjahr(gesuchsjahrRepository.requireById(gesuchsperiode.getGesuchsjahr().getId()));
        return gesuchsperiodeMapper.toDatenDto(gesuchsperiode);
    }

    public List<GesuchsperiodeDto> getAllGesuchsperioden() {
        return this.gesuchsperiodeRepository.findAll()
            .stream()
            .map(gesuchsperiodeMapper::toDto)
            .toList();
    }

    public Optional<GesuchsperiodeWithDatenDto> getGesuchsperiode(final UUID id) {
        final var gesuchsperiode = gesuchsperiodeRepository.findByIdOptional(id);
        return gesuchsperiode.map(gesuchsperiodeMapper::toDatenDto);
    }

    public Gesuchsperiode getGesuchsperiodeForAusbildung(final Ausbildung ausbildung) {
        final var ausbildungBegin = ausbildung.getAusbildungBegin();

        for (int yearOffset = 1; yearOffset >= -1; yearOffset--) {
            var ausbildungsBeginAssumed = ausbildungBegin.withYear(LocalDate.now().getYear() + yearOffset);

            if (ausbildungsBeginAssumed.isBefore(ausbildungBegin)) {
                break;
            }

            var eligibleGesuchsperiode =
                gesuchsperiodeRepository.findAllStartBeforeOrAt(ausbildungsBeginAssumed);

            checkGesuchperiodeGueltigkeit(eligibleGesuchsperiode);

            if (
                eligibleGesuchsperiode
                    .getGesuchsperiodeStart()
                    .plusMonths(6)
                    .isAfter(ausbildungsBeginAssumed)
            ) {
                return eligibleGesuchsperiode;
            }
        }

        throw new CustomValidationsException(
            "No valid gesuchsperiode found for the ausbildungsbegin provided",
            new CustomConstraintViolation(
                VALIDATION_GESUCH_NO_VALID_GESUCHSPERIODE,
                PROPERTY_PATH
            )
        );
    }

    private void checkGesuchperiodeGueltigkeit(final Gesuchsperiode eligibleGesuchsperiode) {
        if (
            Objects.isNull(eligibleGesuchsperiode)
            || eligibleGesuchsperiode.getGueltigkeitStatus() == GueltigkeitStatus.ARCHIVIERT
        ) {
            throw new CustomValidationsException(
                "Es ist keine aktive Gesuchsperiode fuer diesen Ausbildungsbeginn vorhanden",
                new CustomConstraintViolation(
                    ValidationsConstant.VALIDATION_NO_ACTIVE_GESUCHSPERIODE,
                    PROPERTY_PATH
                )
            );
        }

        if (eligibleGesuchsperiode.getGueltigkeitStatus() == GueltigkeitStatus.ENTWURF) {
            final var errorMessage = String.format(
                    "Die Gesuchsperiode fuer diesen Ausbildungsbeginn wird voraussichtlich am %s geoeffnet",
                    eligibleGesuchsperiode.getAufschaltterminStart().toString()
                );
            throw new CustomValidationsException(
                errorMessage,
                new CustomConstraintViolation(
                    ValidationsConstant.VALIDATION_GESUCHSPERIODE_IN_STATUS_DRAFT,
                    PROPERTY_PATH
                )
            );

        }
        if (
            eligibleGesuchsperiode.getGueltigkeitStatus() == GueltigkeitStatus.PUBLIZIERT
            && eligibleGesuchsperiode.getAufschaltterminStart().isAfter(LocalDate.now())
        ) {
            final var errorMessage = String.format(
                    "Die Gesuchsperiode fuer diesen Ausbildungsbeginn wird am %s geoeffnet",
                    eligibleGesuchsperiode.getAufschaltterminStart().toString()
                );
            throw new CustomValidationsException(
                errorMessage,
                new CustomConstraintViolation(
                    ValidationsConstant.VALIDATION_GESUCHSPERIODE_INACTIVE,
                    PROPERTY_PATH
                )
            );
        }
    }

    @Transactional
    public void deleteGesuchsperiode(final UUID gesuchsperiodeId) {
        final var gesuchsperiode = gesuchsperiodeRepository.requireById(gesuchsperiodeId);
        preventUpdateIfReadonly(gesuchsperiode);
        gesuchsperiodeRepository.delete(gesuchsperiode);
    }

    @Transactional
    public GesuchsperiodeWithDatenDto publishGesuchsperiode(final UUID gesuchsperiodeId) {
        final var gesuchsperiode = gesuchsperiodeRepository.requireById(gesuchsperiodeId);
        preventUpdateIfReadonly(gesuchsperiode);
        gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT);
        return gesuchsperiodeMapper.toDatenDto(gesuchsperiode);
    }

    public GesuchsperiodeWithDatenDto getLatest() {
        final var found = gesuchsperiodeRepository.getLatest();
        return found.map(gesuchsperiodeMapper::toDatenDto).orElse(null);
    }

    public boolean isReadonly(final Gesuchsperiode gesuchsperiode) {
        return gesuchsperiode.getGueltigkeitStatus() != GueltigkeitStatus.ENTWURF;
    }

    private void preventUpdateIfReadonly(final Gesuchsperiode gesuchsperiode) {
        if (isReadonly(gesuchsperiode)) {
            throw new IllegalStateException("Cannot update Gesuchsperiode if it is started");
        }
    }

    @Transactional
    public void setOutdatedGesuchsperiodenToArchiviert() {
        final var outdatedGesuchsperioden = gesuchsperiodeRepository.findAllStoppBefore(LocalDate.now());
        LOG.info("Found {} Gesuchsperioden to be archived", outdatedGesuchsperioden.size());
        outdatedGesuchsperioden.forEach(gesuchsperiode -> {
            gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.ARCHIVIERT);
            LOG.info("Updated Gesuchsperiode with id %s to Gueltigkeisstatus ARCHIVIERT");
        });
    }
}
