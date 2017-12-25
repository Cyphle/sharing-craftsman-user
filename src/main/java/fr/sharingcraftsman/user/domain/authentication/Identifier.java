package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.utils.DateService;

public class Identifier implements Authenticator {
  private DateService dateService;

  public Identifier(DateService dateService, HumanResourceAdministrator humanResourceAdministrator, TokenAdministrator tokenAdministrator) {
    this.dateService = dateService;
  }

  @Override
  public Token identify(Credentials credentials) {
    // Retrieve user from username and password
    // Create new tokens
    // Send tokens
    throw new UnsupportedOperationException();
  }

  // Need verify token

  // Need refresh token from refresh token
}
