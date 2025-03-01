package id.overridestudio.tixfestapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    public static final String PATTERN_LOCAL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_LOCAL_DATE_TIME);
        if (dateTimeString.length() == 10) {
            dateTimeString = dateTimeString + " 00:00:00";
        }
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_LOCAL_DATE_TIME);
        return dateTimeFormatter.format(localDateTime);
    }

    public static void formatValidation(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_LOCAL_DATE_TIME);
        if (dateTimeString.length() == 10) {
            dateTimeString = dateTimeString + " 00:00:00";
        }
        try {
            dateTimeFormatter.parse(dateTimeString);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Expected format: yyyy-MM-dd");
        }
    }

}
