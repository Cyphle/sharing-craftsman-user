package fr.sharingcraftsman.user.domain.client.ports;

import fr.sharingcraftsman.user.domain.client.Client;

public interface ClientRepository {
  Client findClient(Client client);

  Client findClientByName(Client client);

  Client createClient(Client client);
}
