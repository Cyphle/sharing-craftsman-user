package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.common.ValidationError;

import java.util.List;

public class ProfileException extends CollaboratorException {
  private final List<ValidationError> errors;

  public ProfileException(String message, List<ValidationError> errors) {
    super(message);
    this.errors = errors;
  }

  public List<ValidationError> getErrors() {
    return errors;
  }
}
