package fr.sharingcraftsman.user.infrastructure.models;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "group_roles")
@ToString
public class GroupRole {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "group")
  private String group;
  @Column(name = "role")
  private String role;

  public GroupRole() {
  }

  public GroupRole(String group, String role) {
    this.group = group;
    this.role = role;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
