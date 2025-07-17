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

package ch.dvbern.stip.api.common.statemachines.gesuch.handlers;

import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FehlendeDokumenteHandler implements GesuchStatusChangeHandler {
    private final GesuchDokumentService gesuchDokumentService;
    private final GesuchService gesuchService;

    @Override
    public void handle(Gesuch gesuch) {
        gesuchService.setDefaultNachfristDokumente(gesuch);
        gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
            .forEach(tranche -> tranche.setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS));
        gesuchDokumentService.setAbgelehnteDokumenteToAusstehendForGesuch(gesuch);
        gesuchService.sendFehlendeDokumenteNotifications(gesuch);
    }
}
