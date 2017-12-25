package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.pivots.ClientPivot;
import fr.sharingcraftsman.user.api.pivots.LoginPivot;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.OAuthAuthenticator;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientAdministrator;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.ports.client.ClientManager;
import fr.sharingcraftsman.user.infrastructure.adapters.ClientAdapter;
import fr.sharingcraftsman.user.infrastructure.adapters.DateService;
import fr.sharingcraftsman.user.infrastructure.adapters.TokenAdapter;
import fr.sharingcraftsman.user.infrastructure.adapters.UserAdapter;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.TokenRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
  private ClientManager clientManager;
  private Authenticator authenticator;

  @Autowired
  public LoginService(UserRepository userRepository, TokenRepository tokenRepository, ClientRepository clientRepository, DateService dateService) {
    HumanResourceAdministrator humanResourceAdministrator = new UserAdapter(userRepository, dateService);
    TokenAdministrator tokenAdministrator = new TokenAdapter(tokenRepository, dateService);
    authenticator = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator);

    ClientStock clientStock = new ClientAdapter(clientRepository);
    clientManager = new ClientAdministrator(clientStock);
  }

  public ResponseEntity login(Login login) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(login))) {
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      Credentials credentials = LoginPivot.fromApiToDomain(login);
      Client client = ClientPivot.fromApiToDomain(login);
      return ResponseEntity.ok(authenticator.login(credentials, client));
    } catch (CredentialsException | CollaboratorException e) {
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }
}
