package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenPivot {
  public static AccessTokenEntity fromDomainToInfra(User user, Client client, AccessToken token) {
    AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
    accessTokenEntity.setClient(client.getName());
    accessTokenEntity.setUsername(user.getUsername());
    accessTokenEntity.setAccessToken(token.getAccessToken());
    accessTokenEntity.setRefreshToken(token.getRefreshToken());
    accessTokenEntity.setExpirationDate(DateConverter.fromLocalDateTimeToDate(token.getExpirationDate()));
    return accessTokenEntity;
  }

  public static AccessToken fromInfraToDomain(AccessTokenEntity accessTokenEntity) {
    return AccessToken.from(accessTokenEntity.getAccessToken(), accessTokenEntity.getRefreshToken(), DateConverter.fromDateToLocalDateTime(accessTokenEntity.getExpirationDate()));
  }
}
