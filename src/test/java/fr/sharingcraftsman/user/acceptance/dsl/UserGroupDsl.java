package fr.sharingcraftsman.user.acceptance.dsl;

public class UserGroupDsl {
  private String username;
  private String group;

  public UserGroupDsl() {
  }

  public UserGroupDsl(String username, String group) {
    this.username = username;
    this.group = group;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }
}
