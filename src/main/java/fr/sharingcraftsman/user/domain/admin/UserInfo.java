package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class UserInfo extends AbstractUserInfo {
  private User user;
  private Profile profile;
  private TechnicalUserDetails technicalUserDetails;

  public UserInfo(User user, Profile profile, TechnicalUserDetails technicalUserDetails) {
    this.user = user;
    this.profile = profile;
    this.technicalUserDetails = technicalUserDetails;
  }

  public Username getUsername() {
    return user.getUsername();
  }

  public String getUsernameContent() {
    return user.getUsernameContent();
  }

  public Password getPassword() {
    return user.getPassword();
  }

  public String getPasswordContent() {
    return user.getPasswordContent();
  }

  public void setPassword(Password password) {
    this.user.setPassword(password);
  }

  public String getFirstname() {
    return profile.getFirstnameContent();
  }

  public String getLastname() {
    return profile.getLastnameContent();
  }

  public String getEmail() {
    return profile.getEmailContent();
  }

  public String getWebsite() {
    return profile.getWebsiteContent();
  }

  public String getGithub() {
    return profile.getGithubContent();
  }

  public String getLinkedin() {
    return profile.getLinkedinContent();
  }

  public boolean isActive() {
    return technicalUserDetails.isActive();
  }

  public long getCreationDate() {
    return DateConverter.fromLocalDateTimeToLong(technicalUserDetails.getCreationDate());
  }

  public long getLastUpdateDate() {
    return DateConverter.fromLocalDateTimeToLong(technicalUserDetails.getLastUpdateDate());
  }

  public void updateFields(UserInfo userUpdated) {
    user.updateFields(userUpdated.user);
    profile.updateFields(userUpdated.profile);
    technicalUserDetails.updateFields(userUpdated.technicalUserDetails);
  }

  @Override
  public boolean isKnown() {
    return true;
  }

  public static UserInfo from(User user, Profile profile, TechnicalUserDetails technicalUserDetails) {
    return new UserInfo(user, profile, technicalUserDetails);
  }
}
