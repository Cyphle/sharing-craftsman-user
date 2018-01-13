package fr.sharingcraftsman.user.common;

import java.time.*;
import java.util.Date;

public class DateConverter {
  public static LocalDateTime fromLongToLocalDateTime(long toConvert) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(toConvert), ZoneId.systemDefault());
  }

  public static LocalDateTime fromDateToLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public static Date fromLocalDateTimeToDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static long fromLocalDateTimeToLong(LocalDateTime localDateTime) {
    ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
    return zdt.toInstant().toEpochMilli();
  }
}
