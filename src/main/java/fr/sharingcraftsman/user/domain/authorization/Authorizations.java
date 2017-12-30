package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Authorizations {
  private List<Group> groups;

  public Authorizations() {
    this.groups = new ArrayList<>();
  }

  public Authorizations(List<Group> groups) {
    this.groups = groups;
  }

  public void addGroup(Group group) {
    this.groups.add(group);
  }
}
