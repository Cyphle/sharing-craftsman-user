package fr.sharingcraftsman.user.api.models;

import java.util.List;

public class AuthorizationDTO {
  private List<GroupDTO> groups;

  public List<GroupDTO> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupDTO> groups) {
    this.groups = groups;
  }
}
