package com.example.angel.sqawker.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

public class DateUtils {

    public static String getDaysAgoString(DateTime dateTime) {

        DateTime dateTimeNow = DateTime.now();

        Period diff = new Period(dateTimeNow, dateTime);
        Duration duration = diff.toStandardDuration();

        if (duration.getStandardHours() == 0) return duration.getStandardMinutes() + " m";
        else if (duration.getStandardDays() == 0) return duration.getStandardHours() + " h";
        else return duration.getStandardDays() + " dias";
    }
}
