package fr.sharingcraftsman.user.domain.exceptions.common;

import fr.sharingcraftsman.user.domain.exceptions.authentication.CredentialException;

public class UsernameException extends CredentialException {
  public UsernameException(String message) {
    super(message);
  }
}
