package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.pivots.ChangePasswordTokenPivot;
import fr.sharingcraftsman.user.api.pivots.ClientPivot;
import fr.sharingcraftsman.user.api.pivots.LoginPivot;
import fr.sharingcraftsman.user.api.pivots.TokenPivot;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.OAuthAuthenticator;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientAdministrator;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.company.ChangePasswordKey;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Organisation;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.ports.client.ClientManager;
import fr.sharingcraftsman.user.domain.ports.company.Company;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

@Service
public class UserService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final Authenticator authenticator;
  private Company company;
  private ClientManager clientManager;

  @Autowired
  public UserService(
          HumanResourceAdministrator humanResourceAdministrator,
          ClientStock clientStock,
          TokenAdministrator tokenAdministrator,
          DateService dateService) {
    company = new Organisation(humanResourceAdministrator, dateService);
    clientManager = new ClientAdministrator(clientStock, new SimpleSecretGenerator());
    authenticator = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator, dateService);
  }

  public ResponseEntity registerUser(ClientDTO clientDTO, LoginDTO loginDTO) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("User " + loginDTO.getUsername() + " is trying to log in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("User is registering with username:" + loginDTO.getUsername());
      Credentials credentials = LoginPivot.fromApiToDomainWithEncryption(loginDTO);
      company.createNewCollaborator(credentials);
      return ResponseEntity.ok().build();
    } catch (CredentialsException | CollaboratorException e) {
      log.warn("Error with registering " + loginDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity requestChangePassword(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("User " + tokenDTO.getUsername() + " is trying to log in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Request for a change password token for:" + tokenDTO.getUsername());
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(tokenDTO.getUsername()), null, false);
      Client client = new Client(clientDTO.getName(), "", false);
      if (!authenticator.isTokenValid(credentials, client, TokenPivot.fromApiToDomain(tokenDTO)))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      ChangePasswordKey changePasswordKey = company.createChangePasswordKeyFor(credentials);
      return ResponseEntity.ok(ChangePasswordTokenPivot.fromDomainToApi(changePasswordKey));
    } catch (UsernameException e) {
      log.warn("Error with change password request " + tokenDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }
}
