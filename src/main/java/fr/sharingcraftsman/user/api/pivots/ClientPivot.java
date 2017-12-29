package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.OAuthClient;
import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.domain.client.Client;

public class ClientPivot {
  public static Client fromApiToDomain(OAuthClient oAuthClient) {
    return Client.from(oAuthClient.getName(), oAuthClient.getSecret());
  }
}
