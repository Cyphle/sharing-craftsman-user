package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class TokenDTO {
  private String username;
  private String accessToken;
  private String refreshToken;
  private long expirationDate;

  public TokenDTO() {
  }

  public TokenDTO(String username, String accessToken, String refreshToken, long expirationDate) {
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
  }

  public TokenDTO(String username, String accessToken) {
    this.username = username;
    this.accessToken = accessToken;
  }

  public TokenDTO(String username, String accessToken, String refreshToken) {
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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
