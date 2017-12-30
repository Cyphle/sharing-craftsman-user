package fr.sharingcraftsman.user.api.models;

import java.util.List;

public class GroupDTO {
  private List<RoleDTO> roles;

  public List<RoleDTO> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleDTO> roles) {
    this.roles = roles;
  }
}
