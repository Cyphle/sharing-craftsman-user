package fr.sharingcraftsman.user.domain.client.ports;

import fr.sharingcraftsman.user.domain.client.AbstractClient;
import fr.sharingcraftsman.user.domain.client.Client;

public interface ClientRepository {
  AbstractClient findClient(Client client);

  AbstractClient findClientByName(Client client);

  Client createClient(Client client);
}
