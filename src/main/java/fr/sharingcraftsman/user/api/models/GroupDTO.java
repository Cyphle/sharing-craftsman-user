package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class GroupDTO {
  private Set<RoleDTO> roles;
  private String name;

  public GroupDTO() {
    this.roles = new HashSet<>();
  }

  public GroupDTO(String name) {
    this.name = name;
    this.roles = new HashSet<>();
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
}
