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

package ch.dvbern.stip.api.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Kanton {
    AG("stip.kanton.ag", 19, null),
    AI("stip.kanton.ai", 16, null),
    AR("stip.kanton.ar", 15, null),
    BL("stip.kanton.bl", 13, null),
    BS("stip.kanton.bs", 12, null),
    BE("stip.kanton.be", 2, "BE_KT"),
    FR("stip.kanton.fr", 10, null),
    GE("stip.kanton.ge", 25, null),
    GL("stip.kanton.gl", 8, null),
    GR("stip.kanton.gr", 18, null),
    JU("stip.kanton.ju", 26, null),
    LU("stip.kanton.lu", 3, null),
    NE("stip.kanton.ne", 24, null),
    NW("stip.kanton.nw", 7, null),
    OW("stip.kanton.ow", 6, null),
    SG("stip.kanton.sg", 17, null),
    SH("stip.kanton.sh", 14, null),
    SZ("stip.kanton.sz", 5, null),
    SO("stip.kanton.so", 11, null),
    TI("stip.kanton.ti", 21, null),
    TG("stip.kanton.tg", 20, null),
    UR("stip.kanton.ur", 4, null),
    VD("stip.kanton.vd", 22, null),
    VS("stip.kanton.vs", 23, null),
    ZG("stip.kanton.zg", 9, null),
    ZH("stip.kanton.zh", 1, null);

    private final String tlKey;
    private final int bfsCode;
    private final String bfsDelivery;
}
