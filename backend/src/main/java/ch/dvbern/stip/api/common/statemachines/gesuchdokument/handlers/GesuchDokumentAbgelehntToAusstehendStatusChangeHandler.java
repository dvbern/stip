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

package ch.dvbern.stip.api.common.statemachines.gesuchdokument.handlers;

import java.util.List;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class GesuchDokumentAbgelehntToAusstehendStatusChangeHandler implements GesuchDokumentStatusChangeHandler {
    private final GesuchDokumentService gesuchDokumentService;

    @Override
    public void handle(
        GesuchDokument gesuchdokument
    ) {
        gesuchDokumentService.deleteFilesOfAbgelehnteGesuchDokumenteForGesuch(List.of(gesuchdokument));

    }
}
