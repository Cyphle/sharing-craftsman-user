package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.domain.client.Client;

public class ClientPivot {
  public static Client fromApiToDomain(Login login) {
    return Client.from(login.getClient(), login.getClientSecret());
  }
}
