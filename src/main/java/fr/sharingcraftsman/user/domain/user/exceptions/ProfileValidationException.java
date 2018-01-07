package fr.sharingcraftsman.user.domain.user.exceptions;

import fr.sharingcraftsman.user.domain.common.ValidationError;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;

import java.util.List;

public class ProfileValidationException extends UserException {
  private final List<ValidationError> errors;

  public ProfileValidationException(String message, List<ValidationError> errors) {
    super(message);
    this.errors = errors;
  }

  public List<ValidationError> getErrors() {
    return errors;
  }
}
