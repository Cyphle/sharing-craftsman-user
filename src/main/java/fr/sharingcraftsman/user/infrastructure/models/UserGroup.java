package fr.sharingcraftsman.user.infrastructure.models;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_groups")
@ToString
public class UserGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username")
  private String username;
  @Column(name = "group")
  private String group;

  public UserGroup() {
  }

  public UserGroup(String username, String group) {
    this.username = username;
    this.group = group;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
