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

package ch.dvbern.stip.api.util;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DateRange;

public final class TestConstants {
    public static final String FREIGABESTELLE = "9477487f-3ac4-4d02-b57c-e0cefb292a88";
    public static final String SOZIALDIENST_ADMIN_ID = "9477487f-3ac4-4d02-b57c-e0cefb292a77";
    public static final String SOZIALDIENST_MITARBEITER_ID = "9477487f-3ac4-4d02-b57c-e0cefb292ae1";
    public static final String GESUCHSTELLER_TEST_ID = "9477487f-3ac4-4d02-b57c-e0cefb292ae5";

    public static final String GESUCHSTELLER_2_TEST_ID = "9477487f-3ac4-4d02-b57c-e0cefb292ae3";
    public static final String GESUCHSTELLER_2_TEST_AHV_NUMMER = "756.9217.0769.85";

    public static final String DELETE_USER_TEST_ID = "2432cf6c-15b3-4fdf-a660-c935f7011f00";

    public static final String JURIST_ID = "9477487f-3ac4-4d02-b57c-e0cefb292ae4";

    public static final String FALL_TEST_ID = "4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b";

    public static final String TEST_FILE_LOCATION = "./src/test/resources/testUpload.txt";
    public static final String TEST_XML_FILE_LOCATION = "./src/test/resources/testUpload.xml";
    public static final String TEST_PNG_FILE_LOCATION = "./src/test/resources/testUpload.png";

    public static final String AHV_NUMMER_VALID = "756.9217.0769.85";
    public static final String AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG = "756.1111.1113.11";
    public static final String AHV_NUMMER_VALID_MUTTER = "756.1111.1111.13";
    public static final String AHV_NUMMER_VALID_VATER = "756.1111.1114.10";
    public static final String AHV_NUMMER_VALID_PARTNER = "756.1111.1112.12";
    public static final String IBAN_CH_NUMMER_VALID = "CH5604835012345678009";
    public static final String IBAN_CH_NUMMER_INVALID = "CH5004835012345678009";
    public static final String QR_IBAN_CH_VALID = "CH6830808002425998193";
    public static final String QR_IBAN_CH_INVALID = "CH6030808002425998193";
    public static final String IBAN_LI_NUMMER_VALID = "LI7408806123456789012";
    public static final String IBAN_AT_NUMMER_VALID = "AT483200000012345864";

    public static UUID TEST_AUSBILDUNGSGANG_ID;
    public static UUID TEST_BILDUNGSKATEGORIE_ID;
    public static UUID TEST_AUSBILDUNGSSTAETTE_ID;
    public static UUID TEST_GESUCHSPERIODE_ID;
    public static UUID TEST_GESUCHSJAHR_ID;

    public static DateRange GUELTIGKEIT_PERIODE_23_24;

    public static DateRange GUELTIGKEIT_PERIODE_FIXED =
        new DateRange(LocalDate.of(2023, 8, 1), LocalDate.of(2024, 7, 31));
}
