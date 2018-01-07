package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.UserForAdmin;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.exceptions.AlreadyExistingUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;

import java.util.List;

public interface Administration {
  List<UserForAdmin> getAllCollaborators();

  void deleteCollaborator(Username username) throws UserException;

  void updateCollaborator(UserForAdmin collaborator) throws UnknownUserException;

  void createCollaborator(UserForAdmin collaborator) throws AlreadyExistingUserException;
}
