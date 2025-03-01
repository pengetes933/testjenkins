package id.overridestudio.tixfestapi.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;


public class DateTimeUtil {
    public static LocalDateTime formatToCustomExpiry(Date date, String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone)); // Set zona waktu
        String formattedDate = sdf.format(date);

        // Konversi ke LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(formattedDate, formatter);
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * Mengembalikan tanggal dan waktu saat ini dalam format yyyy-MM-dd HH:mm:ss Z.
     *
     * @param timeZone Zona waktu yang digunakan (misalnya, "GMT+7").
     * @return LocalDateTime yang berisi tanggal dan waktu saat ini yang diformat.
     */
    public static LocalDateTime getCurrentTimeFormatted(String timeZone) {
        return formatToCustomExpiry(new Date(), timeZone);
    }
}
