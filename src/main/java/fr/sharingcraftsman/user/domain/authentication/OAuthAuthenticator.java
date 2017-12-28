package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.*;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.utils.DateHelper;

public class OAuthAuthenticator implements Authenticator {
  private HumanResourceAdministrator humanResourceAdministrator;
  private TokenAdministrator tokenAdministrator;
  private DateHelper dateHelper;

  public OAuthAuthenticator(HumanResourceAdministrator humanResourceAdministrator, TokenAdministrator tokenAdministrator, DateHelper dateHelper) {
    this.humanResourceAdministrator = humanResourceAdministrator;
    this.tokenAdministrator = tokenAdministrator;
    this.dateHelper = dateHelper;
  }

  @Override
  public Token login(Credentials credentials, Client client) throws CollaboratorException {
    Person collaborator = humanResourceAdministrator.findFromCredentials(credentials);

    if (!collaborator.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");

    tokenAdministrator.deleteTokensOf((Collaborator) collaborator, client);
    return tokenAdministrator.createNewToken((Collaborator) collaborator, client, credentials.stayLogged());
  }

  @Override
  public boolean isTokenValid(Credentials credentials, Client client, ValidToken token) {
    Token foundToken = tokenAdministrator.findTokenFor(client, credentials, token);

    if (foundToken.isValid()) {
      ValidToken validToken = (ValidToken) foundToken;
      if (validToken.getExpirationDate().isBefore(dateHelper.now()))
        return false;
    }

    return foundToken.isValid();
  }

  // Need verify token

  // Need refresh token from refresh token
}
