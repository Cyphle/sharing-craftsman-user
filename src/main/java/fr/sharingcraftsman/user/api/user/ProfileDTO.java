package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.BaseProfile;
import fr.sharingcraftsman.user.domain.user.Profile;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ProfileDTO {
  private String firstname;
  private String lastname;
  private String email;
  private String website;
  private String github;
  private String linkedin;

  public ProfileDTO() {
  }

  public ProfileDTO(String firstname, String lastname, String email, String website, String github, String linkedin) {
    this.lastname = lastname;
    this.firstname = firstname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
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

  public static BaseProfile fromApiToDomain(String username, ProfileDTO profileDTO) throws UsernameException {
    return Profile.from(
            Username.from(username),
            Name.of(profileDTO.getFirstname()),
            Name.of(profileDTO.getLastname()),
            Email.from(profileDTO.getEmail()),
            Link.to(profileDTO.getWebsite()),
            Link.to(profileDTO.getGithub()),
            Link.to(profileDTO.getLinkedin()));
  }

  public static ProfileDTO fromDomainToApi(Profile profile) {
    return new ProfileDTO(
            profile.getFirstnameContent(),
            profile.getLastnameContent(),
            profile.getEmailContent(),
            profile.getWebsiteContent(),
            profile.getGithubContent(),
            profile.getLinkedinContent()
    );
  }
}
