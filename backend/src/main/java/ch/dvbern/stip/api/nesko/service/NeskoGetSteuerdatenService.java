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

package ch.dvbern.stip.api.nesko.service;

import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.GetSteuerdaten;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.StipendienAuskunftService;
import io.quarkiverse.cxf.annotation.CXFClient;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class NeskoGetSteuerdatenService {
    // @Inject
    @CXFClient("stipendienAuskunftService")
    StipendienAuskunftService stipendienAuskunftService;

    public GetSteuerdatenResponse getSteuerdatenResponse(String token, String ssvn, Integer steuerjahr) {
        var request = new GetSteuerdaten();
        request.setSteuerjahr(steuerjahr);
        request.setSozialversicherungsnummer(Long.valueOf(Integer.valueOf(ssvn.replace(".", ""))));
        GetSteuerdatenResponse result = null;
        try {
            result = stipendienAuskunftService.getStipendienAuskunft().getSteuerdaten(request);
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
        return result;
    }
}
