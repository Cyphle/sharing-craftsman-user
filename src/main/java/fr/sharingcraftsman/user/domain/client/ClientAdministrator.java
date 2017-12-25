package fr.sharingcraftsman.user.domain.client;

public class ClientAdministrator {
  private ClientStock clientStock;

  public ClientAdministrator(ClientStock clientStock) {
    this.clientStock = clientStock;
  }

  public boolean clientExists(Client client) {
    return clientStock.findClient(client).isKnown();
  }
}
