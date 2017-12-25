package fr.sharingcraftsman.user.infrastructure.utils;

import fr.sharingcraftsman.user.infrastructure.adapters.DateService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class DateTimeService implements DateService {
  @Override
  public Date now() {
    return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
  }
}
