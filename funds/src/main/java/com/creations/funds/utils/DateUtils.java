package com.creations.funds.utils;

import com.creations.funds.models.SchemeData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtils {

    public static final double ONE_DAY_IN_MILLIS = 24.0 * 60.0 * 60.0 * 1000.0;

    public static List<SchemeData> filterDatesWithinHorizon(List<SchemeData> data, int period, int horizon) {
        final var lastDate = data.get(0).getDate();
        final var startDate = getStartDate(lastDate, period, horizon);
        return data.stream()
                .filter(d -> d.getDate().after(startDate))
                .sorted((c, d) -> d.getDate().compareTo(c.getDate()))
                .collect(Collectors.toList());
    }

    private static Date getStartDate(Date lastDate, int period, int horizon) {
        final var c = Calendar.getInstance();
        c.setTime(lastDate);
        c.add(Calendar.YEAR, -1 * (period + horizon));
        return c.getTime();
    }

    public static String getMonth(SchemeData schemeData) {
        return new SimpleDateFormat("MMM-yy").format(schemeData.getDate());
    }
}
