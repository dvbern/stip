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

import java.math.BigDecimal;
import java.math.BigInteger;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sap.generated.general.SenderParms;
import ch.dvbern.stip.api.sap.generated.general.SenderParmsDelivery;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public class GeneralMapper {
    @Named("getSenderParms")
    public SenderParms getSenderParms(
        BigInteger sysid
    ) {
        final SenderParms sender = new SenderParms();
        sender.setSYSID(sysid);
        return sender;
    }

    @Named("getSenderParmsDelivery")
    public SenderParmsDelivery getSenderParmsDelivery(
        BigInteger sysid,
        BigDecimal deliveryid
    ) {
        final SenderParmsDelivery sender = new SenderParmsDelivery();
        sender.setSYSID(sysid);
        sender.setDELIVERYID(deliveryid);
        return sender;
    }
}
