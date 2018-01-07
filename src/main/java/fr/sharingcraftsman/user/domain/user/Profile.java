package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.common.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Profile extends BaseProfile {
  private Username username;
  private Name firstname;
  private Name lastname;
  private Email email;
  private Link website;
  private Link github;
  private Link linkedin;

  private Profile(Username username, Name firstname, Name lastname, Email email, Link website, Link github, Link linkedin) {
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
  }

  public Username getUsername() {
    return username;
  }

  public String getUsernameContent() {
    return username.getUsername();
  }

  public String getFirstnameContent() {
    return firstname.getName();
  }

  public String getLastnameContent() {
    return lastname.getName();
  }

  public Email getEmail() {
    return email;
  }

  public String getEmailContent() {
    return email.getEmail();
  }

  public String getWebsiteContent() {
    return website.getLink();
  }

  public String getGithubContent() {
    return github.getLink();
  }

  public String getLinkedinContent() {
    return linkedin.getLink();
  }

  public List<ValidationError> validate() {
    List<ValidationError> errors = new ArrayList<>();

    if (!email.isValid())
      errors.add(new ValidationError("Invalid email"));

    return errors;
  }

  public void updateFrom(Profile newProfile) {
    firstname = newProfile.firstname;
    lastname = newProfile.lastname;
    email = newProfile.email;
    website = newProfile.website;
    github = newProfile.github;
    linkedin = newProfile.linkedin;
  }

  @Override
  public boolean isKnown() {
    return true;
  }

  public static Profile from(Username username, Name firstname, Name lastname, Email email, Link website, Link github, Link linkedin) {
    return new Profile(username, firstname, lastname, email, website, github, linkedin);
  }
}
