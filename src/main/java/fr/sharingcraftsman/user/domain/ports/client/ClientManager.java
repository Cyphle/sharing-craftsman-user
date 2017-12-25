package fr.sharingcraftsman.user.domain.ports.client;

import fr.sharingcraftsman.user.domain.client.Client;

public interface ClientManager {
  boolean clientExists(Client client);
}
