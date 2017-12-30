package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class GroupDTO {
  private List<RoleDTO> roles;
  private String name;

  public GroupDTO() {
    this.roles = new ArrayList<>();
  }

  public GroupDTO(String name) {
    this.name = name;
    this.roles = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public List<RoleDTO> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleDTO> roles) {
    this.roles = roles;
  }

  public void addRoles(List<RoleDTO> roles) {
    this.roles.addAll(roles);
  }

  public void addRole(RoleDTO role) {
    roles.add(role);
  }
}
