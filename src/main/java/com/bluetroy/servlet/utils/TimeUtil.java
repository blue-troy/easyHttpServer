package com.bluetroy.servlet.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtil {
    private final static DateTimeFormatter RFC822 = DateTimeFormatter.ofPattern("EEE,d MMM yyyy hh:mm:ss Z", Locale.ENGLISH);

    public static String toRFC822(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(RFC822);
    }
}
