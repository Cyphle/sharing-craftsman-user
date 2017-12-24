package fr.sharingcraftsman.user.domain.common;

import fr.sharingcraftsman.user.domain.authentication.CredentialsException;

public class UsernameException extends CredentialsException {
  public UsernameException(String message) {
    super(message);
  }
}
