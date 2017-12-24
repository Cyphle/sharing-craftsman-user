package fr.sharingcraftsman.user.domain.common;

import fr.sharingcraftsman.user.domain.authentication.CredentialsException;

public class PasswordException extends CredentialsException {
  public PasswordException(String message) {
    super(message);
  }
}
