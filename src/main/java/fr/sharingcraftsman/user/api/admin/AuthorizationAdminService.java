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
    ResponseEntity isUnauthorized = isUnauthorized(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    Set<GroupDTO> groups = GroupDTO.groupFromDomainToApi(authorizationManager.getAllRolesWithTheirGroups());
    return ResponseEntity.ok(groups);
  }

  ResponseEntity createNewGroupWithRoles(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    ResponseEntity isUnauthorized = isUnauthorized(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    authorizationManager.createNewGroupWithRoles(GroupDTO.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }

  ResponseEntity removeRoleFromGroup(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    ResponseEntity isUnauthorized = isUnauthorized(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    authorizationManager.removeRoleFromGroup(GroupDTO.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }
}
