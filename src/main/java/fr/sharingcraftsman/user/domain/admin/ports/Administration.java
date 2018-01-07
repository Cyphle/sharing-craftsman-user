package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.UserForBaseUserForAdmin;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.exceptions.AlreadyExistingUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;

import java.util.List;

public interface Administration {
  List<UserForBaseUserForAdmin> getAllCollaborators();

  void deleteCollaborator(Username username) throws UserException;

  void updateCollaborator(UserForBaseUserForAdmin collaborator) throws UnknownUserException;

  void createCollaborator(UserForBaseUserForAdmin collaborator) throws AlreadyExistingUserException;
}
