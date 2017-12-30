package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class AuthorizationsDTO {
  private List<GroupDTO> groups;

  public AuthorizationsDTO() {
    this.groups = new ArrayList<>();
  }

  public List<GroupDTO> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupDTO> groups) {
    this.groups = groups;
  }

  public void addGroup(GroupDTO groupDTO) {
    groups.add(groupDTO);
  }
}
