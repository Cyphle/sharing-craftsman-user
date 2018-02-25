package fr.sharingcraftsman.user.api.authentication;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.common.Username;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
public class TokenDTO {
  private String username;
  private String accessToken;
  private String refreshToken;
  private long expirationDate;

  private TokenDTO() {
  }

  public TokenDTO(String username) {
    this.username = username;
  }

  private TokenDTO(String username, String accessToken) {
    this.username = username;
    this.accessToken = accessToken;
  }

  private TokenDTO(String username, String accessToken, String refreshToken, long expirationDate) {
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
  }

  private TokenDTO(String username, String accessToken, String refreshToken) {
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

  public static TokenDTO from(String username, String accessToken) {
    return new TokenDTO(username, accessToken);
  }

  public static TokenDTO from(String username) {
    return new TokenDTO(username);
  }

  public static TokenDTO from(String username, String accessToken, String refreshToken) {
    return new TokenDTO(username, accessToken, refreshToken);
  }

  public static TokenDTO from(String username, String accessToken, String refreshToken, long expirationDate) {
    return new TokenDTO(username, accessToken, refreshToken, expirationDate);
  }

  public static TokenDTO fromDomainToApi(AccessToken token, Username username) {
    TokenDTO apiToken = new TokenDTO();
    apiToken.setAccessToken(token.getAccessToken());
    apiToken.setRefreshToken(token.getRefreshToken());
    apiToken.setUsername(username.getUsername());

    apiToken.setExpirationDate(DateConverter.fromLocalDateTimeToLong(token.getExpirationDate()));
    return apiToken;
  }

  public static AccessToken fromApiToDomain(TokenDTO token) {
    return AccessToken.from(token.getAccessToken(), token.getRefreshToken(), DateConverter.fromLongToLocalDateTime(token.getExpirationDate()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TokenDTO tokenDTO = (TokenDTO) o;

    if (username != null ? !username.equals(tokenDTO.username) : tokenDTO.username != null) return false;
    if (accessToken != null ? !accessToken.equals(tokenDTO.accessToken) : tokenDTO.accessToken != null) return false;
    return refreshToken != null ? refreshToken.equals(tokenDTO.refreshToken) : tokenDTO.refreshToken == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
    result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
    return result;
  }
}
