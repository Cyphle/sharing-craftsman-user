package fr.sharingcraftsman.user.domain.utils;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

public interface DateHelper {
  LocalDateTime getDayAt(int offsetDayes);

  LocalDateTime now();
}
