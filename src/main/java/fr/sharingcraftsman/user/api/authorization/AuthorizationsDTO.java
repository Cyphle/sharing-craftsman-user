package fr.sharingcraftsman.user.api.authorization;

import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.Group;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class AuthorizationsDTO {
  private Set<GroupDTO> groups;

  private AuthorizationsDTO() {
    this.groups = new HashSet<>();
  }

  private AuthorizationsDTO(HashSet<GroupDTO> groups) {
    this.groups = groups;
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

  public static AuthorizationsDTO from(HashSet<GroupDTO> groups) {
    return new AuthorizationsDTO(groups);
  }

  public static AuthorizationsDTO fromDomainToApi(Authorization authorization) {
    AuthorizationsDTO authorizationsDTO = new AuthorizationsDTO();
    for (Group group : authorization.getGroups()) {
      GroupDTO groupDTO = GroupDTO.from(group.getName());
      groupDTO.addRoles(
              group.getRoles()
                      .stream()
                      .map(role -> RoleDTO.from(role.getName()))
                      .collect(Collectors.toList())
      );
      authorizationsDTO.addGroup(groupDTO);
    }
    return authorizationsDTO;
  }
}
