package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class Authorization {
  private List<Group> groups;

  private Authorization(List<Group> groups) {
    this.groups = groups;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public static Authorization from(List<Group> groups) {
    return new Authorization(groups);
  }
}
