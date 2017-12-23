package fr.sharingcraftsman.acceptance.dsl;

public class TokenDsl {
  private String accessToken;
  private String refreshToken;

  public TokenDsl(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
