package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.pivots.ClientPivot;
import fr.sharingcraftsman.user.api.pivots.LoginPivot;
import fr.sharingcraftsman.user.api.pivots.TokenPivot;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.*;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
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
  private ClientOrganisation clientOrganisation;
  private AuthenticationManager authenticationManager;

  @Autowired
  public TokenService(
          UserRepository userRepository,
          AccessTokenRepository accessTokenRepository,
          ClientRepository clientRepository,
          DateService dateService) {
    authenticationManager = new AuthenticationManagerImpl(userRepository, accessTokenRepository, dateService);
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
  }

  public ResponseEntity login(ClientDTO clientDTO, LoginDTO loginDTO) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + loginDTO.getUsername() + " is trying to log in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("UserEntity " + loginDTO.getUsername() + " is logging");
      Credentials credentials = LoginPivot.fromApiToDomainWithEncryption(loginDTO);
      Client client = ClientPivot.fromApiToDomain(clientDTO);
      TokenDTO token = TokenPivot.fromDomainToApi((AccessToken) authenticationManager.login(credentials, client), credentials);
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
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + token.getUsername() + " is trying to check token in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(token.getUsername()), null, false);
      Client client = new Client(clientDTO.getName(), "", false);

      if (authenticationManager.isTokenValid(credentials, client, TokenPivot.fromApiToDomain(token))) {
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
      authenticationManager.logout(credentials, client, TokenPivot.fromApiToDomain(token));
      return ResponseEntity.ok().build();
    } catch (CredentialsException e) {
      log.warn("Error with log out " + token.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity refreshToken(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to refresh token with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(tokenDTO.getUsername()), null, false);
      Client client = new Client(clientDTO.getName(), "", false);
      if (authenticationManager.isRefreshTokenValid(credentials, client, TokenPivot.fromApiToDomain(tokenDTO))) {
        authenticationManager.deleteToken(credentials, client, TokenPivot.fromApiToDomain(tokenDTO));
        TokenDTO token = TokenPivot.fromDomainToApi((AccessToken) authenticationManager.createNewToken(credentials, client), credentials);
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


    /*
      - Verify refresh token is ok
      - Delete existing token
      - Create new token
     */
  }
}
