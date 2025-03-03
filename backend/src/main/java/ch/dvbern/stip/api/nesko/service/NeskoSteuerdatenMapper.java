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

import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class NeskoSteuerdatenMapper {
    Steuerdaten updateFromNeskoSteuerdaten(Steuerdaten steuerdaten, GetSteuerdatenResponse getSteuerdatenResponse) {
        var steuerdatenNesko = getSteuerdatenResponse.getSteuerdaten();

        steuerdaten.setTotalEinkuenfte(steuerdatenNesko.getTotalEinkuenfte().getEffektiv().intValue());
        steuerdaten.setEigenmietwert(steuerdatenNesko.getMietwertKanton().intValue());
        steuerdaten.setIsArbeitsverhaeltnisSelbstaendig();
        steuerdaten.setSaeule3a();
        steuerdaten.setSaeule2();
        steuerdaten.setKinderalimente();
        steuerdaten.setVermoegen(steuerdatenNesko.getSteuerbaresVermoegenKanton().getEffektiv().intValue());
        steuerdaten.setSteuernKantonGemeinde(steuerdatenNesko.getSteuerbetragKanton().intValue());
        steuerdaten.setSteuernBund(steuerdatenNesko.getSteuerbetragBund().intValue());
        int fahrkosten = switch (steuerdaten.getSteuerdatenTyp()) {
            case FAMILIE -> (steuerdatenNesko.getFahrkosten().getMann().getEffektiv().intValue()
            + steuerdatenNesko.getFahrkosten().getFrau().getEffektiv().intValue());
            case VATER -> steuerdatenNesko.getFahrkosten().getMann().getEffektiv().intValue();
            case MUTTER -> steuerdatenNesko.getFahrkosten().getFrau().getEffektiv().intValue();
        };
        steuerdaten.setFahrkosten(fahrkosten);
        int fahrkostenPartner = switch (steuerdaten.getSteuerdatenTyp()) {
            case FAMILIE -> 0;
            case VATER -> steuerdatenNesko.getFahrkosten().getFrau().getEffektiv().intValue();
            case MUTTER -> steuerdatenNesko.getFahrkosten().getMann().getEffektiv().intValue();
        };
        steuerdaten.setFahrkostenPartner(fahrkostenPartner);
        int verpflegung = switch (steuerdaten.getSteuerdatenTyp()) {
            case FAMILIE -> (steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann().getEffektiv().intValue()
            + steuerdatenNesko.getFahrkosten().getFrau().getEffektiv().intValue());
            case VATER -> steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann().getEffektiv().intValue();
            case MUTTER -> steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau().getEffektiv().intValue();
        };
        steuerdaten.setVerpflegung(verpflegung);
        int verpflegungPartner = switch (steuerdaten.getSteuerdatenTyp()) {
            case FAMILIE -> 0;
            case VATER -> steuerdatenNesko.getKostenAuswaertigeVerpflegung().getFrau().getEffektiv().intValue();
            case MUTTER -> steuerdatenNesko.getKostenAuswaertigeVerpflegung().getMann().getEffektiv().intValue();
        };
        steuerdaten.setVerpflegungPartner(verpflegungPartner);
        return steuerdaten;
    }
}
