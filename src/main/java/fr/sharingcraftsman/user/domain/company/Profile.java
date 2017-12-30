package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.common.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class Profile {
  private Username username;
  private Name firstname;
  private Name lastname;
  private Email email;
  private Link website;
  private Link github;
  private Link linkedin;

  public Profile(Username username, Name firstname, Name lastname, Email email, Link website, Link github, Link linkedin) {
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
}
