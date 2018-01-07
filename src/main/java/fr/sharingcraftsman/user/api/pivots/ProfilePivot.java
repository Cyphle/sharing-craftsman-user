package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ProfileDTO;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.BaseProfile;
import fr.sharingcraftsman.user.domain.user.Profile;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class ProfilePivot {
  public static BaseProfile fromApiToDomain(String username, ProfileDTO profileDTO) throws UsernameException {
    return Profile.from(
            usernameBuilder.from(username),
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
