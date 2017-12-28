package fr.sharingcraftsman.user.domain.utils;

import java.time.LocalDateTime;

public class LocalDateTimeHelper implements DateHelper {
  @Override
  public LocalDateTime getDayAt(int offsetDayes) {
    return LocalDateTime.now().plusDays(offsetDayes);
  }

  @Override
  public LocalDateTime now() {
    return LocalDateTime.now();
  }
}
