package fr.sharingcraftsman.user.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateConverter {
  public static LocalDateTime fromLongToLocalDateTime(long toConvert) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(toConvert), ZoneId.systemDefault());
  }
}
