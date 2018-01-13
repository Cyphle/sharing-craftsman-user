package fr.sharingcraftsman.user.api.admin;

public class UserGroupDTO {
  private String username;
  private String group;

  private UserGroupDTO() {
  }

  private UserGroupDTO(String username, String group) {
    this.username = username;
    this.group = group;
  }

  public String getUsername() {
    return username;
  }

  public String getGroup() {
    return group;
  }

  public static UserGroupDTO from(String username, String group) {
    return new UserGroupDTO(username, group);
  }
}
