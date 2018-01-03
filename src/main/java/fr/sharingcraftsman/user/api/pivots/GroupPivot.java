package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.GroupDTO;
import fr.sharingcraftsman.user.api.models.RoleDTO;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupPivot {
  public static Set<GroupDTO> groupFromDomainToApi(Set<Group> groups) {
    return groups.stream()
            .map(group -> new GroupDTO(group.getName(), GroupPivot.roleFromDomainToApi(group.getRoles())))
            .collect(Collectors.toSet());
  }

  private static Set<RoleDTO> roleFromDomainToApi(Set<Role> roles) {
    return roles.stream()
            .map(role -> new RoleDTO(role.getRole()))
            .collect(Collectors.toSet());
  }
}
