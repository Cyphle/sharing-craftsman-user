package fr.sharingcraftsman.user.domain.user;

import java.time.LocalDateTime;

public class ChangePasswordKey {
  private final User user;
  private final String key;
  private LocalDateTime expirationDate;

  public ChangePasswordKey(User user, String key, LocalDateTime expirationDate) {
    this.user = user;
    this.key = key;
    this.expirationDate = expirationDate;
  }

  public String getUsername() {
    return user.getUsername();
  }

  public String getKey() {
    return key;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public static ChangePasswordKey from(User user, String key, LocalDateTime expirationDate) {
    return new ChangePasswordKey(user, key, expirationDate);
  }
}
