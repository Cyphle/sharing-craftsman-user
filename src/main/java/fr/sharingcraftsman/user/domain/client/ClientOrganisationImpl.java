package fr.sharingcraftsman.user.domain.client;

import fr.sharingcraftsman.user.domain.client.exceptions.AlreadyExistingClientException;
import fr.sharingcraftsman.user.domain.client.exceptions.ClientException;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
import fr.sharingcraftsman.user.domain.utils.SecretGenerator;

public class ClientOrganisationImpl implements ClientOrganisation {
  private ClientRepository clientRepository;
  private SecretGenerator secretGenerator;

  public ClientOrganisationImpl(ClientRepository clientRepository, SecretGenerator secretGenerator) {
    this.clientRepository = clientRepository;
    this.secretGenerator = secretGenerator;
  }

  @Override
  public boolean clientExists(Client client) {
    return clientRepository.findClient(client).isKnown();
  }

  @Override
  public void createNewClient(Client client) throws ClientException {
    AbstractClient foundClient = clientRepository.findClientByName(client);

    if (foundClient.isKnown())
      throw new AlreadyExistingClientException("Already existing client");

    client.setSecret(secretGenerator.generateSecret());
    clientRepository.createClient(client);
  }
}
