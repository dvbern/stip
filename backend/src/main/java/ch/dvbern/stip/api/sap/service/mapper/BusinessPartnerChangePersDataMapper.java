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

package ch.dvbern.stip.api.sap.service.mapper;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeRequest;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named
public class BusinessPartnerChangePersDataMapper {
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA toBusinessPartnerPERSDATA(Auszahlung auszahlung) {
        BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA persdata =
            new BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA();
        persdata.setFIRSTNAME(auszahlung.getVorname());
        persdata.setLASTNAME(auszahlung.getNachname());
        return persdata;
    }

}
