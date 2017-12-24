package fr.sharingcraftsman.user.domain.common;

import fr.sharingcraftsman.user.domain.authentication.CredentialException;

public class PasswordException extends CredentialException {
  public PasswordException(String message) {
    super(message);
  }
}
