package org.example.common.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static ZoneId istanbulEurope = ZoneId.of("Europe/Istanbul");

    public static Date nowAsDate()
    {
        Instant instant = Instant.now();
        return Date.from(instant.atZone(istanbulEurope).toInstant());
    }

    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addWeeks(final Date date, final int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date addMinutes(final Date date, final int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static Date addSeconds(final Date date, final int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    public static Date addMilliseconds(final Date date, final int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

    public static Date setYears(final Date date, final int amount) {
        return set(date, Calendar.YEAR, amount);
    }

    public static Date setDays(final Date date, final int amount) {
        return set(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date setHours(final Date date, final int amount) {
        return set(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date setMinutes(final Date date, final int amount) {
        return set(date, Calendar.MINUTE, amount);
    }

    public static Date setSeconds(final Date date, final int amount) {
        return set(date, Calendar.SECOND, amount);
    }

    public static Date setMilliseconds(final Date date, final int amount) {
        return set(date, Calendar.MILLISECOND, amount);
    }

    private static Date set(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        // getInstance() returns a new object, so this method is thread safe.
        final Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(calendarField, amount);
        return c.getTime();
    }

    private static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    public static long differenceBetweenTimestamps(Long startTime, Long endTime, TimeUnit unit) {
        if(startTime > endTime) {
            return -1L;
        }

        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        long diffInMilliseconds = endTime - startTime;

        switch (unit) {
            case DAYS:
                return TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            case HOURS:
                return TimeUnit.HOURS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            case MINUTES:
                return TimeUnit.MINUTES.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            case SECONDS:
                return TimeUnit.SECONDS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            case MICROSECONDS:
                return TimeUnit.MICROSECONDS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            case NANOSECONDS:
                return TimeUnit.NANOSECONDS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            case MILLISECONDS:
            default:
                return diffInMilliseconds;
        }
    }

    public static long differenceBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        return differenceBetweenTimestamps(startDate.getTime(), endDate.getTime(), unit);
    }
}
