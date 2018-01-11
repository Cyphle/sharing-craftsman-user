package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.domain.authorization.AuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthorizationAdminService extends AbstractAdminService {
  @Autowired
  public AuthorizationAdminService(
          ClientRepository clientRepository,
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository) {
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
  }

  ResponseEntity getGroups(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    Set<GroupDTO> groups = GroupDTO.groupFromDomainToApi(authorizationManager.getAllRolesWithTheirGroups());
    return ResponseEntity.ok(groups);
  }

  ResponseEntity createNewGroupWithRoles(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    authorizationManager.createNewGroupWithRoles(GroupDTO.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }

  ResponseEntity removeRoleFromGroup(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    authorizationManager.removeRoleFromGroup(GroupDTO.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }
}
