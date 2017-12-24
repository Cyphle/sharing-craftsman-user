package fr.sharingcraftsman.user.command.common;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@EqualsAndHashCode
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username", unique = true)
  private String username;
  @Column(name = "lastname")
  private String lastname;
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "password")
  private String password;
  @Column(name = "is_active")
  private boolean isActive = true;
  @Column(name = "creation_date")
  private Date creationDate;
  @Column(name = "last_update_date")
  private Date lastUpdateDate;

  public User() { }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
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

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }
}
