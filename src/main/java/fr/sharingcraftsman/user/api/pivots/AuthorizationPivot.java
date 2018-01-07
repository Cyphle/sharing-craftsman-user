package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.models.GroupDTO;
import fr.sharingcraftsman.user.api.models.RoleDTO;
import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.Group;

import java.util.stream.Collectors;

public class AuthorizationPivot {
  public static AuthorizationsDTO fromDomainToApi(Authorization authorization) {
    AuthorizationsDTO authorizationsDTO = new AuthorizationsDTO();
    for (Group group : authorization.getGroups()) {
      GroupDTO groupDTO = new GroupDTO(group.getName());
      groupDTO.addRoles(
              group.getRoles()
                      .stream()
                      .map(role -> new RoleDTO(role.getName()))
                      .collect(Collectors.toList())
      );
      authorizationsDTO.addGroup(groupDTO);
    }
    return authorizationsDTO;
  }
}
