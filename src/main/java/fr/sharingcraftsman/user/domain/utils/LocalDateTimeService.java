package fr.sharingcraftsman.user.domain.utils;

import java.time.LocalDateTime;

public class LocalDateTimeService implements DateService {
  @Override
  public LocalDateTime getDayAt(int offsetDayes) {
    return LocalDateTime.now().plusDays(offsetDayes);
  }
}
