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

package ch.dvbern.stip.api.ausbildung.service;

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungRepository;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsgangRepository;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.fall.service.FallService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class AusbildungService {
    private final FallRepository fallRepository;
    private final AusbildungRepository ausbildungRepository;
    private final AusbildungsgangRepository ausbildungsgangRepository;
    private final AusbildungMapper ausbildungMapper;
    private final GesuchService gesuchService;
    private final GesuchsperiodenService gesuchsperiodeService;
    private final FallService fallService;
    private final Validator validator;

    @Transactional
    public AusbildungDto createAusbildung(final AusbildungUpdateDto ausbildungUpdateDto) {
        if (fallService.hasAktiveAusbildung(ausbildungUpdateDto.getFallId())) {
            throw new BadRequestException("Cannot create Ausbildung for Fall with Aktive Ausbildung");
        }

        final var ausbildung = ausbildungMapper.toNewEntity(ausbildungUpdateDto);
        ausbildung.setFall(fallRepository.requireById(ausbildung.getFall().getId()));

        final var violations = validator.validate(ausbildung);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }
        ausbildungRepository.persist(ausbildung);
        final var gesuchCreateDto = new GesuchCreateDto();
        gesuchCreateDto.setAusbildungId(ausbildung.getId());
        gesuchService.createGesuch(gesuchCreateDto);

        if (ausbildung.getAusbildungsgang() != null) {
            ausbildung
                .setAusbildungsgang(ausbildungsgangRepository.requireById(ausbildung.getAusbildungsgang().getId()));
        }

        return ausbildungMapper.toDto(ausbildung);
    }

    @Transactional
    public AusbildungDto getAusbildungById(final UUID ausbildungId) {
        return ausbildungMapper.toDto(ausbildungRepository.requireById(ausbildungId));
    }

    @Transactional
    public AusbildungDto patchAusbildung(final UUID ausbildungId, final AusbildungUpdateDto ausbildungUpdateDto) {
        var ausbildung = ausbildungRepository.requireById(ausbildungId);
        ausbildung = ausbildungMapper.partialUpdate(ausbildungUpdateDto, ausbildung);

        if (ausbildung.getAusbildungsgang() != null) {
            ausbildung
                .setAusbildungsgang(ausbildungsgangRepository.requireById(ausbildung.getAusbildungsgang().getId()));
        }
        Set<ConstraintViolation<Ausbildung>> violations = validator.validate(ausbildung);
        if (!violations.isEmpty()) {
            throw new ValidationsException("Die Entität ist nicht valid", violations);
        }

        final var gesuch = ausbildung.getGesuchs().get(0);
        final var gesuchsperiode = gesuchsperiodeService.getGesuchsperiodeForAusbildung(
            ausbildung
        );
        gesuch.setGesuchsperiode(gesuchsperiode);
        var ausbildungsstart =
            ausbildung.getAusbildungBegin().withYear(gesuchsperiode.getGesuchsperiodeStart().getYear());
        if (ausbildungsstart.isAfter(gesuchsperiode.getGesuchsperiodeStopp())) {
            ausbildungsstart = ausbildungsstart.minusYears(1);
        }

        gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .setGueltigkeit(
                new DateRange(ausbildungsstart, ausbildungsstart.plusYears(1).minusDays(1))
            );
        ausbildungRepository.persistAndFlush(ausbildung);

        return ausbildungMapper.toDto(ausbildung);
    }
}
