package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.common.*;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@ToString
public class UserForAdmin extends BaseUserForAdmin {
  private Username username;
  private Password password;
  private Name firstname;
  private Name lastname;
  private Email email;
  private Link website;
  private Link github;
  private Link linkedin;
  private String changePasswordKey;
  private LocalDateTime changePasswordKeyExpirationDate;
  private boolean isActive;
  private LocalDateTime creationDate;
  private LocalDateTime lastUpdateDate;

  public UserForAdmin(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin, String changePasswordKey, Date changePasswordExpirationDate, boolean active, Date creationDate, Date lastUpdateDate) {
    this.username = new Username(username);
    this.password = new Password(password);
    this.firstname = Name.of(firstname);
    this.lastname = Name.of(lastname);
    this.email = Email.from(email);
    this.website = Link.to(website);
    this.github = Link.to(github);
    this.linkedin = Link.to(linkedin);
    this.changePasswordKey = changePasswordKey;
    this.changePasswordKeyExpirationDate = changePasswordExpirationDate != null ? fromDateToLocalDatetime(changePasswordExpirationDate) : null;
    this.isActive = active;
    this.creationDate = fromDateToLocalDatetime(creationDate);
    this.lastUpdateDate = fromDateToLocalDatetime(lastUpdateDate);
  }

  public UserForAdmin(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin, String changePasswordKey, LocalDateTime changePasswordKeyExpirationDate, boolean isActive, LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
    this.username = new Username(username);
    this.password = new Password(password);
    this.firstname = Name.of(firstname);
    this.lastname = Name.of(lastname);
    this.email = Email.from(email);
    this.website = Link.to(website);
    this.github = Link.to(github);
    this.linkedin = Link.to(linkedin);
    this.changePasswordKey = changePasswordKey;
    this.changePasswordKey = changePasswordKey;
    this.changePasswordKeyExpirationDate = changePasswordKeyExpirationDate;
    this.isActive = isActive;
    this.creationDate = creationDate;
    this.lastUpdateDate = lastUpdateDate;
  }

  public String getUsernameContent() {
    return username.getUsername();
  }

  public Username getUsername() { return username; }

  public String getPassword() {
    return password.getPassword();
  }

  public String getFirstname() {
    return firstname.getName();
  }

  public String getLastname() {
    return lastname.getName();
  }

  public String getEmail() {
    return email.getEmail();
  }

  public String getWebsite() {
    return website.getLink();
  }

  public String getGithub() {
    return github.getLink();
  }

  public String getLinkedin() {
    return linkedin.getLink();
  }

  public String getChangePasswordKey() {
    return changePasswordKey;
  }

  public long getChangePasswordKeyExpirationDate() {
    return changePasswordKeyExpirationDate != null ? fromLocalDatetimeToLong(changePasswordKeyExpirationDate) : 0;
  }

  public boolean isActive() {
    return isActive;
  }

  public long getCreationDate() {
    return fromLocalDatetimeToLong(creationDate);
  }

  public long getLastUpdateDate() {
    return fromLocalDatetimeToLong(lastUpdateDate);
  }

  public void updateFields(UserForAdmin collaborator) {
    username = collaborator.username;
    firstname = collaborator.firstname;
    lastname = collaborator.lastname;
    email = collaborator.email;
    website = collaborator.website;
    github = collaborator.github;
    linkedin = collaborator.linkedin;
    isActive = collaborator.isActive;
  }

  @Override
  public boolean isKnown() {
    return true;
  }

  public static UserForAdmin from(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin, String changePasswordKey, Date changePasswordExpirationDate, boolean active, Date creationDate, Date lastUpdateDate) {
    return new UserForAdmin(username, password, firstname, lastname, email, website, github, linkedin, changePasswordKey, changePasswordExpirationDate, active, creationDate, lastUpdateDate);
  }

  public static UserForAdmin from(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin, String changePasswordKey, LocalDateTime changePasswordExpirationDate, boolean active, LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
    return new UserForAdmin(username, password, firstname, lastname, email, website, github, linkedin, changePasswordKey, changePasswordExpirationDate, active, creationDate, lastUpdateDate);
  }

  private long fromLocalDatetimeToLong(LocalDateTime localDateTime) {
    ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
    return zdt.toInstant().toEpochMilli();
  }

  private LocalDateTime fromDateToLocalDatetime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserForAdmin that = (UserForAdmin) o;

    if (isActive != that.isActive) return false;
    if (username != null ? !username.equals(that.username) : that.username != null) return false;
    if (password != null ? !password.equals(that.password) : that.password != null) return false;
    if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
    if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
    if (email != null ? !email.equals(that.email) : that.email != null) return false;
    if (website != null ? !website.equals(that.website) : that.website != null) return false;
    if (github != null ? !github.equals(that.github) : that.github != null) return false;
    if (linkedin != null ? !linkedin.equals(that.linkedin) : that.linkedin != null) return false;
    return changePasswordKey != null ? changePasswordKey.equals(that.changePasswordKey) : that.changePasswordKey == null;
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
    result = 31 * result + (changePasswordKey != null ? changePasswordKey.hashCode() : 0);
    result = 31 * result + (isActive ? 1 : 0);
    return result;
  }
}
