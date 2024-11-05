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
import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateRequest;

public class BusinessParnterCreateMapper {
    BusinessPartnerCreateAdresseMapper adresseMapper = new BusinessPartnerCreateAdresseMapper();
    BusinessPartnerCreatePersDataMapper persDataMapper = new BusinessPartnerCreatePersDataMapper();
    BusinessPartnerCreateOrgDataMapper orgDataMapper = new BusinessPartnerCreateOrgDataMapper();

    BusinessPartnerCreateRequest.BUSINESSPARTNER toBusniessPartner(Auszahlung auszahlung) {
        BusinessPartnerCreateRequest.BUSINESSPARTNER businesspartner =
            new BusinessPartnerCreateRequest.BUSINESSPARTNER();
        BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA persdata =
            persDataMapper.toBusinessPartnerPERSDATA(auszahlung);
        BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA orgdata =
            orgDataMapper.toBusinessPartnerORGDATA(auszahlung);
        BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS address =
            adresseMapper.toBusinessPartnerAdresse(auszahlung);
        businesspartner.setPERSDATA(persdata);
        businesspartner.setORGDATA(orgdata);
        businesspartner.getADDRESS().add(address);
        return businesspartner;
    }
}
