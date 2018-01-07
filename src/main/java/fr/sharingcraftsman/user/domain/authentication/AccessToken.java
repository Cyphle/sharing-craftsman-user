package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.common.DateConverter;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode
public class AccessToken extends BaseToken {
  private String accessToken;
  private String refreshToken;
  private LocalDateTime expirationDate;

  private AccessToken(String accessToken, String refreshToken, LocalDateTime expirationDate) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public static AccessToken from(String accessToken, String refreshToken, LocalDateTime expirationDate) {
    return new AccessToken(accessToken, refreshToken, expirationDate);
  }

  static AccessToken fromOnlyAccessToken(String accessToken) {
    return new AccessToken(accessToken, "", DateConverter.fromLongToLocalDateTime(0));
  }

  public static AccessToken fromOnlyRefreshToken(String refreshToken) {
    return new AccessToken("", refreshToken, DateConverter.fromLongToLocalDateTime(0));
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
