package fr.sharingcraftsman.user.domain.authentication;

import java.time.LocalDateTime;

public class ValidToken extends Token {
  public static final ValidTokenBuilder validTokenBuilder = new ValidTokenBuilder();

  private String accessToken;
  private String refreshToken;
  private LocalDateTime expirationDate;

  public ValidToken(String accessToken, String refreshToken, LocalDateTime expirationDate) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  public static class ValidTokenBuilder {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expirationDate;

    public ValidTokenBuilder withAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    public ValidTokenBuilder withRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
      return this;
    }

    public ValidTokenBuilder expiringThe(LocalDateTime expirationDate) {
      this.expirationDate = expirationDate;
      return this;
    }

    public Token build() {
      return new ValidToken(accessToken, refreshToken, expirationDate);
    }
  }
}
