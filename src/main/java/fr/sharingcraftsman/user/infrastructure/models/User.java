package fr.sharingcraftsman.user.infrastructure.models;

import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@ToString
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
  @Column(name = "email")
  private String email;
  @Column(name = "website")
  private String website;
  @Column(name = "github")
  private String github;
  @Column(name = "linkedin")
  private String linkedin;
  @Column(name = "is_active")
  private boolean isActive = true;
  @Column(name = "creation_date")
  private Date creationDate;
  @Column(name = "last_update_date")
  private Date lastUpdateDate;
  @Column(name = "change_password_key")
  private String changePasswordKey;
  @Column(name = "change_password_expiration_date")
  private Date changePasswordExpirationDate;

  public User() { }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(String username, String firstname, String lastname, String email, String website, String github, String linkedin) {
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
  }

  public User(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin) {
    this(username, firstname, lastname, email, website, github, linkedin);
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getGithub() {
    return github;
  }

  public void setGithub(String github) {
    this.github = github;
  }

  public String getLinkedin() {
    return linkedin;
  }

  public void setLinkedin(String linkedin) {
    this.linkedin = linkedin;
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

  public String getChangePasswordKey() {
    return changePasswordKey;
  }

  public void setChangePasswordKey(String changePasswordKey) {
    this.changePasswordKey = changePasswordKey;
  }

  public Date getChangePasswordExpirationDate() {
    return changePasswordExpirationDate;
  }

  public void setChangePasswordExpirationDate(Date changePasswordExpirationDate) {
    this.changePasswordExpirationDate = changePasswordExpirationDate;
  }

  public void updateFromProfile(User user) {
    firstname = user.firstname;
    lastname = user.lastname;
    email = user.email;
    website = user.website;
    github = user.github;
    linkedin = user.linkedin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (username != null ? !username.equals(user.username) : user.username != null) return false;
    if (lastname != null ? !lastname.equals(user.lastname) : user.lastname != null) return false;
    if (firstname != null ? !firstname.equals(user.firstname) : user.firstname != null) return false;
    return password != null ? password.equals(user.password) : user.password == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }
}
