package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authorization.AuthorizationsDTO;
import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import lombok.ToString;

@ToString
public class UserInfoDTO {
  private String username;
  private String password;
  private String firstname;
  private String lastname;
  private String email;
  private String website;
  private String github;
  private String linkedin;
  private AuthorizationsDTO authorizations;
  private boolean isActive;
  private long creationDate;
  private long lastUpdateDate;

  public UserInfoDTO() {
  }

  private UserInfoDTO(
          String username,
          String firstname,
          String lastname,
          String email,
          String website,
          String github,
          String linkedin,
          AuthorizationsDTO authorizations,
          boolean isActive,
          long creationDate,
          long lastUpdateDate) {
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
    this.authorizations = authorizations;
    this.isActive = isActive;
    this.creationDate = creationDate;
    this.lastUpdateDate = lastUpdateDate;
  }

  private UserInfoDTO(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin, boolean isActive, long creationDate, long lastUpdateDate) {
    this.username = username;
    this.password = password;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
    this.isActive = isActive;
    this.creationDate = creationDate;
    this.lastUpdateDate = lastUpdateDate;
  }

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

  public AuthorizationsDTO getAuthorizations() {
    return authorizations;
  }

  public void setAuthorizations(AuthorizationsDTO authorizations) {
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

  public static UserInfo fromApiToDomain(UserInfoDTO user) throws UsernameException, PasswordException {
    return UserInfo.from(
            User.from(user.getUsername(), user.getPassword() != null && !user.getPassword().isEmpty() ? user.getPassword() : "NOPASSWORD"),
            Profile.from(Username.from(user.getUsername()), Name.of(user.getFirstname()), Name.of(user.getLastname()), Email.from(user.getEmail()), Link.to(user.getWebsite()), Link.to(user.getGithub()), Link.to(user.getLinkedin())),
            TechnicalUserDetails.from(Username.from(user.getUsername()), user.isActive, DateConverter.fromLongToLocalDateTime(user.getCreationDate()), DateConverter.fromLongToLocalDateTime(user.getLastUpdateDate()))
    );
  }

  public static UserInfoDTO from(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin, boolean isActive, long creationDate, long lastUpdateDate) {
    return new UserInfoDTO(username, password, firstname, lastname, email, website, github, linkedin, isActive, creationDate, lastUpdateDate);
  }

  public static UserInfoDTO from(String username, String firstname, String lastname, String email, String website, String github, String linkedin, AuthorizationsDTO authorizations, boolean isActive, long creationDate, long lastUpdateDate) {
    return new UserInfoDTO(username, firstname, lastname, email, website, github, linkedin, authorizations, isActive, creationDate, lastUpdateDate);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserInfoDTO that = (UserInfoDTO) o;

    if (isActive != that.isActive) return false;
    if (username != null ? !username.equals(that.username) : that.username != null) return false;
    if (password != null ? !password.equals(that.password) : that.password != null) return false;
    if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
    if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
    if (email != null ? !email.equals(that.email) : that.email != null) return false;
    if (website != null ? !website.equals(that.website) : that.website != null) return false;
    if (github != null ? !github.equals(that.github) : that.github != null) return false;
    if (linkedin != null ? !linkedin.equals(that.linkedin) : that.linkedin != null) return false;
    return authorizations != null ? authorizations.equals(that.authorizations) : that.authorizations == null;
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
