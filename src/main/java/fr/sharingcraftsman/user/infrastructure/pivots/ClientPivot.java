package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.infrastructure.models.ClientEntity;

public class ClientPivot {
  public static ClientEntity fromDomainToInfra(Client client) {
    return new ClientEntity(client.getName(), client.getSecret());
  }

  public static Client fromInfraToDomain(ClientEntity ClientEntity) {
    return Client.from(ClientEntity.getName(), ClientEntity.getSecret());
  }
}
