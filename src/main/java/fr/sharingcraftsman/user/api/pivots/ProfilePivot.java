package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ProfileDTO;
import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.BaseProfile;
import fr.sharingcraftsman.user.domain.user.Profile;

public class ProfilePivot {
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
