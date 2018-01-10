package fr.sharingcraftsman.user.api.authentication;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authentication.AuthenticationManagerImpl;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private ClientOrganisation clientOrganisation;
  private AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationService(
          UserRepository userRepository,
          AccessTokenRepository accessTokenRepository,
          ClientRepository clientRepository,
          DateService dateService) {
    authenticationManager = new AuthenticationManagerImpl(userRepository, accessTokenRepository, dateService);
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
  }

  public ResponseEntity login(ClientDTO clientDTO, LoginDTO loginDTO) {
    if (!clientOrganisation.clientExists(ClientDTO.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + loginDTO.getUsername() + " is trying to log in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("UserEntity " + loginDTO.getUsername() + " is logging");
      Credentials credentials = LoginDTO.fromApiToDomain(loginDTO);
      Client client = ClientDTO.fromApiToDomain(clientDTO);
      TokenDTO token = TokenDTO.fromDomainToApi((AccessToken) authenticationManager.login(client, credentials), credentials.getUsername());
      return ResponseEntity.ok(token);
    } catch (UnknownUserException e) {
      log.warn("Unauthorized user: " + loginDTO.getUsername() + ": " + e.getMessage());
      return new ResponseEntity<>("Unauthorized collaborator", HttpStatus.UNAUTHORIZED);
    } catch (CredentialsException | UserException e) {
      log.warn("Error with loginDTO " + loginDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity checkToken(ClientDTO clientDTO, TokenDTO token) {
    if (!clientOrganisation.clientExists(ClientDTO.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + token.getUsername() + " is trying to check token in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());
      Client client = Client.from(clientDTO.getName(), clientDTO.getSecret());

      if (authenticationManager.isTokenValid(client, Username.from(token.getUsername()), TokenDTO.fromApiToDomain(token))) {
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
      Client client = Client.from(clientDTO.getName(), "");
      authenticationManager.logout(client, Username.from(token.getUsername()), TokenDTO.fromApiToDomain(token));
      return ResponseEntity.ok().build();
    } catch (CredentialsException e) {
      log.warn("Error with log out " + token.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity refreshToken(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientOrganisation.clientExists(ClientDTO.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to refresh token with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      Username username = Username.from(tokenDTO.getUsername());
      Client client = Client.from(clientDTO.getName(), "");
      if (authenticationManager.isRefreshTokenValid(client, username, TokenDTO.fromApiToDomain(tokenDTO))) {
        authenticationManager.deleteToken(client, username, TokenDTO.fromApiToDomain(tokenDTO));
        TokenDTO token = TokenDTO.fromDomainToApi((AccessToken) authenticationManager.createNewToken(client, username), username);
        return ResponseEntity.ok(token);
      } else {
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
      }
    } catch (CredentialsException | UserException e) {
      log.warn("Error with get new token from refresh token " + tokenDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }
}
