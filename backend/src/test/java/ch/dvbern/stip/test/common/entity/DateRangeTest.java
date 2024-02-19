package ch.dvbern.stip.test.common.entity;

import ch.dvbern.stip.api.common.entity.DateRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class DateRangeTest {
	private final LocalDate von = LocalDate.of(2022,12,1);
	private final LocalDate bis = LocalDate.of(2023,8,31);

	private final DateRange dateRange = new DateRange();
	private final DateRange otherDateRange = new DateRange(von, bis);
	private final DateRange sameOtherDateRange = new DateRange(von, bis);
	@Test
	void testDateRangeEquality(){
		Assertions.assertEquals(false, dateRange.equals(otherDateRange));
		Assertions.assertEquals(true, otherDateRange.equals(sameOtherDateRange));
	}

	@Test
	void testDateRangeHashCode(){
		Assertions.assertNotEquals(dateRange.hashCode(), otherDateRange.hashCode());
		Assertions.assertEquals(otherDateRange.hashCode(), sameOtherDateRange.hashCode());
	}

	@Test
	void testDateRangeEqualitySameHashCodeEquality(){
		Assertions.assertEquals(dateRange.equals(otherDateRange), dateRange.hashCode() == otherDateRange.hashCode());
		Assertions.assertEquals(otherDateRange.equals(sameOtherDateRange), otherDateRange.hashCode() == sameOtherDateRange.hashCode());
	}

	@Test
	void containsOneDayBevorRange() {
		LocalDate dateToTest = bis.plusDays(1);
		DateRange range = new DateRange(von, bis);
		Assertions.assertFalse(range.contains(dateToTest));
	}

	@Test
	void containsOneDayAfterRange() {
		LocalDate dateToTest = von.minusDays(1);
		DateRange range = new DateRange(von, bis);
		Assertions.assertFalse(range.contains(dateToTest));
	}

	@Test
	void containsFirstDayOfRange() {
		DateRange range = new DateRange(von, bis);
		Assertions.assertTrue(range.contains(von));
	}

	@Test
	void containsLastDayOfRange() {
		DateRange range = new DateRange(von, bis);
		Assertions.assertTrue(range.contains(bis));
	}
}
