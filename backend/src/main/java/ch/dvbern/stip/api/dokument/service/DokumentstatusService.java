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

package ch.dvbern.stip.api.dokument.service;

import ch.dvbern.stip.api.common.statemachines.gesuchdokument.GesuchDokumentStatusConfigProducer;
import ch.dvbern.stip.api.common.statemachines.gesuchdokument.handlers.GesuchDokumentStatusChangeHandler;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatusChangeEvent;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import com.github.oxo42.stateless4j.StateMachine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DokumentstatusService {
    private final GesuchDokumentKommentarService dokumentKommentarService;
    private final Instance<GesuchDokumentStatusChangeHandler> handlers;

    public void triggerStatusChangeNoComment(
        final GesuchDokument gesuchDokument,
        final GesuchDokumentStatusChangeEvent event
    ) {
        final var sm = createStateMachine(gesuchDokument);
        sm.fire(DokumentstatusChangeEventTrigger.createTrigger(event), gesuchDokument);
    }

    public void triggerStatusChange(final GesuchDokument gesuchDokument, final GesuchDokumentStatusChangeEvent event) {
        final var sm = createStateMachine(gesuchDokument);
        sm.fire(DokumentstatusChangeEventTrigger.createTrigger(event), gesuchDokument);
        dokumentKommentarService.createEmptyKommentarForGesuchDokument(gesuchDokument);
    }

    public void triggerStatusChangeWithComment(
        final GesuchDokument gesuchDokument,
        final GesuchDokumentStatusChangeEvent event,
        final GesuchDokumentKommentarDto commentDto
    ) {
        final var sm = createStateMachine(gesuchDokument);
        sm.fire(DokumentstatusChangeEventTrigger.createTrigger(event), gesuchDokument);
        dokumentKommentarService.createKommentarForGesuchDokument(gesuchDokument, commentDto);
    }

    private StateMachine<GesuchDokumentStatus, GesuchDokumentStatusChangeEvent> createStateMachine(
        final GesuchDokument gesuchDokument
    ) {
        var config = GesuchDokumentStatusConfigProducer.createStateMachineConfig(handlers);
        return new StateMachine<>(
            gesuchDokument.getStatus(),
            gesuchDokument::getStatus,
            gesuchDokument::setStatus,
            config
        );
    }
}
