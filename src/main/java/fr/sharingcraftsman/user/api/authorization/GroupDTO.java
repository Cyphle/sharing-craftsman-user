package fr.sharingcraftsman.user.api.authorization;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class GroupDTO {
  private Set<RoleDTO> roles;
  private String name;

  private GroupDTO() {
    this.roles = new HashSet<>();
  }

  private GroupDTO(String name) {
    this.name = name;
    this.roles = new HashSet<>();
  }

  private GroupDTO(String name, Set<RoleDTO> roles) {
    this.name = name;
    this.roles = roles;
  }

  public String getName() {
    return name;
  }

  public Set<RoleDTO> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleDTO> roles) {
    this.roles = roles;
  }

  public void addRoles(List<RoleDTO> roles) {
    this.roles.addAll(roles);
  }

  public void addRole(RoleDTO role) {
    roles.add(role);
  }

  public static GroupDTO from(String name) {
    return new GroupDTO(name);
  }

  public static GroupDTO from(String name, Set<RoleDTO> roles) {
    return new GroupDTO(name, roles);
  }

  public static Set<GroupDTO> groupFromDomainToApi(Set<Group> groups) {
    return groups.stream()
            .map(group -> GroupDTO.from(group.getName(), RoleDTO.roleFromDomainToApi(group.getRoles())))
            .collect(Collectors.toSet());
  }

  public static Group fromApiToDomain(GroupDTO groupDTO) {
    Group group = Group.from(groupDTO.getName());
    group.addRoles(
            groupDTO
                    .getRoles()
                    .stream()
                    .map(role -> Role.from(role.getName()))
                    .collect(Collectors.toList())
    );
    return group;
  }
}
