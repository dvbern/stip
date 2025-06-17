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

package ch.dvbern.stip.stipdecision.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Kanton {
    AG("stip.kanton.ag"), AI("stip.kanton.ai"), AR("stip.kanton.ar"), BL("stip.kanton.bl"), BS("stip.kanton.bs"),
    BE("stip.kanton.be"), FR("stip.kanton.fr"), GE("stip.kanton.ge"), GL("stip.kanton.gl"), GR("stip.kanton.gr"),
    JU("stip.kanton.ju"), LU("stip.kanton.lu"), NE("stip.kanton.ne"), NW("stip.kanton.nw"), OW("stip.kanton.ow"),
    SG("stip.kanton.sg"), SH("stip.kanton.sh"), SZ("stip.kanton.sz"), SO("stip.kanton.so"), TI("stip.kanton.ti"),
    TG("stip.kanton.tg"), UR("stip.kanton.ur"), VD("stip.kanton.vd"), VS("stip.kanton.vs"), ZG("stip.kanton.zg"),
    ZH("stip.kanton.zh"), AU("stip.kanton.au");

    public String tlKey;
}
