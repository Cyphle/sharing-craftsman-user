package fr.sharingcraftsman.user.domain.client.ports;

import fr.sharingcraftsman.user.domain.client.BaseClient;
import fr.sharingcraftsman.user.domain.client.Client;

public interface ClientRepository {
  BaseClient findClient(Client client);

  BaseClient findClientByName(Client client);

  Client createClient(Client client);
}
