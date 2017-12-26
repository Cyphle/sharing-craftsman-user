package fr.sharingcraftsman.user.infrastructure.adapters;

import java.time.LocalDateTime;
import java.util.Date;

public interface DateService {
  Date now();

  LocalDateTime getDayAt(int offsetDayes);
}
