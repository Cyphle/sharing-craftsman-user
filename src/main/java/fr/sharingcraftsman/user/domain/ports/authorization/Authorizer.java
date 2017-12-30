package fr.sharingcraftsman.user.domain.ports.authorization;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authorization.Authorizations;

public interface Authorizer {
  Authorizations getAuthorizationsOf(Credentials credentials);
}
