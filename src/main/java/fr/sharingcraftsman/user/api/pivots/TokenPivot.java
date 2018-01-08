package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.common.Username;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TokenPivot {
  public static TokenDTO fromDomainToApi(AccessToken token, Username username) {
    TokenDTO apiToken = new TokenDTO();
    apiToken.setAccessToken(token.getAccessToken());
    apiToken.setRefreshToken(token.getRefreshToken());
    apiToken.setUsername(username.getUsername());

    ZonedDateTime zdt = token.getExpirationDate().atZone(ZoneId.systemDefault());
    apiToken.setExpirationDate(zdt.toInstant().toEpochMilli());
    return apiToken;
  }

  public static AccessToken fromApiToDomain(TokenDTO token) {
    return AccessToken.from(token.getAccessToken(), token.getRefreshToken(), DateConverter.fromLongToLocalDateTime(token.getExpirationDate()));
  }
}
