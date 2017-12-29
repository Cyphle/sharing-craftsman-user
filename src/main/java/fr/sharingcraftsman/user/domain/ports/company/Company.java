package fr.sharingcraftsman.user.domain.ports.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.company.ChangePassword;
import fr.sharingcraftsman.user.domain.company.ChangePasswordKey;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.UnknownCollaboratorException;

public interface Company {
  void createNewCollaborator(Credentials credentials) throws CollaboratorException, CredentialsException;

  ChangePasswordKey createChangePasswordKeyFor(Credentials credentials);

  void changePassword(Credentials credentials, ChangePassword changePassword) throws CollaboratorException;
}
