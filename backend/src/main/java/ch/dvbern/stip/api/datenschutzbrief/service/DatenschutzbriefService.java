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

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.api.datenschutzbrief.repo.DatenschutzbriefRepository;
import ch.dvbern.stip.api.datenschutzbrief.type.DatenschutzbriefEmpfaenger;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternService;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.DatenschutzbriefPdfService;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class DatenschutzbriefService {
    private final DatenschutzbriefPdfService datenschutzbriefPdfService;
    private final DatenschutzbriefRepository datenschutzbriefRepository;
    private final SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;
    private final ElternService elternService;

    public RestMulti<ByteArrayOutputStream> getDatenschutzbriefDokument(final UUID elternId) {
        final var elternTeil = elternService.getElternTeilById(elternId);
        final var filename = String.format("datenschutzbrief_%s", elternTeil.getElternTyp().toString());

        final CompletableFuture<ByteArrayOutputStream> generateDokumentFuture = CompletableFuture
            .supplyAsync(() -> datenschutzbriefPdfService.createDatenschutzbriefForElternteil(elternTeil));
        final Supplier<CompletionStage<ByteArrayOutputStream>> stageSupplier =
            () -> generateDokumentFuture;
        return DokumentDownloadUtil.getWrapedDokument(filename, stageSupplier);
    }

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
            final var empfaenger =
                getElternDatenschutzbriefEmpfaenger(trancheToUse.getGesuchFormular().getElterns(), empfaengerToCreate);
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

    private List<DatenschutzbriefEmpfaenger> getRequiredDatenschutzbriefEmpfaenger(
        final Familiensituation familiensituation
    ) {
        return steuerdatenTabBerechnungsService.calculateTabs(familiensituation)
            .stream()
            .map(DatenschutzbriefEmpfaenger::fromSteuerdatenTyp)
            .toList();
    }

    private Eltern getElternDatenschutzbriefEmpfaenger(Set<Eltern> eltern, DatenschutzbriefEmpfaenger empfaengerTyp) {
        final var prioritizedEltern = switch (empfaengerTyp) {
            case VATER -> eltern.stream().filter(e -> e.getElternTyp() == ElternTyp.VATER);
            case MUTTER -> eltern.stream().filter(e -> e.getElternTyp() == ElternTyp.MUTTER);
            default -> eltern.stream();
        };

        return prioritizedEltern.findFirst().orElseThrow(IllegalStateException::new);
    }
}
