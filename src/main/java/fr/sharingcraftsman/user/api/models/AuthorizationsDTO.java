package fr.sharingcraftsman.user.api.models;

import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.Group;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class AuthorizationsDTO {
  private Set<GroupDTO> groups;

  public AuthorizationsDTO() {
    this.groups = new HashSet<>();
  }

  public Set<GroupDTO> getGroups() {
    return groups;
  }

  public void setGroups(Set<GroupDTO> groups) {
    this.groups = groups;
  }

  public void addGroup(GroupDTO groupDTO) {
    groups.add(groupDTO);
  }

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
