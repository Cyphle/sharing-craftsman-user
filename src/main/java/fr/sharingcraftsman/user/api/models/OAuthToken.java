package fr.sharingcraftsman.user.api.models;

import lombok.ToString;

@ToString
public class OAuthToken {
  private String accessToken;
  private String refreshToken;
  private long expirationDate;

  public OAuthToken() {
  }

  public OAuthToken(String accessToken, String refreshToken, long expirationDate) {
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

  public long getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(long expirationDate) {
    this.expirationDate = expirationDate;
  }
}
