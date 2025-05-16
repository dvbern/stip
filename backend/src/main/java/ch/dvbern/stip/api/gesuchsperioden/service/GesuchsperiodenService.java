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
import ch.dvbern.stip.api.ausbildung.type.AusbildungCreateErrorType;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
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
import org.apache.commons.lang3.tuple.Pair;

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

    public Pair<Gesuchsperiode, AusbildungCreateErrorType> getGesuchsperiodeForAusbildung(final Ausbildung ausbildung) {
        final var ausbildungBegin = ausbildung.getAusbildungBegin();

        for (int yearOffset = 1; yearOffset >= -1; yearOffset--) {
            var ausbildungsBeginAssumed = ausbildungBegin.withYear(LocalDate.now().getYear() + yearOffset);

            if (ausbildungsBeginAssumed.isBefore(ausbildungBegin)) {
                break;
            }

            var eligibleGesuchsperiode =
                gesuchsperiodeRepository.findPubliziertStartBeforeOrAt(ausbildungsBeginAssumed);

            if (
                eligibleGesuchsperiode
                    .getGesuchsperiodeStart()
                    .plusMonths(6)
                    .isAfter(ausbildungsBeginAssumed) &&
                eligibleGesuchsperiode.getAufschaltterminStart().isBefore(LocalDate.now())
            ) {
                return Pair.of(eligibleGesuchsperiode, null);
            }
        }

        // None were found, throw error
        final var ausbildungThisYear = ausbildungBegin.withYear(LocalDate.now().getYear());
        LocalDate dateToCheck;
        if (ausbildungThisYear.isBefore(LocalDate.now())) {
            dateToCheck = ausbildungThisYear.plusYears(1);
        } else {
            dateToCheck = ausbildungThisYear;
        }

        final var toCheck = gesuchsperiodeRepository.findStartBeforeOrAt(dateToCheck);

        if (toCheck.getGueltigkeitStatus() == GueltigkeitStatus.ENTWURF) {
            // Die Gesuchsperiode für diesen Ausbildungsbeginn wird voraussichtlich am xx.xx geöffnet
            return Pair.of(toCheck, AusbildungCreateErrorType.PERIODE_IN_ENTWURF_GEFUNDEN);
        } else if (toCheck.getGueltigkeitStatus() == GueltigkeitStatus.PUBLIZIERT) {
            // Die Gesuchsperiode für diesen Ausbildungsbeginn wird am xx.xx geöffnet
            return Pair.of(toCheck, AusbildungCreateErrorType.INAKTIVE_PERIODE_GEFUNDEN);
        } else {
            // Es ist keine aktive Gesuchsperiode für diesen Ausbildungsbeginn vorhanden
            return Pair.of(null, AusbildungCreateErrorType.KEINE_AKTIVE_PERIODE_GEFUNDEN);
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
        final var outdatedGesuchsperioden = gesuchsperiodeRepository.findAllPubliziertStoppBefore(LocalDate.now());
        LOG.info("Found {} Gesuchsperioden to be archived", outdatedGesuchsperioden.size());
        outdatedGesuchsperioden.forEach(gesuchsperiode -> {
            gesuchsperiode.setGueltigkeitStatus(GueltigkeitStatus.ARCHIVIERT);
            LOG.info("Updated Gesuchsperiode with id %s to Gueltigkeisstatus ARCHIVIERT");
        });
    }
}
