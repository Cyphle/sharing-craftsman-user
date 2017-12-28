package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TokenPivot {
  public static OAuthToken fromDomainToApi(ValidToken token, Credentials credentials) {
    OAuthToken apiToken = new OAuthToken();
    apiToken.setAccessToken(token.getAccessToken());
    apiToken.setRefreshToken(token.getRefreshToken());
    apiToken.setUsername(credentials.getUsernameContent());

    ZonedDateTime zdt = token.getExpirationDate().atZone(ZoneId.systemDefault());
    apiToken.setExpirationDate(zdt.toInstant().toEpochMilli());
    return apiToken;
  }
}
