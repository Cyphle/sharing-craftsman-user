package fr.sharingcraftsman.user.domain.ports.authorization;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authorization.Authorizations;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;

import java.util.Set;

public interface Authorizer {
  Authorizations getAuthorizationsOf(Credentials credentials);

  void addGroup(Credentials credentials, Groups groupToAdd);

  Set<Group> getAllRolesWithTheirGroups();
}
