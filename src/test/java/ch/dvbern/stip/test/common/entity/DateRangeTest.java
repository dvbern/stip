package ch.dvbern.stip.test.common.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.entity.DateRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateRangeTest {
	private LocalDate von = LocalDate.of(2022,12,1);
	private LocalDate bis = LocalDate.of(2023,8,31);

	private DateRange dateRange = new DateRange();
	private DateRange otherDateRange = new DateRange(von, bis);
	private DateRange sameOtherDateRange = new DateRange(von, bis);
	@Test
	public void testDateRangeEquality(){
		Assertions.assertEquals(false, dateRange.equals(otherDateRange));
		Assertions.assertEquals(true, otherDateRange.equals(sameOtherDateRange));
	}

	@Test
	public void testDateRangeHashCode(){
		Assertions.assertNotEquals(dateRange.hashCode(), otherDateRange.hashCode());
		Assertions.assertEquals(otherDateRange.hashCode(), sameOtherDateRange.hashCode());
	}

	@Test
	public void testDateRangeEqualitySameHashCodeEquality(){
		Assertions.assertEquals(dateRange.equals(otherDateRange), dateRange.hashCode() == otherDateRange.hashCode());
		Assertions.assertEquals(otherDateRange.equals(sameOtherDateRange), otherDateRange.hashCode() == sameOtherDateRange.hashCode());
	}
}
