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

package ch.dvbern.stip.api.massendruck.service;

import java.util.UUID;

import ch.dvbern.stip.api.massendruck.repo.MassendruckJobRepository;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobPdfService {
    private final MassendruckJobRepository massendruckJobRepository;

    @Transactional
    public void setFailedStatusOnJob(final UUID massendruckJobId) {
        final var massendruck = massendruckJobRepository.requireById(massendruckJobId);
        massendruck.setStatus(MassendruckJobStatus.FAILED);
    }

    @Transactional
    public void downloadCombineAndSaveForJob(final UUID massendruckJobId) {
        final var massendruckJob = massendruckJobRepository.requireById(massendruckJobId);

        // Download all PDFs from S3 if Verfuegung, otherwise generate the Datenschutzbriefe PDFs

        // Merge them using an iText PdfMerger

        // Set the Document for MassendruckJob

        // Upload the merged PDF to S3
    }
}
