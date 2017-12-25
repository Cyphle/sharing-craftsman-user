package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.*;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.utils.DateService;

public class OAuthAuthenticator implements Authenticator {
  private HumanResourceAdministrator humanResourceAdministrator;
  private TokenAdministrator tokenAdministrator;

  public OAuthAuthenticator(HumanResourceAdministrator humanResourceAdministrator, TokenAdministrator tokenAdministrator) {
    this.humanResourceAdministrator = humanResourceAdministrator;
    this.tokenAdministrator = tokenAdministrator;
  }

  @Override
  public Token login(Credentials credentials, Client client) throws CollaboratorException {
    Person collaborator = humanResourceAdministrator.findFromCredentials(credentials);

    if (!collaborator.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");

    tokenAdministrator.deleteTokensOf((Collaborator) collaborator, client);
    return tokenAdministrator.createNewToken((Collaborator) collaborator, client, credentials.stayLogged());
  }

  // Need verify token

  // Need refresh token from refresh token
}
