package fr.sharingcraftsman.user.domain.user.exceptions;

public class AlreadyExistingUserException extends UserException {
  public AlreadyExistingUserException(String message) {
    super(message);
  }
}
