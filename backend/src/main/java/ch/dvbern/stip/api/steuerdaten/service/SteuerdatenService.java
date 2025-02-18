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
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.gesuch.service.ValidateUpdateLegalityUtil;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.service.GesuchsjahrUtil;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SteuerdatenService {
    private final GesuchTrancheRepository trancheRepository;
    private final SteuerdatenMapper steuerdatenMapper;
    private final SteuerdatenRepository steuerdatenRepository;
    private final BenutzerService benutzerService;
    private final SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;

    public Set<Steuerdaten> getSteuerdaten(UUID gesuchTrancheId) {
        return trancheRepository.requireById(gesuchTrancheId).getGesuchFormular().getSteuerdaten();
    }

    @Transactional
    public void setAndValidateSteuerdatenUpdateLegality(
        final List<SteuerdatenDto> steuerdatenDtos,
        final GesuchTranche trancheToUpdate
    ) {
        final var gesuchsjahr = trancheToUpdate
            .getGesuch()
            .getGesuchsperiode()
            .getGesuchsjahr();

        final var steuerdatenList = trancheToUpdate.getGesuchFormular()
            .getSteuerdaten()
            .stream()
            .filter(tab -> tab.getSteuerdatenTyp() != null)
            .toList();

        final var requiredTabs =
            steuerdatenTabBerechnungsService.calculateTabs(trancheToUpdate.getGesuchFormular().getFamiliensituation());

        for (final var steuerdatenDto : steuerdatenDtos) {
            setAndValidateSteuerdatenTabUpdateLegality(
                steuerdatenDto,
                steuerdatenList.stream()
                    .filter(tab -> tab.getId().equals(steuerdatenDto.getId()))
                    .findFirst()
                    .orElse(null),
                gesuchsjahr
            );
        }
    }

    private void setAndValidateSteuerdatenTabUpdateLegality(
        final SteuerdatenDto steuerdatenDto,
        final Steuerdaten steuerdatenTabs,
        Gesuchsjahr gesuchsjahr
    ) {
        final var benutzerRollenIdentifiers = benutzerService.getCurrentBenutzer()
            .getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());

        Integer steuerjahrToSet = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
        Integer veranlagungsCodeToSet = 0;

        if (steuerdatenTabs != null) {
            final Integer steuerjahrDtoValue = steuerdatenDto.getSteuerjahr();
            final Integer steuerjahrExistingValue = steuerdatenTabs.getSteuerjahr();
            final Integer steuerjahrDefaultValue = GesuchsjahrUtil.getDefaultSteuerjahr(gesuchsjahr);
            steuerjahrToSet = ValidateUpdateLegalityUtil.getAndValidateLegalityValue(
                benutzerRollenIdentifiers,
                steuerjahrDtoValue,
                steuerjahrExistingValue,
                steuerjahrDefaultValue
            );

            final Integer veranlagungsCodeDtoValue = steuerdatenDto.getVeranlagungsCode();
            final Integer veranlagungsCodeExistingValue = steuerdatenTabs.getVeranlagungsCode();
            final Integer veranlagungscodeDefaltValue = 0;
            veranlagungsCodeToSet = ValidateUpdateLegalityUtil.getAndValidateLegalityValue(
                benutzerRollenIdentifiers,
                veranlagungsCodeDtoValue,
                veranlagungsCodeExistingValue,
                veranlagungscodeDefaltValue
            );
        }

        steuerdatenDto.setSteuerjahr(steuerjahrToSet);
        steuerdatenDto.setVeranlagungsCode(veranlagungsCodeToSet);
    }

    @Transactional
    public List<Steuerdaten> updateSteuerdaten(
        UUID gesuchTrancheId,
        List<SteuerdatenDto> steuerdatenDtos
    ) {
        final var tranche = trancheRepository.requireById(gesuchTrancheId);
        final var formular = tranche.getGesuchFormular();
        setAndValidateSteuerdatenUpdateLegality(
            steuerdatenDtos,
            tranche
        );
        final var steuerdaten = steuerdatenMapper.map(steuerdatenDtos, formular.getSteuerdaten()).stream().toList();
        steuerdaten.forEach(steuerdatenRepository::persistAndFlush);
        return steuerdaten;
    }
}
