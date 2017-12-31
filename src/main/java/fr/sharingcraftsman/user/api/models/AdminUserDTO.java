package fr.sharingcraftsman.user.api.models;

public class AdminUserDTO {
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String website;
  private String githube;
  private String linkedin;
  private AuthorizationsDTO authorizations;
  private boolean isActive;
  private long creationDate;
  private long lastUpdateDate;

  public AdminUserDTO() {
  }

  public AdminUserDTO(String username, String firstname, String lastname, String email, String website, String githube, String linkedin, AuthorizationsDTO authorizations, boolean isActive, long creationDate, long lastUpdateDate) {
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.website = website;
    this.githube = githube;
    this.linkedin = linkedin;
    this.authorizations = authorizations;
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

  public String getGithube() {
    return githube;
  }

  public void setGithube(String githube) {
    this.githube = githube;
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
}
