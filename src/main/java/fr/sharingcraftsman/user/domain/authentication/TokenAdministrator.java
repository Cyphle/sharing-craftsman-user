package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;

public interface TokenAdministrator {
  void deleteTokensOf(Collaborator collaborator, Client client);

  ValidToken createNewToken(Collaborator collaborator, Client client, boolean isLongToken);

  Token findTokenFor(Client client, Credentials credentials, ValidToken token);
}
