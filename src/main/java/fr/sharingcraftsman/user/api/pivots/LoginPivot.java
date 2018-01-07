package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;

public class LoginPivot {
  public static Credentials fromApiToDomain(LoginDTO loginDTO) throws CredentialsException {
    return Credentials.buildWithPersistentLogging(
            loginDTO.getUsername(),
            loginDTO.getPassword(),
            loginDTO.isPersistentLogging()
    );
  }
}
