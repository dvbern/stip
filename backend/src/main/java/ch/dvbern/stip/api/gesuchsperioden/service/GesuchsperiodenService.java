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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.type.GesuchsperiodeSelectErrorType;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.common.util.DateUtil;
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

    public Pair<Gesuchsperiode, GesuchsperiodeSelectErrorType> getGesuchsperiodeForAusbildung(
        final Ausbildung ausbildung
    ) {
        if (ausbildung.getAusbildungBegin().isBefore(LocalDate.now())) {
            return getGesuchsperiodeDateInPast(ausbildung.getAusbildungBegin());
        } else {
            return getGesuchsperiodeDateInFuture(ausbildung.getAusbildungBegin());
        }
    }

    Pair<Gesuchsperiode, GesuchsperiodeSelectErrorType> getGesuchsperiodeDateInFuture(final LocalDate ausbildungBegin) {
        final var overlappingPerioden = gesuchsperiodeRepository.findAllStartAndStopIntersect(ausbildungBegin);
        if (overlappingPerioden.isEmpty()) {
            return Pair.of(null, GesuchsperiodeSelectErrorType.KEINE_AKTIVE_PERIODE_GEFUNDEN);
        }

        // get is fine here, as the list is guaranteed not empty
        final var toAssign =
            overlappingPerioden.stream().max(Comparator.comparing(Gesuchsperiode::getAufschaltterminStart)).get();
        if (toAssign.getGueltigkeitStatus() == GueltigkeitStatus.ARCHIVIERT) {
            LOG.error("A Gesuchsperiode in the future is archiviert");
            return Pair.of(null, GesuchsperiodeSelectErrorType.KEINE_AKTIVE_PERIODE_GEFUNDEN);
        }

        if (toAssign.isActiveFor(LocalDate.now())) {
            return Pair.of(toAssign, null);
        }

        if (toAssign.getGueltigkeitStatus() == GueltigkeitStatus.ENTWURF) {
            return Pair.of(toAssign, GesuchsperiodeSelectErrorType.PERIODE_IN_ENTWURF_GEFUNDEN);
        } else {
            return Pair.of(toAssign, GesuchsperiodeSelectErrorType.INAKTIVE_PERIODE_GEFUNDEN);
        }
    }

    private Pair<Gesuchsperiode, GesuchsperiodeSelectErrorType> getGesuchsperiodeDateInPast(
        final LocalDate ausbildungBegin
    ) {
        final var currentlyPublic = gesuchsperiodeRepository.findAllPublicStartAndStopIntersect(LocalDate.now());
        if (currentlyPublic.size() != 2) {
            LOG.error("There are != 2 currently active Gesuchsperioden, currently active: {}", currentlyPublic.size());
            return Pair.of(null, GesuchsperiodeSelectErrorType.KEINE_AKTIVE_PERIODE_GEFUNDEN);
        }

        final var isFruehling = DateUtil.isFruehling(ausbildungBegin);

        Gesuchsperiode toAssign;
        if (isFruehling) {
            final var toAssignOpt = currentlyPublic.stream()
                .filter(gesuchsperiode -> DateUtil.isFruehling(gesuchsperiode.getGesuchsperiodeStart()))
                .findFirst();

            if (toAssignOpt.isEmpty()) {
                LOG.error("No active Fruehling periode found");
                return Pair.of(null, GesuchsperiodeSelectErrorType.KEINE_AKTIVE_PERIODE_GEFUNDEN);
            }

            toAssign = toAssignOpt.get();
        } else {
            final var toAssignOpt = currentlyPublic.stream()
                .filter(gesuchsperiode -> DateUtil.isHerbst(gesuchsperiode.getGesuchsperiodeStart()))
                .findFirst();

            if (toAssignOpt.isEmpty()) {
                LOG.error("No active Herbst periode found");
                return Pair.of(null, GesuchsperiodeSelectErrorType.KEINE_AKTIVE_PERIODE_GEFUNDEN);
            }

            toAssign = toAssignOpt.get();
        }

        if (toAssign.getGueltigkeitStatus() == GueltigkeitStatus.ENTWURF) {
            LOG.error("Gesuchsperiode for a past date was found in Status Entwurf: {}", toAssign.getId());
            return Pair.of(toAssign, GesuchsperiodeSelectErrorType.INAKTIVE_PERIODE_GEFUNDEN);
        } else if (toAssign.getGueltigkeitStatus() == GueltigkeitStatus.ARCHIVIERT) {
            return Pair.of(toAssign, GesuchsperiodeSelectErrorType.INAKTIVE_PERIODE_GEFUNDEN);
        } else {
            return Pair.of(toAssign, null);
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
