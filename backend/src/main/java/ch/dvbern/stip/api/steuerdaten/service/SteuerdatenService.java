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

package ch.dvbern.stip.api.steuerdaten.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.validation.SteuerdatenPageValidation;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SteuerdatenService {
    private final Validator validator;
    private final GesuchTrancheRepository trancheRepository;
    private final SteuerdatenMapper steuerdatenMapper;
    private final SteuerdatenRepository steuerdatenRepository;

    public Steuerdaten getSteuerdatenById(UUID id) {
        return steuerdatenRepository.requireById(id);
    }

    public Set<Steuerdaten> getSteuerdaten(UUID gesuchTrancheId) {
        return trancheRepository.requireById(gesuchTrancheId).getGesuchFormular().getSteuerdaten();
    }

    @Transactional
    public List<Steuerdaten> updateSteuerdaten(
        UUID gesuchTrancheId,
        List<SteuerdatenDto> steuerdatenDtos
    ) {
        final var formular = trancheRepository.requireById(gesuchTrancheId).getGesuchFormular();

        final var steuerdaten = steuerdatenMapper.map(steuerdatenDtos, formular.getSteuerdaten()).stream().toList();
        ValidatorUtil.validate(validator, formular, SteuerdatenPageValidation.class);

        steuerdaten.forEach(steuerdatenRepository::persistAndFlush);
        return steuerdaten;
    }
}
