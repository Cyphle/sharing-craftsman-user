package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.infrastructure.models.OAuthClient;

public class ClientPivot {
  public static OAuthClient fromDomainToInfra(Client client) {
    return new OAuthClient(client.getName(), client.getSecret());
  }

  public static Client fromInfraToDomain(OAuthClient OAuthClient) {
    return Client.from(OAuthClient.getName(), OAuthClient.getSecret());
  }
}
