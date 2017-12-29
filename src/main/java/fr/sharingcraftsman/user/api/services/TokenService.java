package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.pivots.ClientPivot;
import fr.sharingcraftsman.user.api.pivots.LoginPivot;
import fr.sharingcraftsman.user.api.pivots.TokenPivot;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.*;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientAdministrator;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.UnknownCollaboratorException;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.ports.client.ClientManager;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

@Service
public class TokenService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private ClientManager clientManager;
  private Authenticator authenticator;

  @Autowired
  public TokenService(
          HumanResourceAdministrator humanResourceAdministrator,
          TokenAdministrator tokenAdministrator,
          ClientStock clientStock,
          DateService dateService) {
    authenticator = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator, dateService);
    clientManager = new ClientAdministrator(clientStock, new SimpleSecretGenerator());
  }

  public ResponseEntity login(ClientDTO clientDTO, LoginDTO loginDTO) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("User " + loginDTO.getUsername() + " is trying to log in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("User " + loginDTO.getUsername() + " is logging");
      Credentials credentials = LoginPivot.fromApiToDomainWithEncryption(loginDTO);
      Client client = ClientPivot.fromApiToDomain(clientDTO);
      TokenDTO token = TokenPivot.fromDomainToApi((ValidToken) authenticator.login(credentials, client), credentials);
      return ResponseEntity.ok(token);
    } catch (UnknownCollaboratorException e) {
      log.warn("Unauthorized user: " + loginDTO.getUsername() + ": " + e.getMessage());
      return new ResponseEntity<>("Unauthorized collaborator", HttpStatus.UNAUTHORIZED);
    } catch (CredentialsException | CollaboratorException e) {
      log.warn("Error with loginDTO " + loginDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity checkToken(ClientDTO clientDTO, TokenDTO token) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("User " + token.getUsername() + " is trying to check token in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(token.getUsername()), null, false);
      Client client = new Client(clientDTO.getName(), "", false);

      if (authenticator.isTokenValid(credentials, client, TokenPivot.fromApiToDomain(token))) {
        return ResponseEntity.ok().build();
      } else {
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
      }
    } catch (CredentialsException e) {
      log.warn("Error with check token " + token.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity logout(ClientDTO clientDTO, TokenDTO token) {
    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(token.getUsername()), null, false);
      Client client = new Client(clientDTO.getName(), "", false);
      authenticator.logout(credentials, client, TokenPivot.fromApiToDomain(token));
      return ResponseEntity.ok().build();
    } catch (CredentialsException e) {
      log.warn("Error with log out " + token.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity refreshToken(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("User " + tokenDTO.getUsername() + " is trying to refresh token with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(tokenDTO.getUsername()), null, false);
      Client client = new Client(clientDTO.getName(), "", false);
      if (authenticator.isRefreshTokenValid(credentials, client, TokenPivot.fromApiToDomain(tokenDTO))) {

      } else {

      }
    } catch (CredentialsException e) {
      e.printStackTrace();
    }


    /*
      - Verify refresh token is ok
      - Delete existing token
      - Create new token
     */
    throw new UnsupportedOperationException();
  }
}
