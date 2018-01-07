package fr.sharingcraftsman.user.api.admin;

public class UserGroupDTO {
  private String username;
  private String group;

  public UserGroupDTO() {
  }

  public UserGroupDTO(String username, String group) {
    this.username = username;
    this.group = group;
  }

  public String getUsername() {
    return username;
  }

  public String getGroup() {
    return group;
  }
}
