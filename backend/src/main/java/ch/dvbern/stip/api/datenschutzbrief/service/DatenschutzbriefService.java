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

package ch.dvbern.stip.api.datenschutzbrief.service;

import java.util.List;
import java.util.stream.Stream;

import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.api.datenschutzbrief.repo.DatenschutzbriefRepository;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class DatenschutzbriefService {
    private final DatenschutzbriefRepository datenschutzbriefRepository;
    private final SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;

    @Transactional
    public void createAllRequiredDatenschutzbriefeForGesuch(final Gesuch gesuch) {
        if (gesuch.getGesuchTranchen().size() != 1) {
            LOG.error("Trying to create Datenschutzbriefe for a Gesuch with more than 1 Tranche");
            return;
        }

        final var trancheToUse = gesuch.getGesuchTranchen().getFirst();
        final var requiredEmpfaenger =
            getRequiredDatenschutzbriefEmpfaenger(trancheToUse.getGesuchFormular().getFamiliensituation());
        for (final var empfaengerToCreate : requiredEmpfaenger) {
            final var empfaenger = trancheToUse.getGesuchFormular()
                .getElternteilOfTyp(empfaengerToCreate)
                .orElseThrow(IllegalStateException::new);
            final var datenschutzbrief = new Datenschutzbrief()
                .setVersendet(false)
                .setDatenschutzbriefEmpfaenger(empfaengerToCreate)
                .setVorname(empfaenger.getVorname())
                .setNachname(empfaenger.getNachname())
                .setGesuch(gesuch);

            gesuch.getDatenschutzbriefs().add(datenschutzbrief);

            datenschutzbriefRepository.persist(datenschutzbrief);
        }
    }

    private List<ElternTyp> getRequiredDatenschutzbriefEmpfaenger(
        final Familiensituation familiensituation
    ) {
        return steuerdatenTabBerechnungsService.calculateTabs(familiensituation)
            .stream()
            .flatMap(steuerdatenTyp -> switch (steuerdatenTyp) {
                case MUTTER -> Stream.of(ElternTyp.MUTTER);
                case VATER -> Stream.of(ElternTyp.VATER);
                case FAMILIE -> Stream.of(ElternTyp.MUTTER, ElternTyp.VATER);
            })
            .toList();
    }
}
