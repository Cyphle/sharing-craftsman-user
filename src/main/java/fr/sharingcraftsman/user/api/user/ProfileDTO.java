package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.AbstractProfile;
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
  private String picture;

  private ProfileDTO() {
  }

  private ProfileDTO(String firstname, String lastname, String email, String website, String github, String linkedin, String picture) {
    this.lastname = lastname;
    this.firstname = firstname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
    this.picture = picture;
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

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public static ProfileDTO from(String firstname, String lastname, String email, String website, String github, String linkedin, String picture) {
    return new ProfileDTO(firstname, lastname, email, website, github, linkedin, picture);
  }

  public static AbstractProfile fromApiToDomain(String username, ProfileDTO profileDTO) throws UsernameException {
    return Profile.from(
            Username.from(username),
            Name.of(profileDTO.firstname),
            Name.of(profileDTO.lastname),
            Email.from(profileDTO.email),
            Link.to(profileDTO.website),
            Link.to(profileDTO.github),
            Link.to(profileDTO.linkedin),
            Name.of(profileDTO.picture)
    );
  }

  public static ProfileDTO fromDomainToApi(Profile profile) {
    return ProfileDTO.from(
            profile.getFirstnameContent(),
            profile.getLastnameContent(),
            profile.getEmailContent(),
            profile.getWebsiteContent(),
            profile.getGithubContent(),
            profile.getLinkedinContent(),
            profile.getPictureContent()
    );
  }
}
