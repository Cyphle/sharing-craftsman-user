package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.api.pivots.TokenPivot;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.OAuthAuthenticator;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientAdministrator;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.ports.client.ClientManager;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import fr.sharingcraftsman.user.infrastructure.adapters.ClientAdapter;
import fr.sharingcraftsman.user.infrastructure.adapters.TokenAdapter;
import fr.sharingcraftsman.user.infrastructure.adapters.UserAdapter;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.TokenRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
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
  private Authenticator authenticator;

  @Autowired
  public TokenService(UserRepository userRepository, TokenRepository tokenRepository, DateService dateService) {
    HumanResourceAdministrator humanResourceAdministrator = new UserAdapter(userRepository, dateService);
    TokenAdministrator tokenAdministrator = new TokenAdapter(tokenRepository);
    authenticator = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator, dateService);
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
}
