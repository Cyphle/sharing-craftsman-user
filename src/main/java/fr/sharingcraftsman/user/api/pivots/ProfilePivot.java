package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ProfileDTO;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.BaseProfile;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.ProfileBuilder;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class ProfilePivot {
  public static BaseProfile fromApiToDomain(String username, ProfileDTO profileDTO) throws UsernameException {
    return new ProfileBuilder()
            .withUsername(usernameBuilder.from(username))
            .withFirstname(Name.of(profileDTO.getFirstname()))
            .withLastname(Name.of(profileDTO.getLastname()))
            .withEmail(Email.from(profileDTO.getEmail()))
            .withWebsite(Link.to(profileDTO.getWebsite()))
            .withGithub(Link.to(profileDTO.getGithub()))
            .withLinkedin(Link.to(profileDTO.getLinkedin()))
            .build();
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
