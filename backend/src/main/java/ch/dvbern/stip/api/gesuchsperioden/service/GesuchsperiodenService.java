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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.exception.CustomValidationsException;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.common.validation.CustomConstraintViolation;
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

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_GESUCH_NO_VALID_GESUCHSPERIODE;

@RequestScoped
@RequiredArgsConstructor
public class GesuchsperiodenService {

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

    public List<GesuchsperiodeDto> getAllActive() {
        return gesuchsperiodeRepository
            .findAllActiveForDate(LocalDate.now())
            .map(gesuchsperiodeMapper::toDto)
            .toList();
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

            if (
                (eligibleGesuchsperiode != null) &&
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
                "gesuchsperiode"
            )
        );
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

    private void preventUpdateIfReadonly(final Gesuchsperiode gesuchsperiode) {
        if (gesuchsperiode.getGueltigkeitStatus() != GueltigkeitStatus.ENTWURF) {
            throw new IllegalStateException("Cannot update Gesuchsperiode if it is started");
        }
    }
}
