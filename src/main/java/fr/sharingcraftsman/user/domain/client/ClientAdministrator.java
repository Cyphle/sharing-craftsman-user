package fr.sharingcraftsman.user.domain.client;

import fr.sharingcraftsman.user.domain.ports.client.ClientManager;
import fr.sharingcraftsman.user.domain.utils.SecretGenerator;

public class ClientAdministrator implements ClientManager {
  private ClientStock clientStock;
  private SecretGenerator secretGenerator;

  public ClientAdministrator(ClientStock clientStock, SecretGenerator secretGenerator) {
    this.clientStock = clientStock;
    this.secretGenerator = secretGenerator;
  }

  @Override
  public boolean clientExists(Client client) {
    return clientStock.findClient(client).isKnown();
  }

  @Override
  public void createNewClient(Client client) throws ClientException {
    Client foundClient = clientStock.findClientByName(client);

    if (foundClient.isKnown())
      throw new AlreadyExistingClientException("Already existing client");

    client.setSecret(secretGenerator.generateSecret());
    clientStock.createClient(client);
  }
}
