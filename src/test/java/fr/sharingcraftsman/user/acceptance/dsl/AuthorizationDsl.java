package fr.sharingcraftsman.user.acceptance.dsl;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class AuthorizationDsl {
  private List<GroupDsl> groups;

  public List<GroupDsl> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupDsl> groups) {
    this.groups = groups;
  }
}
