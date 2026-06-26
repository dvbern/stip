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

package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.util.GesuchUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.generated.dto.GesuchStateInfoDto;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class GesuchStateInfoMapper {
    @Inject
    GesuchStatusService gesuchStatusService;

    @Mapping(source = ".", target = "canGetBerechnung", qualifiedByName = "getCanGetBerechnung")
    @Mapping(source = ".", target = "canTriggerManuellPruefen", qualifiedByName = "getCanTriggerManuellPruefen")
    @Mapping(source = ".", target = "canBearbeitungAbschliessen", qualifiedByName = "canBearbeitungAbschliessen")
    @Mapping(source = ".", target = "canChangeGesuchsperiode", qualifiedByName = "canChangeGesuchsperiode")
    @Mapping(source = ".", target = "canSBInitAenderung", qualifiedByName = "canSBInitAenderung")
    public abstract GesuchStateInfoDto toDto(Gesuch gesuch);

    @Named("getCanGetBerechnung")
    boolean getCanGetBerechnung(Gesuch gesuch) {
        return gesuchStatusService.canGetBerechnungSb(gesuch);
    }

    @Named("getCanTriggerManuellPruefen")
    boolean getCanTriggerManuellPruefen(Gesuch gesuch) {
        return gesuchStatusService.getCanTriggerManuellPruefen(gesuch);
    }

    @Named("canBearbeitungAbschliessen")
    boolean canBearbeitungAbschliessen(Gesuch gesuch) {
        return gesuchStatusService.canBearbeitungAbschliessen(gesuch);
    }

    @Named("canChangeGesuchsperiode")
    boolean canChangeGesuchsperiode(Gesuch gesuch) {
        return gesuchStatusService.canChangeGesuchsperiode(gesuch);
    }

    @Named("canSBInitAenderung")
    boolean canSBInitAenderung(Gesuch gesuch) {
        return GesuchUtil.canSbInitAendererung(gesuch);
    }

}
