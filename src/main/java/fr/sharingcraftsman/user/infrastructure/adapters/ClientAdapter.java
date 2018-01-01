package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.infrastructure.models.OAuthClient;
import fr.sharingcraftsman.user.infrastructure.pivots.ClientPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientAdapter implements ClientStock {
  private ClientRepository clientRepository;

  @Autowired
  public ClientAdapter(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @Override
  public Client findClient(Client client) {
    OAuthClient foundClient = clientRepository.findByNameAndSecret(client.getName(), client.getSecret());

    if (foundClient == null)
      return Client.unkownClient();

    return Client.knownClient(foundClient.getName(), foundClient.getSecret());
  }

  @Override
  public Client findClientByName(Client client) {
    OAuthClient foundClient = clientRepository.findByName(client.getName());

    if (foundClient == null)
      return Client.unkownClient();

    return Client.knownClient(foundClient.getName(), foundClient.getSecret());
  }

  @Override
  public Client createClient(Client client) {
    OAuthClient OAuthClient = ClientPivot.fromDomainToInfra(client);
    return ClientPivot.fromInfraToDomain(clientRepository.save(OAuthClient));
  }
}
