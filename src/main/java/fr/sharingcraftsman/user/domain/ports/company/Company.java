package fr.sharingcraftsman.user.domain.ports.company;

import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.company.*;

import java.util.List;

public interface Company {
  void createNewCollaborator(Credentials credentials) throws CollaboratorException, CredentialsException;

  ChangePasswordKey createChangePasswordKeyFor(Credentials credentials);

  void changePassword(Credentials credentials, ChangePassword changePassword) throws CollaboratorException;

  Profile updateProfile(Profile profile) throws CollaboratorException;
}
