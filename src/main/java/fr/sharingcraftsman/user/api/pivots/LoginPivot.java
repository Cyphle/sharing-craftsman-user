package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.Credentials;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class LoginPivot {
  public static Credentials fromApiToDomainWithEncryption(LoginDTO loginDTO) throws CredentialsException {
    return Credentials.buildEncryptedCredentials(usernameBuilder.from(loginDTO.getUsername()), passwordBuilder.from(loginDTO.getPassword()), loginDTO.stayLogged());
  }

  public static Credentials fromApiToDomain(LoginDTO loginDTO) throws CredentialsException {
    Credentials credentials = Credentials.buildCredentials(
            usernameBuilder.from(loginDTO.getUsername()),
            passwordBuilder.from(loginDTO.getPassword()),
            loginDTO.stayLogged()
    );
    return credentials;
  }
}
