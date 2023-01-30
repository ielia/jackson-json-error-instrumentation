package com.ielia.test.jackson.errorinstrumentation.mutagens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.ielia.test.jackson.errorinstrumentation.MutationIndexIndicator;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.Clock;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Supplier;

public class PastPresentFutureMutagen implements Mutagen {
    public enum Timeframe {
        PAST,
        PRESENT,
        FUTURE,
    }

    protected Clock clock;
    protected Locale locale;

    /**
     * Picker functions: 0 = Past, 1 = Future, 2 = Present (i.e., Timeframe.X.ordinal())
     */
    protected Map<Class<?>, Supplier<Object>[]> timePickersByType = new HashMap<>();

    public PastPresentFutureMutagen() {
        this(Clock.systemDefaultZone(), Locale.getDefault());
    }

    public PastPresentFutureMutagen(Clock clock) {
        this(clock, Locale.getDefault());
    }

    public PastPresentFutureMutagen(TimeZone timeZone) {
        this(Clock.system(timeZone.toZoneId()));
    }

    public PastPresentFutureMutagen(TimeZone timeZone, Locale locale) {
        this(Clock.system(timeZone.toZoneId()), locale);
    }

    public PastPresentFutureMutagen(ZoneId zoneId) {
        this(Clock.system(zoneId));
    }

    public PastPresentFutureMutagen(ZoneId zoneId, Locale locale) {
        this(Clock.system(zoneId), locale);
    }

    @SuppressWarnings("unchecked")
    public PastPresentFutureMutagen(Clock clock, Locale locale) {
        this.clock = clock;
        this.locale = locale;
        timePickersByType.put(java.util.Date.class, new Supplier[] { () -> getDate(getDaysAgo()), () -> getDate(getNow()), () -> getDate(getDaysLater()) });
        timePickersByType.put(java.util.Calendar.class, new Supplier[] { () -> getCalendar(getDaysAgo()), () -> getCalendar(getNow()), () -> getCalendar(getDaysLater()) });
        timePickersByType.put(java.time.Instant.class, new Supplier[] { () -> getDaysAgo().toInstant(), () -> getNow().toInstant(), () -> getDaysLater().toInstant() });
        timePickersByType.put(java.time.LocalDate.class, new Supplier[] { () -> getDaysAgo().toLocalDate(), () -> getNow().toLocalDate(), () -> getDaysLater().toLocalDate() });
        timePickersByType.put(java.time.LocalDateTime.class, new Supplier[] { () -> getDaysAgo().toLocalDateTime(), () -> getNow().toLocalDateTime(), () -> getDaysLater().toLocalDateTime() });
        timePickersByType.put(java.time.LocalTime.class, new Supplier[] { () -> getMinutesAgo().toLocalTime(), () -> getNow().toLocalTime(), () -> getMinutesLater().toLocalTime() });
        timePickersByType.put(java.time.MonthDay.class, new Supplier[] { () -> MonthDay.from(getDaysAgo()), () -> MonthDay.from(getNow()), () -> MonthDay.from(getDaysLater()) });
        timePickersByType.put(java.time.OffsetDateTime.class, new Supplier[] { () -> getDaysAgo().toOffsetDateTime(), () -> getNow().toOffsetDateTime(), () -> getDaysLater().toOffsetDateTime() });
        timePickersByType.put(java.time.OffsetTime.class, new Supplier[] { () -> getMinutesAgo().toOffsetDateTime().toOffsetTime(), () -> getNow().toOffsetDateTime().toOffsetTime(), () -> getMinutesLater().toOffsetDateTime().toOffsetTime() });
        timePickersByType.put(java.time.Year.class, new Supplier[] { () -> Year.from(getYearsAgo()), () -> Year.from(getNow()), () -> Year.from(getYearsLater()) });
        timePickersByType.put(java.time.YearMonth.class, new Supplier[] { () -> YearMonth.from(getMonthsAgo()), () -> YearMonth.from(getNow()), () -> YearMonth.from(getMonthsLater()) });
        timePickersByType.put(java.time.ZonedDateTime.class, new Supplier[] { this::getDaysAgo, this::getNow, this::getDaysLater });
        timePickersByType.put(java.time.chrono.HijrahDate.class, new Supplier[] { () -> HijrahDate.from(getDaysAgo()), () -> HijrahDate.from(getNow()), () -> HijrahDate.from(getDaysLater()) });
        timePickersByType.put(java.time.chrono.JapaneseDate.class, new Supplier[] { () -> JapaneseDate.from(getDaysAgo()), () -> JapaneseDate.from(getNow()), () -> JapaneseDate.from(getDaysLater()) });
        timePickersByType.put(java.time.chrono.MinguoDate.class, new Supplier[] { () -> MinguoDate.from(getDaysAgo()), () -> MinguoDate.from(getNow()), () -> MinguoDate.from(getDaysLater()) });
        timePickersByType.put(java.time.chrono.ThaiBuddhistDate.class, new Supplier[] { () -> ThaiBuddhistDate.from(getDaysAgo()), () -> ThaiBuddhistDate.from(getNow()), () -> ThaiBuddhistDate.from(getDaysLater()) });
    }

    @Override
    public boolean serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, Class<?>... groups) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsField(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indicator, Class<?>... groups) throws Exception {
        Timeframe timeframe = getTimeframe(writer, groups);
        if (timeframe == null) { return false; }

        Supplier<Object>[] timePickers = null;
        Class<?> fieldClass = writer.getType().getRawClass();
        while (timePickers == null && fieldClass != Object.class) {
            timePickers = timePickersByType.get(fieldClass);
            fieldClass = fieldClass.getSuperclass();
        }
        if (timePickers == null) { return false; }

        if (indicator.targetMutationIndex != indicator.currentMutationIndex++) { return false; }

        Object newValue = timePickers[timeframe.ordinal()].get();
        // TODO: See if this serializer below will ever need contextualization.
        gen.writeFieldName(writer.getName());
        JsonSerializer<Object> serializer = ((BeanPropertyWriter) writer).getSerializer();
        if (serializer == null) {
            serializer = provider.findValueSerializer(writer.getType());
        }
        serializer.serialize(newValue, gen, provider);
        indicator.setDescription("Changed " + writer.getType().toCanonical() + " to " + timeframe.name() + ".");
        indicator.setMutagen(this.getClass());
        indicator.setPath(gen.getOutputContext().pathAsPointer().toString());
        return true;
    }

    @Override
    public boolean serializeAsPrimitiveArray(Object array, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
    }

    @Override
    public boolean serializeAsPrimitiveCollection(Collection<?> collection, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer, MutationIndexIndicator indexIndicator, boolean isField, Class<?>... groups) throws Exception {
        return false;
    }

    /**
     * @param writer Jackson PropertyWriter.
     * @param groups Validation groups.
     * @return Timeframe PAST, PRESENT or FUTURE. Never PRESENT_OR_FUTURE.
     */
    protected Timeframe getTimeframe(PropertyWriter writer, Class<?>[] groups) {
        return getBoundedValue(
                getAppliedAnnotation(Past.class, Past::groups, Past.List.class, Past.List::value, writer, groups) != null,
                getAppliedAnnotation(PastOrPresent.class, PastOrPresent::groups, PastOrPresent.List.class, PastOrPresent.List::value, writer, groups) != null,
                getAppliedAnnotation(FutureOrPresent.class, FutureOrPresent::groups, FutureOrPresent.List.class, FutureOrPresent.List::value, writer, groups) != null,
                getAppliedAnnotation(Future.class, Future::groups, Future.List.class, Future.List::value, writer, groups) != null,
                Timeframe.PAST, Timeframe.PRESENT, Timeframe.FUTURE
        );
    }

    protected Calendar getCalendar(ZonedDateTime zdt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(clock.getZone()), locale);
        cal.setTime(getDate(zdt));
        return cal;
    }

    protected Date getDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Note: locale is ignored
     * @return Year before last ("2 years ago"), same month/day/time as `now()`.
     */
    protected ZonedDateTime getYearsAgo() {
        return ZonedDateTime.now(clock).minusYears(2);
    }

    /**
     * Note: locale is ignored
     * @return Month before last ("2 months ago"), same day/time as `now()`.
     */
    protected ZonedDateTime getMonthsAgo() {
        return ZonedDateTime.now(clock).minusMonths(2);
    }

    /**
     * Note: locale is ignored
     * @return Day before yesterday, same time as `now()`.
     */
    protected ZonedDateTime getDaysAgo() {
        return ZonedDateTime.now(clock).minusDays(2);
    }

    /**
     * Note: locale is ignored
     * @return Minute before last ("2 minutes ago").
     */
    protected ZonedDateTime getMinutesAgo() {
        return ZonedDateTime.now(clock).minusMinutes(2);
    }

    /**
     * Note: locale is ignored
     * @return Present date-time.
     */
    protected ZonedDateTime getNow() {
        return ZonedDateTime.now(clock);
    }

    /**
     * Note: locale is ignored
     * @return Minute after next ("in 2 minutes").
     */
    protected ZonedDateTime getMinutesLater() {
        return ZonedDateTime.now(clock).plusMinutes(2);
    }

    /**
     * Note: locale is ignored
     * @return Day after tomorrow, same time as `now()`.
     */
    protected ZonedDateTime getDaysLater() {
        return ZonedDateTime.now(clock).plusDays(2);
    }

    /**
     * Note: locale is ignored
     * @return Month after next ("in 2 months"), same day/time as `now()`.
     */
    protected ZonedDateTime getMonthsLater() {
        return ZonedDateTime.now(clock).plusMonths(2);
    }

    /**
     * Note: locale is ignored
     * @return Year after next ("in 2 years"), same month/day/time as `now()`.
     */
    protected ZonedDateTime getYearsLater() {
        return ZonedDateTime.now(clock).plusYears(2);
    }
}
