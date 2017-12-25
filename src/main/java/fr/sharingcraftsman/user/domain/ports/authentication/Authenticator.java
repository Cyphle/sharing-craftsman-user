package fr.sharingcraftsman.user.domain.ports.authentication;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.Token;

public interface Authenticator {
  Token identify(Credentials credentials);
}
