package fr.sharingcraftsman.user.domain.client;

import fr.sharingcraftsman.user.domain.ports.client.ClientManager;

public class ClientAdministrator implements ClientManager {
  private ClientStock clientStock;

  public ClientAdministrator(ClientStock clientStock) {
    this.clientStock = clientStock;
  }

  @Override
  public boolean clientExists(Client client) {
    return clientStock.findClient(client).isKnown();
  }
}
