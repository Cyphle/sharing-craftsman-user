package fr.sharingcraftsman.user.domain.ports.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.company.ChangePasswordKey;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;

public interface Company {
  void createNewCollaborator(Credentials credentials) throws CollaboratorException;

  ChangePasswordKey createChangePasswordKeyFor(Credentials credentials);
}
