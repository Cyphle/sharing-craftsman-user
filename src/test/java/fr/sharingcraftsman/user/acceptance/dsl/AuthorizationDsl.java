package fr.sharingcraftsman.user.acceptance.dsl;

import java.util.List;

public class AuthorizationDsl {
  private List<GroupDsl> groups;

  public List<GroupDsl> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupDsl> groups) {
    this.groups = groups;
  }
}
