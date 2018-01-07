package fr.sharingcraftsman.user.domain.ports.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.user.*;

public interface Company {
  void createNewCollaborator(Credentials credentials) throws CollaboratorException, CredentialsException;

  ChangePasswordKey createChangePasswordKeyFor(Credentials credentials) throws UnknownCollaboratorException;

  void changePassword(Credentials credentials, ChangePassword changePassword) throws CollaboratorException;

  BaseProfile updateProfile(BaseProfile baseProfile) throws CollaboratorException;

  Email findEmailOf(Credentials credentials);
}
