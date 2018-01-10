package fr.sharingcraftsman.user.acceptance.dsl;

import lombok.ToString;

@ToString
public class UserDsl {
  private String username;
  private String password;
  private String firstname;
  private String lastname;
  private String email;
  private String website;
  private String github;
  private String linkedin;
  private AuthorizationDsl authorizations;
  private boolean isActive;
  private long creationDate;
  private long lastUpdateDate;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
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

  public AuthorizationDsl getAuthorizations() {
    return authorizations;
  }

  public void setAuthorizations(AuthorizationDsl authorizations) {
    this.authorizations = authorizations;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public long getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  public long getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(long lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserDsl userDsl = (UserDsl) o;

    if (isActive != userDsl.isActive) return false;
    if (username != null ? !username.equals(userDsl.username) : userDsl.username != null) return false;
    if (password != null ? !password.equals(userDsl.password) : userDsl.password != null) return false;
    if (firstname != null ? !firstname.equals(userDsl.firstname) : userDsl.firstname != null) return false;
    if (lastname != null ? !lastname.equals(userDsl.lastname) : userDsl.lastname != null) return false;
    if (email != null ? !email.equals(userDsl.email) : userDsl.email != null) return false;
    if (website != null ? !website.equals(userDsl.website) : userDsl.website != null) return false;
    if (github != null ? !github.equals(userDsl.github) : userDsl.github != null) return false;
    if (linkedin != null ? !linkedin.equals(userDsl.linkedin) : userDsl.linkedin != null) return false;
    return authorizations != null ? authorizations.equals(userDsl.authorizations) : userDsl.authorizations == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (website != null ? website.hashCode() : 0);
    result = 31 * result + (github != null ? github.hashCode() : 0);
    result = 31 * result + (linkedin != null ? linkedin.hashCode() : 0);
    result = 31 * result + (authorizations != null ? authorizations.hashCode() : 0);
    result = 31 * result + (isActive ? 1 : 0);
    return result;
  }
}
