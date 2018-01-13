package fr.sharingcraftsman.user.domain.common;

import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;

public class PasswordException extends CredentialsException {
  public PasswordException(String message) {
    super(message);
  }
}
