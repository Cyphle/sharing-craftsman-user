package fr.sharingcraftsman.user.domain.user;

import java.time.LocalDateTime;

public class ChangePasswordToken {
  private final User user;
  private final String token;
  private LocalDateTime expirationDate;

  private ChangePasswordToken(User user, String token, LocalDateTime expirationDate) {
    this.user = user;
    this.token = token;
    this.expirationDate = expirationDate;
  }

  public String getUsername() {
    return user.getUsername();
  }

  public String getToken() {
    return token;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public static ChangePasswordToken from(User user, String token, LocalDateTime expirationDate) {
    return new ChangePasswordToken(user, token, expirationDate);
  }
}
