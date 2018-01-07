package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.pivots.AuthorizationPivot;
import fr.sharingcraftsman.user.api.pivots.ClientPivot;
import fr.sharingcraftsman.user.api.pivots.TokenPivot;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.OAuthAuthenticator;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.authorization.Authorizations;
import fr.sharingcraftsman.user.domain.authorization.GroupAdministrator;
import fr.sharingcraftsman.user.domain.authorization.GroupRoleAuthorizer;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientAdministrator;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.user.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.ports.authorization.Authorizer;
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
public class RoleService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final Authenticator authenticator;
  private ClientManager clientManager;
  private Authorizer authorizer;

  @Autowired
  public RoleService(
          HumanResourceAdministrator humanResourceAdministrator,
          ClientStock clientStock,
          TokenAdministrator tokenAdministrator,
          GroupAdministrator groupAdministrator,
          RoleAdministrator roleAdministrator,
          DateService dateService) {
    clientManager = new ClientAdministrator(clientStock, new SimpleSecretGenerator());
    authenticator = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator, dateService);
    authorizer = new GroupRoleAuthorizer(groupAdministrator, roleAdministrator);
  }

  public ResponseEntity getAuthorizations(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientManager.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to see authorizations with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Request to get authotizations of:" + tokenDTO.getUsername());
      Credentials credentials = Credentials.buildCredentials(usernameBuilder.from(tokenDTO.getUsername()), null, false);

      if (verifyToken(clientDTO, tokenDTO, credentials))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      Authorizations authorizations = authorizer.getAuthorizationsOf(credentials);
      return ResponseEntity.ok(AuthorizationPivot.fromDomainToApi(authorizations));
    } catch (CredentialsException e) {
      log.warn("Error with getting authorizations " + tokenDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  private boolean verifyToken(ClientDTO clientDTO, TokenDTO tokenDTO, Credentials credentials) {
    Client client = new Client(clientDTO.getName(), "", false);
    return !authenticator.isTokenValid(credentials, client, TokenPivot.fromApiToDomain(tokenDTO));
  }




  /*
  #  - Should have for user
#    -> GROUP_USERS
#        -> Containing ROLE_USER
#  - Should have for admin
#    -> GROUP_ADMINS
#        -> Containing ROLE_ADMIN, ROLE_USER
   */
}
