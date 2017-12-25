package fr.sharingcraftsman.user.api.models;

import java.time.LocalDateTime;

public class OAuthToken {
  private String accessToken;
  private String refreshToken;
  private LocalDateTime expirationDate;

  public OAuthToken() {
  }

  public OAuthToken(String accessToken, String refreshToken, LocalDateTime expirationDate) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }
}
