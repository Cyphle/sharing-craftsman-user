package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.Credentials;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class LoginPivot {
  public static Credentials fromApiToDomainWithEncryption(Login login) throws CredentialsException {
    return Credentials.buildEncryptedCredentials(usernameBuilder.from(login.getUsername()), passwordBuilder.from(login.getPassword()), login.stayLogged());
  }

  public static Credentials fromApiToDomain(Login login) throws CredentialsException {
    Credentials credentials = Credentials.buildCredentials(
            usernameBuilder.from(login.getUsername()),
            passwordBuilder.from(login.getPassword()),
            login.stayLogged()
    );
    return credentials;
  }
}
