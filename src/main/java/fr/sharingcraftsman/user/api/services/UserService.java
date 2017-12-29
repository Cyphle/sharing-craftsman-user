package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.models.OAuthClient;
import fr.sharingcraftsman.user.api.pivots.ClientPivot;
import fr.sharingcraftsman.user.api.pivots.LoginPivot;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.client.ClientAdministrator;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Organisation;
import fr.sharingcraftsman.user.domain.ports.client.ClientManager;
import fr.sharingcraftsman.user.domain.ports.company.Company;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private Company company;
  private ClientManager clientManager;

  @Autowired
  public UserService(HumanResourceAdministrator humanResourceAdministrator, ClientStock clientStock) {
    company = new Organisation(humanResourceAdministrator);
    clientManager = new ClientAdministrator(clientStock, new SimpleSecretGenerator());
  }

  public ResponseEntity registerUser(OAuthClient oAuthClient, Login login) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(oAuthClient))) {
      log.warn("User " + login.getUsername() + " is trying to log in with unauthorized client: " + oAuthClient.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("User is registering with username:" + login.getUsername());
      Credentials credentials = LoginPivot.fromApiToDomainWithEncryption(login);
      company.createNewCollaborator(credentials);
      return ResponseEntity.ok().build();
    } catch (CredentialsException | CollaboratorException e) {
      log.warn("Error with registering " + login.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }
}
