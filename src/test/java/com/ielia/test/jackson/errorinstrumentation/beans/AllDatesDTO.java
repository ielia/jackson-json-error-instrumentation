package com.ielia.test.jackson.errorinstrumentation.beans;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ielia.test.jackson.errorinstrumentation.dates.ExtendedDate;
import com.ielia.test.jackson.errorinstrumentation.serializers.HijrahDateSerializer;
import com.ielia.test.jackson.errorinstrumentation.serializers.JapaneseDateSerializer;
import com.ielia.test.jackson.errorinstrumentation.serializers.MinguoDateSerializer;
import com.ielia.test.jackson.errorinstrumentation.serializers.ThaiBuddhistDateSerializer;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AllDatesDTO {
    public static final Clock clock = Clock.fixed(Instant.ofEpochMilli(20L * 12L * 31L * 24L * 60L * 60L * 1000L), ZoneId.of("UTC-12:00"));
    public static final Locale locale = Locale.UK;

    public static class AnyDTO { @Past @PastOrPresent @FutureOrPresent @Future public Date now = new Date(clock.millis()); }
    public static class ExtendedDateDTO { @Past public ExtendedDate now = new ExtendedDate(clock.millis()); }
    public static class NotInThePresentDTO { @Past @Future public Date now = new Date(clock.millis() - 12L * 31L * 24L * 60L * 60L * 1000L); }

    public static class DateDTO { @Past public Date now = new Date(clock.millis()); }
    public static class CalendarDTO {
        @Past public Calendar now;
        {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(clock.getZone()), locale);
            cal.setTime(new Date(clock.millis()));
            now = cal;
        }
    }
    public static class JapaneseImperialCalendarDTO {
        @Past public Calendar now;
        {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(clock.getZone()), new Locale("ja", "JP", "JP"));
            cal.setTime(new Date(clock.millis()));
            now = cal;
        }
    }
    public static class InstantDTO { @Future public Instant now = clock.instant(); }
    public static class LocalDateDTO { @PastOrPresent public LocalDate now = LocalDate.now(clock); }
    public static class LocalDateTimeDTO { @FutureOrPresent public LocalDateTime now = LocalDateTime.now(clock); }
    public static class LocalTimeDTO { @Past @PastOrPresent public LocalTime now = LocalTime.now(clock); }
    public static class MonthDayDTO { @Future @FutureOrPresent public MonthDay now = MonthDay.now(clock); }
    public static class OffsetDateTimeDTO { @Past public OffsetDateTime now = OffsetDateTime.now(clock); }
    public static class OffsetTimeDTO { @Past public OffsetTime now = OffsetTime.now(clock); }
    public static class YearDTO { @Past public Year now = Year.now(clock); }
    public static class YearMonthDTO { @Past public YearMonth now = YearMonth.now(clock); }
    public static class ZonedDateTimeDTO { @Past public ZonedDateTime now = ZonedDateTime.now(clock); }
    public static class HijrahDateDTO { @JsonSerialize(using = HijrahDateSerializer.class) @Past public HijrahDate now = HijrahDate.now(clock); }
    public static class JapaneseDateDTO { @JsonSerialize(using = JapaneseDateSerializer.class) @Past public JapaneseDate now = JapaneseDate.now(clock); }
    public static class MinguoDateDTO { @JsonSerialize(using = MinguoDateSerializer.class) @Past public MinguoDate now = MinguoDate.now(clock); }
    public static class ThaiBuddhistDateDTO { @JsonSerialize(using = ThaiBuddhistDateSerializer.class) @Past public ThaiBuddhistDate now = ThaiBuddhistDate.now(clock); }
}
