package fr.sharingcraftsman.user.api.client;

import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.exceptions.ClientException;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private ClientOrganisation clientOrganisation;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
  }

  public ResponseEntity register(ClientDTO clientDTO) {
    log.info("[ClientService::logout] register: " + clientDTO.getName());

    try {
      log.info("Registering new client: " + clientDTO.getName());
      clientOrganisation.createNewClient(clientDTO.fromApiToDomain(clientDTO));
    } catch (ClientException e) {
      log.warn("Client already exists: " + clientDTO.getName());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
    return ResponseEntity.ok().build();
  }
}
