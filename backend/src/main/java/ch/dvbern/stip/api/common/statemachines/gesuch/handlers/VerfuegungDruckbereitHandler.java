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

import java.util.Optional;

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus;
import ch.dvbern.stip.berechnung.service.BerechnungService;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class VerfuegungDruckbereitHandler implements GesuchStatusChangeHandler {
    private final TenantService tenantService;
    private final BerechnungService berechnungService;
    private final BuchhaltungService buchhaltungService;
    private final VerfuegungPdfService verfuegungPdfService;
    private final VerfuegungService verfuegungService;

    @Override
    public void handle(Gesuch gesuch) {
        BerechnungsresultatDto stipendien = null;
        final var latestVerfuegung = verfuegungService.getLatestVerfuegung(gesuch);
        final var tenantConfig = tenantService.getConfigForCurrentTenant();
        if (!latestVerfuegung.getVerfuegungStatus().isNegativ()) {
            stipendien = berechnungService.getBerechnungsresultatFromGesuch(
                gesuch,
                tenantConfig.berechnung().currentMajorVersion(),
                tenantConfig.berechnung().currentMinorVersion()
            );

            final int berechnungsresultat = stipendien.getBerechnungStipendium();
            final boolean hasAnspruch = berechnungsresultat > 0;

            latestVerfuegung.setVerfuegungStatus(
                hasAnspruch ? VerfuegungStatus.ANSPRUCH : VerfuegungStatus.KEIN_ANSPRUCH
            );
            latestVerfuegung.setBerechnungJsonData(BerechnungService.serializeBerechnungresultatDto(stipendien));

            if (hasAnspruch || !gesuch.isFirstVerfuegung()) {
                buchhaltungService.createStipendiumBuchhaltungEntry(
                    gesuch,
                    berechnungsresultat
                );
            }
        }

        if (latestVerfuegung.getDokumente().isEmpty()) {
            verfuegungPdfService.createVerfuegungsDocuments(gesuch, Optional.ofNullable(stipendien));
        }
    }
}
