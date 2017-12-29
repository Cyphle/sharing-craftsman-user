package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.domain.client.Client;

public class ClientPivot {
  public static Client fromApiToDomain(ClientDTO clientDTO) {
    return Client.from(clientDTO.getName(), clientDTO.getSecret());
  }
}
