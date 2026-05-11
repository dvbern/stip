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
    AG("stip.kanton.ag", 19),
    AI("stip.kanton.ai", 16),
    AR("stip.kanton.ar", 15),
    BL("stip.kanton.bl", 13),
    BS("stip.kanton.bs", 12),
    BE("stip.kanton.be", 2),
    FR("stip.kanton.fr", 10),
    GE("stip.kanton.ge", 25),
    GL("stip.kanton.gl", 8),
    GR("stip.kanton.gr", 18),
    JU("stip.kanton.ju", 26),
    LU("stip.kanton.lu", 3),
    NE("stip.kanton.ne", 24),
    NW("stip.kanton.nw", 7),
    OW("stip.kanton.ow", 6),
    SG("stip.kanton.sg", 17),
    SH("stip.kanton.sh", 14),
    SZ("stip.kanton.sz", 5),
    SO("stip.kanton.so", 11),
    TI("stip.kanton.ti", 21),
    TG("stip.kanton.tg", 20),
    UR("stip.kanton.ur", 4),
    VD("stip.kanton.vd", 22),
    VS("stip.kanton.vs", 23),
    ZG("stip.kanton.zg", 9),
    ZH("stip.kanton.zh", 1);

    private final String tlKey;
    private final int bfsCode;
}
