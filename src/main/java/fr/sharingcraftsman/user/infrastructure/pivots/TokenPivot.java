package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenPivot {
  public static AccessTokenEntity fromDomainToInfra(User user, Client client, ValidToken token) {
    AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
    accessTokenEntity.setClient(client.getName());
    accessTokenEntity.setUsername(user.getUsername());
    accessTokenEntity.setAccessToken(token.getAccessToken());
    accessTokenEntity.setRefreshToken(token.getRefreshToken());
    accessTokenEntity.setExpirationDate(Date.from(token.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant()));
    return accessTokenEntity;
  }

  public static ValidToken fromInfraToDomain(AccessTokenEntity accessTokenEntity) {
    return new ValidToken(accessTokenEntity.getAccessToken(), accessTokenEntity.getRefreshToken(), LocalDateTime.ofInstant(accessTokenEntity.getExpirationDate().toInstant(), ZoneId.systemDefault()));
  }
}
