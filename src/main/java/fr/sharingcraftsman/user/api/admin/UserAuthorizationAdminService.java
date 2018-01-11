package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.domain.authorization.AuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationAdminService extends AbstractAdminService {
  @Autowired
  public UserAuthorizationAdminService(
          ClientRepository clientRepository,
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository) {
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
  }

  ResponseEntity addGroupToUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserGroupDTO userGroupDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

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
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

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
