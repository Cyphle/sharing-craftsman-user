package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.infrastructure.models.OAuthToken;

import java.time.ZoneId;
import java.util.Date;

public class TokenPivot {
  public static OAuthToken fromDomainToInfra(Collaborator collaborator, Client client, ValidToken token) {
    OAuthToken oAuthToken = new OAuthToken();
    oAuthToken.setClient(client.getName());
    oAuthToken.setUsername(collaborator.getUsername());
    oAuthToken.setAccessToken(token.getAccessToken());
    oAuthToken.setRefreshToken(token.getRefreshToken());
    oAuthToken.setExpirationDate(Date.from(token.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant()));
    return oAuthToken;
  }
}
