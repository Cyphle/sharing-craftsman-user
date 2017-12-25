package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientRegistration;
import fr.sharingcraftsman.user.domain.client.ClientAdministrator;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.ports.client.ClientManager;
import fr.sharingcraftsman.user.infrastructure.adapters.ClientAdapter;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
  private ClientManager clientManager;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    ClientStock clientStock = new ClientAdapter(clientRepository);
    clientManager = new ClientAdministrator(clientStock);
  }

  public ResponseEntity register(ClientRegistration clientRegistration) {
    throw new UnsupportedOperationException();
  }
}
