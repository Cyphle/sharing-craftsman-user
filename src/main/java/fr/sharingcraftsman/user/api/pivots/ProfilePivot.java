package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ProfileDTO;
import fr.sharingcraftsman.user.domain.company.Profile;

public class ProfilePivot {
  public static Profile fromApiToDomain(String username, ProfileDTO profileDTO) {
    throw new UnsupportedOperationException();
  }

  public static ProfileDTO fromDomainToApi(Profile profile) {
    throw new UnsupportedOperationException();
  }
}
