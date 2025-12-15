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

package ch.dvbern.stip.api.sap.service;

import java.math.BigInteger;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sap.generated.business_partner.BusinessPartnerSearchRequest;
import ch.dvbern.stip.api.sap.generated.business_partner.SenderParms;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BusinessPartnerSearchMapper {
    @Mapping(target = "ONLYWITHREF", constant = "false")
    @Mapping(target = "READALLDETAILS", constant = "false")
    @Mapping(target = "OFFSET", constant = "0")
    @Mapping(target = "LIMIT", constant = "50")
    public abstract BusinessPartnerSearchRequest.FILTERPARMS getFilterParms(
        String sozialversicherungsnummer
    );

    @Mapping(source = ".", target = "AHVNR", qualifiedByName = "getSvn")
    public abstract BusinessPartnerSearchRequest.FILTERPARMSID getFilterParmsId(String sozialversicherungsnummer);

    @Named("getSvn")
    public String getSvn(String sozialversicherungsnummer) {
        return sozialversicherungsnummer.replace(".", "");
    }

    @Named("getSenderParms")
    public SenderParms getSenderParms(@Context BigInteger sysid, String sozialversicherungsnummer) {
        final SenderParms sender = new SenderParms();
        sender.setSYSID(sysid);
        return sender;
    }

    @Mapping(source = ".", target = "FILTERPARMS")
    @Mapping(source = ".", target = "FILTERPARMSID")
    @Mapping(source = ".", target = "SENDER", qualifiedByName = "getSenderParms")
    public abstract BusinessPartnerSearchRequest toBusinessPartnerSearchRequest(
        @Context BigInteger sysid,
        String sozialversicherungsnummer
    );

}
