package fr.sharingcraftsman.user.common;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class DateTimeService implements DateService {
  @Override
  public Date nowInDate() {
    return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
  }

  @Override
  public LocalDateTime now() {
    return LocalDateTime.now();
  }

  @Override
  public LocalDateTime getDayAt(int offsetDays) {
    return LocalDateTime.now().plusDays(offsetDays);
  }
}
