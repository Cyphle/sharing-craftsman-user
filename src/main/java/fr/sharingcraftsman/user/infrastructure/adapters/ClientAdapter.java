package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.infrastructure.models.ClientEntity;
import fr.sharingcraftsman.user.infrastructure.pivots.ClientPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientAdapter implements ClientStock {
  private ClientJpaRepository clientJpaRepository;

  @Autowired
  public ClientAdapter(ClientJpaRepository clientJpaRepository) {
    this.clientJpaRepository = clientJpaRepository;
  }

  @Override
  public Client findClient(Client client) {
    ClientEntity foundClient = clientJpaRepository.findByNameAndSecret(client.getName(), client.getSecret());

    if (foundClient == null)
      return Client.unkownClient();

    return Client.knownClient(foundClient.getName(), foundClient.getSecret());
  }

  @Override
  public Client findClientByName(Client client) {
    ClientEntity foundClient = clientJpaRepository.findByName(client.getName());

    if (foundClient == null)
      return Client.unkownClient();

    return Client.knownClient(foundClient.getName(), foundClient.getSecret());
  }

  @Override
  public Client createClient(Client client) {
    ClientEntity ClientEntity = ClientPivot.fromDomainToInfra(client);
    return ClientPivot.fromInfraToDomain(clientJpaRepository.save(ClientEntity));
  }
}
