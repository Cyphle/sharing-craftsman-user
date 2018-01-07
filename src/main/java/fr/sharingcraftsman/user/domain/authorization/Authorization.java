package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Authorization {
  private List<Group> groups;

  private Authorization() {
    this.groups = new ArrayList<>();
  }

  private Authorization(List<Group> groups) {
    this.groups = groups;
  }

  public void addGroup(Group group) {
    this.groups.add(group);
  }

  public List<Group> getGroups() {
    return groups;
  }

  public static Authorization get() {
    return new Authorization();
  }

  public static Authorization get(List<Group> groups) {
    return new Authorization(groups);
  }
}
