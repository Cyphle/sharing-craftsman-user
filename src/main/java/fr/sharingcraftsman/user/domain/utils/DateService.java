package fr.sharingcraftsman.user.domain.utils;

import java.time.LocalDateTime;

public interface DateService {
  LocalDateTime getDayAt(int offsetDayes);
}
