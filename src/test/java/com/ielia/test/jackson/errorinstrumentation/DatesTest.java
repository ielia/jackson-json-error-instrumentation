package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.beans.AllDatesDTO;
import com.ielia.test.jackson.errorinstrumentation.mutagens.PastPresentFutureMutagen;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Clock;
import java.util.Locale;

public class DatesTest extends TestNGTest {
    public static final Clock clock = AllDatesDTO.clock;
    public static final Locale locale = Locale.US;

    @Test(groups = "unit")
    public void testNoPossibleMutations() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.AnyDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[0]);
    }

    @Test(groups = "unit")
    public void testPresent() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.NotInThePresentDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.util.Date to PRESENT.", "{\"now\":642816000000}"),
        });
    }

    @Test(groups = "unit")
    public void testExtendedDate() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.ExtendedDateDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed com.ielia.test.jackson.errorinstrumentation.dates.ExtendedDate to FUTURE.", "{\"now\":642988800000}"),
        });
    }

    @Test(groups = "unit")
    public void testDate() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.DateDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.util.Date to FUTURE.", "{\"now\":642988800000}"),
        });
    }

    @Test(groups = "unit")
    public void testCalendar() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.CalendarDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.util.Calendar to FUTURE.", "{\"now\":642988800000}"),
        });
    }

    @Test(groups = "unit")
    public void testJapaneseImperialCalendar() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.JapaneseImperialCalendarDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.util.Calendar to FUTURE.", "{\"now\":642988800000}"),
        });
    }

    @Test(groups = "unit")
    public void testInstant() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.InstantDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.Instant to PAST.", "{\"now\":642643200.000000000}"),
        });
    }

    @Test(groups = "unit")
    public void testLocalDate() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.LocalDateDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.LocalDate to FUTURE.", "{\"now\":[1990,5,17]}"),
        });
    }

    @Test(groups = "unit")
    public void testLocalDateTime() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.LocalDateTimeDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.LocalDateTime to PAST.", "{\"now\":[1990,5,13,12,0]}"),
        });
    }

    @Test(groups = "unit")
    public void testLocalTime() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.LocalTimeDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.LocalTime to FUTURE.", "{\"now\":[12,2]}"),
        });
    }

    @Test(groups = "unit")
    public void testMonthDay() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.MonthDayDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.MonthDay to PAST.", "{\"now\":\"--05-13\"}"),
        });
    }

    @Test(groups = "unit")
    public void testOffsetDateTime() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.OffsetDateTimeDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.OffsetDateTime to FUTURE.", "{\"now\":642988800.000000000}"),
        });
    }

    @Test(groups = "unit")
    public void testOffsetTime() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.OffsetTimeDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.OffsetTime to FUTURE.", "{\"now\":[12,2,\"-12:00\"]}"),
        });
    }

    @Test(groups = "unit")
    public void testYear() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.YearDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.Year to FUTURE.", "{\"now\":1992}"),
        });
    }

    @Test(groups = "unit")
    public void testYearMonth() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.YearMonthDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.YearMonth to FUTURE.", "{\"now\":[1990,7]}"),
        });
    }

    @Test(groups = "unit")
    public void testZonedDateTime() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.ZonedDateTimeDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.ZonedDateTime to FUTURE.", "{\"now\":642988800.000000000}"),
        });
    }

    @Test(groups = "unit")
    public void testHijrahDate() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.HijrahDateDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.chrono.HijrahDate to FUTURE.", "{\"now\":\"Hijrah-umalqura AH 1410-10-22\"}"),
        });
    }

    @Test(groups = "unit")
    public void testJapaneseDate() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.JapaneseDateDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.chrono.JapaneseDate to FUTURE.", "{\"now\":\"Japanese Heisei 2-05-17\"}"),
        });
    }

    @Test(groups = "unit")
    public void testMinguoDate() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.MinguoDateDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.chrono.MinguoDate to FUTURE.", "{\"now\":\"Minguo ROC 79-05-17\"}"),
        });
    }

    @Test(groups = "unit")
    public void testThaiBuddhistDate() {
        Mutation[] actual = new JSONMutationInstrumentator(new AllDatesDTO.ThaiBuddhistDateDTO(), new PastPresentFutureMutagen(clock, locale)).getErrorCombinations().toArray(Mutation[]::new);
        Assert.assertEquals(actual, new Mutation[] {
                new Mutation(0, "/now", PastPresentFutureMutagen.class, "Changed java.time.chrono.ThaiBuddhistDate to FUTURE.", "{\"now\":\"ThaiBuddhist BE 2533-05-17\"}"),
        });
    }
}
