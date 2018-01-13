package fr.sharingcraftsman.user.api.authentication;

import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AbstractToken;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authentication.AuthenticationManagerImpl;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private AuthenticationManager authenticationManager;
  private AuthorizationVerifierService authorizationVerifierService;

  @Autowired
  public AuthenticationService(
          UserRepository userRepository,
          AccessTokenRepository accessTokenRepository,
          DateService dateService,
          AuthorizationVerifierService authorizationVerifierService) {
    authenticationManager = new AuthenticationManagerImpl(userRepository, accessTokenRepository, dateService);
    this.authorizationVerifierService = authorizationVerifierService;
  }

  public ResponseEntity login(ClientDTO clientDTO, LoginDTO loginDTO) {
    log.info("[AuthenticationService::login] Client: " + clientDTO.getName() + ", User: " + loginDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      log.info("UserEntity " + loginDTO.getUsername() + " is logging");

      AbstractToken accessToken = authenticationManager.login(ClientDTO.fromApiToDomain(clientDTO), LoginDTO.fromApiToDomain(loginDTO));
      TokenDTO token = TokenDTO.fromDomainToApi((AccessToken) accessToken, Username.from(loginDTO.getUsername()));
      return ResponseEntity.ok(token);
    } catch (CredentialsException | UserException e) {
      log.warn("Error: " + e.getMessage());
      return new ResponseEntity<>("Unauthorized user", HttpStatus.UNAUTHORIZED);
    }
  }

  ResponseEntity checkToken(ClientDTO clientDTO, TokenDTO token) {
    log.info("[AuthenticationService::checkToken] Client: " + clientDTO.getName() + ", User: " + token.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());

      boolean isTokenValid = authenticationManager.isTokenValid(Client.from(clientDTO.getName(), clientDTO.getSecret()), Username.from(token.getUsername()), TokenDTO.fromApiToDomain(token));
      if (isTokenValid) {
        return ResponseEntity.ok().build();
      } else {
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
      }
    } catch (CredentialsException e) {
      return logAndSendBadRequest(e);
    }
  }

  ResponseEntity logout(ClientDTO clientDTO, TokenDTO token) {
    log.info("[AuthenticationService::logout] Client: " + clientDTO.getName() + ", User: " + token.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      log.info("Validating token of " + token.getUsername() + " with value " + token.getAccessToken());

      authenticationManager.logout(
              Client.from(clientDTO.getName(), ""),
              Username.from(token.getUsername()),
              TokenDTO.fromApiToDomain(token)
      );
      return ResponseEntity.ok().build();
    } catch (CredentialsException e) {
      return logAndSendBadRequest(e);
    }
  }

  public ResponseEntity refreshToken(ClientDTO clientDTO, TokenDTO tokenDTO) {
    log.info("[AuthenticationService::refreshToken] Client: " + clientDTO.getName() + ", User: " + tokenDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

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
      return logAndSendBadRequest(e);
    }
  }

  private ResponseEntity logAndSendBadRequest(Exception e) {
    log.warn("Error: " + e.getMessage());
    return ResponseEntity
            .badRequest()
            .body(e.getMessage());
  }
}
