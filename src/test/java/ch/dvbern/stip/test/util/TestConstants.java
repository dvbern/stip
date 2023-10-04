package ch.dvbern.stip.test.util;

import ch.dvbern.stip.api.common.entity.DateRange;

import java.time.LocalDate;
import java.util.UUID;

public final class TestConstants {

	public static final String GESUCHSTELLER_TEST_ID = "9477487f-3ac4-4d02-b57c-e0cefb292ae5";
	public static final String GESUCHSTELLER_TEST_AHV_NUMMER = "756.1234.5678.90";

	public static final String GESUCHSTELLER_2_TEST_ID = "9477487f-3ac4-4d02-b57c-e0cefb292ae3";
	public static final String GESUCHSTELLER_2_TEST_AHV_NUMMER = "756.9217.0769.85";

	public static final String FALL_TEST_ID ="4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b";

	public static final UUID GESUCHSPERIODE_TEST_ID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
	public static final UUID AUSBILDUNGSSTAETTE_ID = UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5");

	public static final String TEST_FILE_LOCATION = "./src/test/resources/testUpload.txt";
	public static final String TEST_XML_FILE_LOCATION = "./src/test/resources/testUpload.xml";

	public static final String AHV_NUMMER_VALID = "756.9217.0769.85";
	public static final String AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG = "756.1111.1113.11";
	public static final String AHV_NUMMER_VALID_MUTTER = "756.1111.1111.13";
	public static final String AHV_NUMMER_VALID_VATTER = "756.1111.1114.10";
	public static final String AHV_NUMMER_VALID_PARTNER = "756.1111.1112.12";

	public static final DateRange GUELTIGKEIT_PERIODE_23_24 =
			new DateRange(LocalDate.of(2023, 8, 1), LocalDate.of(2024, 7, 31));
}
