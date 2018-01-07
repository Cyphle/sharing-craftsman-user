package fr.sharingcraftsman.user.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
  public static LocalDateTime fromLongToLocalDateTime(long toConvert) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(toConvert), ZoneId.systemDefault());
  }

  public static LocalDateTime fromDateToLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }
}
