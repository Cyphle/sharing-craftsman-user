package fr.sharingcraftsman.user.command.pivots;

import fr.sharingcraftsman.user.command.models.Login;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.Credentials;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class LoginPivot {
  public static Credentials fromApiToDomain(Login login) throws CredentialsException {
    return Credentials.buildEncryptedCredentials(usernameBuilder.from(login.getUsername()), passwordBuilder.from(login.getPassword()));
  }
}
