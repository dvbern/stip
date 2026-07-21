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

package ch.dvbern.stip.api.generator.service;

import java.util.UUID;

import ch.dvbern.stip.api.demo.service.GenerateDemoDataService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DokumentGenerator {
    @Inject
    GenerateDemoDataService generateDemoDataService;

    @Transactional
    public void createDokumentsForAllRequired(UUID gesuchTrancheId) {
        generateDemoDataService.createDemoDokumentsForAllRequired(gesuchTrancheId);
    }
}
