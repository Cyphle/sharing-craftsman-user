package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;

public class ProfileBuilder {
  private Username username;
  private Name firstname;
  private Name lastname;
  private Email email;
  private Link website;
  private Link github;
  private Link linkedin;

  public ProfileBuilder withUsername(Username username) {
    this.username = username;
    return this;
  }

  public ProfileBuilder withFirstname(Name firstname) {
    this.firstname = firstname;
    return this;
  }

  public ProfileBuilder withLastname(Name lastname) {
    this.lastname = lastname;
    return this;
  }

  public ProfileBuilder withEmail(Email email) {
    this.email = email;
    return this;
  }

  public ProfileBuilder withWebsite(Link website) {
    this.website = website;
    return this;
  }

  public ProfileBuilder withGithub(Link github) {
    this.github = github;
    return this;
  }

  public ProfileBuilder withLinkedin(Link linkedin) {
    this.linkedin = linkedin;
    return this;
  }

  public Profile build() {
    return new Profile(username, firstname, lastname, email, website, github, linkedin);
  }
}