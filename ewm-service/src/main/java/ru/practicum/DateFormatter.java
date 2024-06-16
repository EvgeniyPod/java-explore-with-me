package ru.practicum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime toLocalDateTime(String date) {
        return LocalDateTime.parse(date, DATE_FORMAT);
    }

    public static String toString(LocalDateTime date) {
        return date.format(DATE_FORMAT);
    }
}