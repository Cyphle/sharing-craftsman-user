package fr.sharingcraftsman.user.domain.ports.authentication;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.Token;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;

public interface Authenticator {
  Token login(Credentials credentials, Client client) throws CollaboratorException;
}