package fr.sharingcraftsman.user.api.authorization;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AuthenticationManagerImpl;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.UserAuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final AuthenticationManager authenticationManager;
  private UserAuthorizationManager userAuthorizationManager;
  private AuthorizationVerifierService authorizationVerifierService;

  @Autowired
  public AuthorizationService(
          UserRepository userRepository,
          AccessTokenRepository accessTokenRepository,
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository,
          DateService dateService,
          AuthorizationVerifierService authorizationVerifierService) {
    authenticationManager = new AuthenticationManagerImpl(userRepository, accessTokenRepository, dateService);
    userAuthorizationManager = new UserAuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
    this.authorizationVerifierService = authorizationVerifierService;
  }

  ResponseEntity getAuthorizations(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      log.info("Request to get authotizations of:" + tokenDTO.getUsername());

      if (verifyToken(clientDTO, tokenDTO))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      Authorization authorization = userAuthorizationManager.getAuthorizationsOf(Username.from(tokenDTO.getUsername()));
      return ResponseEntity.ok(AuthorizationsDTO.fromDomainToApi(authorization));
    } catch (UsernameException e) {
      log.warn("Error: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  private boolean verifyToken(ClientDTO clientDTO, TokenDTO tokenDTO) throws UsernameException {
    Client client = Client.from(clientDTO.getName(), "");
    return !authenticationManager.isTokenValid(client, Username.from(tokenDTO.getUsername()), TokenDTO.fromApiToDomain(tokenDTO));
  }
}
