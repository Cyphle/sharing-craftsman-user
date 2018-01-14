package fr.sharingcraftsman.user.acceptance.dsl;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ProfileDsl {
  private String firstname;
  private String lastname;
  private String email;
  private String website;
  private String github;
  private String linkedin;
  private String picture;

  public ProfileDsl() {
  }

  public ProfileDsl(String firstname, String lastname, String email, String website, String github, String linkedin, String picture) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
    this.picture = picture;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  public String getWebsite() {
    return website;
  }

  public String getGithub() {
    return github;
  }

  public String getLinkedin() {
    return linkedin;
  }

  public String getPicture() {
    return picture;
  }
}
