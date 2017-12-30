package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.models.GroupDTO;
import fr.sharingcraftsman.user.api.models.RoleDTO;
import fr.sharingcraftsman.user.domain.authorization.Authorizations;
import fr.sharingcraftsman.user.domain.authorization.Group;

import java.util.stream.Collectors;

public class AuthorizationPivot {
  public static AuthorizationsDTO fromDomainToApi(Authorizations authorizations) {
    AuthorizationsDTO authorizationsDTO = new AuthorizationsDTO();
    for (Group group : authorizations.getGroups()) {
      GroupDTO groupDTO = new GroupDTO(group.getName());
      groupDTO.addRoles(
              group.getRoles()
                      .stream()
                      .map(role -> new RoleDTO(role.getRole()))
                      .collect(Collectors.toList())
      );
      authorizationsDTO.addGroup(groupDTO);
    }
    return authorizationsDTO;
  }
}
