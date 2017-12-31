package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
