package fr.sharingcraftsman.user.domain.ports.client;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientException;

public interface ClientManager {
  boolean clientExists(Client client);

  void createNewClient(Client client) throws ClientException;
}
