package fr.sharingcraftsman.user.common;

import java.time.LocalDateTime;
import java.util.Date;

public interface DateService {
  Date nowInDate();

  LocalDateTime now();

  LocalDateTime getDayAt(int offsetDayes);
}
