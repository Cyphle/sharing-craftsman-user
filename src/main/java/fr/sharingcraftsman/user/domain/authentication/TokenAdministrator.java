package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;

public interface TokenAdministrator {
  void deleteTokensOf(Collaborator collaborator, Client client);

  ValidToken createNewToken(Client client, Collaborator collaborator, ValidToken token);

  Token findTokenFor(Client client, Credentials credentials, ValidToken token);
}
