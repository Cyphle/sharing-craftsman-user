package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.domain.authorization.AuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationAdminService {
  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  private AuthorizationManager authorizationManager;
  private AuthorizationVerifierService authorizationVerifierService;

  @Autowired
  public UserAuthorizationAdminService(
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository,
          AuthorizationVerifierService authorizationVerifierService) {
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
    this.authorizationVerifierService = authorizationVerifierService;
  }

  ResponseEntity addGroupToUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserGroupDTO userGroupDTO) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      authorizationManager.addGroup(Username.from(userGroupDTO.getUsername()), Groups.valueOf(userGroupDTO.getGroup()));
      return ResponseEntity.ok().build();
    } catch (UsernameException e) {
      log.warn("Error: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  ResponseEntity removeGroupToUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserGroupDTO userGroupDTO) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      authorizationManager.removeGroup(Username.from(userGroupDTO.getUsername()), Groups.valueOf(userGroupDTO.getGroup()));
      return ResponseEntity.ok().build();
    } catch (UsernameException e) {
      log.warn("Error: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }
}
