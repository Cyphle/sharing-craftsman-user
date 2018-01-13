package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.domain.authorization.AuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthorizationAdminService {
  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  private AuthorizationManager authorizationManager;
  private AuthorizationVerifierService authorizationVerifierService;

  @Autowired
  public AuthorizationAdminService(
          AuthorizationRepository authorizationRepository,
          AuthorizationVerifierService authorizationVerifierService) {
    authorizationManager = new AuthorizationManagerImpl(authorizationRepository);
    this.authorizationVerifierService = authorizationVerifierService;
  }

  ResponseEntity getGroups(ClientDTO clientDTO, TokenDTO tokenDTO) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    Set<GroupDTO> groups = GroupDTO.groupFromDomainToApi(authorizationManager.getAllRolesWithTheirGroups());
    return ResponseEntity.ok(groups);
  }

  ResponseEntity createNewGroupWithRoles(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    authorizationManager.createNewGroupWithRoles(GroupDTO.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }

  ResponseEntity removeRoleFromGroup(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    authorizationManager.removeRoleFromGroup(GroupDTO.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }
}
