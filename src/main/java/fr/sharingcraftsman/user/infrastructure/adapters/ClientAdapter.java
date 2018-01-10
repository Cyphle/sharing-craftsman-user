package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.client.BaseClient;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.UnknownClient;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.infrastructure.models.ClientEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientAdapter implements ClientRepository {
  private ClientJpaRepository clientJpaRepository;

  @Autowired
  public ClientAdapter(ClientJpaRepository clientJpaRepository) {
    this.clientJpaRepository = clientJpaRepository;
  }

  @Override
  public BaseClient findClient(Client client) {
    ClientEntity foundClient = clientJpaRepository.findByNameAndSecret(client.getName(), client.getSecret());

    if (foundClient == null)
      return UnknownClient.get();

    return Client.from(foundClient.getName(), foundClient.getSecret());
  }

  @Override
  public BaseClient findClientByName(Client client) {
    ClientEntity foundClient = clientJpaRepository.findByName(client.getName());

    if (foundClient == null)
      return UnknownClient.get();

    return Client.from(foundClient.getName(), foundClient.getSecret());
  }

  @Override
  public Client createClient(Client client) {
    ClientEntity clientEntity = ClientEntity.fromDomainToInfra(client);
    return ClientEntity.fromInfraToDomain(clientJpaRepository.save(clientEntity));
  }
}
