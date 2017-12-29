package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.models.OAuthClient;
import fr.sharingcraftsman.user.api.models.OAuthToken;
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
  public TokenService(HumanResourceAdministrator humanResourceAdministrator, TokenAdministrator tokenAdministrator, ClientStock clientStock, DateService dateService) {
    authenticator = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator, dateService);
    clientManager = new ClientAdministrator(clientStock, new SimpleSecretGenerator());
  }

  public ResponseEntity login(OAuthClient oAuthClient, Login login) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(oAuthClient))) {
      log.warn("User " + login.getUsername() + " is trying to log in with unauthorized client: " + oAuthClient.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("User " + login.getUsername() + " is logging");
      Credentials credentials = LoginPivot.fromApiToDomainWithEncryption(login);
      Client client = ClientPivot.fromApiToDomain(oAuthClient);
      OAuthToken token = TokenPivot.fromDomainToApi((ValidToken) authenticator.login(credentials, client), credentials);
      return ResponseEntity.ok(token);
    } catch (CredentialsException | CollaboratorException e) {
      log.warn("Error with login " + login.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity checkToken(OAuthToken token) {
    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(token.getUsername()), null, false);
      Client client = new Client(token.getClient(), "", false);

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

  public ResponseEntity logout(OAuthToken token) {
    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(token.getUsername()), null, false);
      Client client = new Client(token.getClient(), "", false);
      authenticator.logout(credentials, client, TokenPivot.fromApiToDomain(token));
      return ResponseEntity.ok().build();
    } catch (CredentialsException e) {
      log.warn("Error with log out " + token.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }
}
