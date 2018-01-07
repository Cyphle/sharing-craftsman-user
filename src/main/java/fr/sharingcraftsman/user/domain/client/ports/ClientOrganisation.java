package fr.sharingcraftsman.user.domain.client.ports;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.exceptions.ClientException;

public interface ClientOrganisation {
  boolean clientExists(Client client);

  void createNewClient(Client client) throws ClientException;
}
