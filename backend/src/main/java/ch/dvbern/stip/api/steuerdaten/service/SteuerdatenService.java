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
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternUtil;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuerdaten.validation.SteuerdatenPageValidation;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import ch.dvbern.stip.integration.steuerdaten.domain.port.SteuerdatenPortFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class SteuerdatenService {
    private final Validator validator;
    private final GesuchTrancheRepository trancheRepository;
    private final SteuerdatenMapper steuerdatenMapper;
    private final SteuerdatenRepository steuerdatenRepository;
    private final SteuerdatenPortFactory steuerdatenPortFactory;
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;

    @Transactional
    public List<SteuerdatenDto> getSteuerdaten(UUID gesuchTrancheId) {
        // query history if tranche does not exist anymore
        var gesuchTranche = trancheRepository.findById(gesuchTrancheId);
        if (gesuchTranche == null) {
            gesuchTranche = gesuchTrancheHistoryService.getLatestTranche(gesuchTrancheId);
        }
        final var steuerdaten = gesuchTranche.getGesuchFormular().getSteuerdaten();
        return steuerdaten.stream().map(steuerdatenMapper::toDto).toList();
    }

    @Transactional
    public List<SteuerdatenDto> updateSteuerdaten(
        UUID gesuchTrancheId,
        List<SteuerdatenDto> steuerdatenDtos
    ) {
        final var formular = trancheRepository.requireById(gesuchTrancheId).getGesuchFormular();

        final var steuerdaten = steuerdatenMapper.map(steuerdatenDtos, formular.getSteuerdaten()).stream().toList();
        ValidatorUtil.validate(validator, formular, SteuerdatenPageValidation.class);

        steuerdaten.forEach(steuerdatenRepository::persistAndFlush);
        return steuerdaten.stream()
            .map(steuerdatenMapper::toDto)
            .toList();
    }

    @Transactional
    public List<SteuerdatenDto> updateSteuerdatenFromPort(
        UUID gesuchTrancheId,
        SteuerdatenTyp steuerdatenTyp,
        int steuerjahr
    ) {
        final var gesuchtranche = trancheRepository.requireById(gesuchTrancheId);
        final var gesuchFormular = gesuchtranche.getGesuchFormular();
        final var steuerdatenOptional = gesuchFormular.getSteuerdaten()
            .stream()
            .filter(steuerdatum -> steuerdatum.getSteuerdatenTyp() == steuerdatenTyp)
            .findFirst();
        Steuerdaten steuerdaten = null;
        if (steuerdatenOptional.isPresent()) {
            steuerdaten = steuerdatenOptional.get();
        } else {
            steuerdaten = new Steuerdaten();
            steuerdaten.setSteuerdatenTyp(steuerdatenTyp);
        }

        final Optional<Eltern> elternToUse = switch (steuerdatenTyp) {
            // If Familie, use Vater for lookup, see KSTIP-2734
            case FAMILIE, VATER -> ElternUtil.getElternByType(gesuchFormular.getElterns(), ElternTyp.VATER);
            case MUTTER -> ElternUtil.getElternByType(gesuchFormular.getElterns(), ElternTyp.MUTTER);
        };

        String ssvn = elternToUse.orElseThrow(NotFoundException::new).getSozialversicherungsnummer();

        var steuerdatenPortData = steuerdatenPortFactory.getSteuerdatenAdapter()
            .getSteuerdaten(
                ssvn,
                steuerjahr,
                steuerdatenTyp,
                gesuchtranche.getGesuch().getAusbildung().getFall().getFallNummer(),
                gesuchtranche.getGesuch().getGesuchNummer()
            );

        steuerdaten = steuerdatenMapper.partialUpdate(steuerdatenPortData, steuerdaten);
        gesuchFormular.getSteuerdaten().add(steuerdaten);

        steuerdatenRepository.persistAndFlush(steuerdaten);
        return trancheRepository.requireById(gesuchTrancheId)
            .getGesuchFormular()
            .getSteuerdaten()
            .stream()
            .map(steuerdatenMapper::toDto)
            .toList();
    }
}
