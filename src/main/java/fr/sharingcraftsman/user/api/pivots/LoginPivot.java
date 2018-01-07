package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.Credentials;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class LoginPivot {
  public static Credentials fromApiToDomainWithEncryption(LoginDTO loginDTO) throws CredentialsException {
    return Credentials.buildWithEncryptionAndPersistentLogging(loginDTO.getUsername(), loginDTO.getPassword(), loginDTO.isPersistentLogging());
  }

  public static Credentials fromApiToDomain(LoginDTO loginDTO) throws CredentialsException {
    Credentials credentials = Credentials.buildWithPersistentLogging(
            loginDTO.getUsername(),
            loginDTO.getPassword(),
            loginDTO.isPersistentLogging()
    );
    return credentials;
  }
}
