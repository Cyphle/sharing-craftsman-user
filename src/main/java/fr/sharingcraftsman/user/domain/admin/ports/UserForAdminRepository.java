package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.BaseUserForAdmin;
import fr.sharingcraftsman.user.domain.admin.UserForAdmin;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.AbstractUser;

import java.util.List;

public interface UserForAdminRepository {
  List<UserForAdmin> getAllCollaborators();

  AbstractUser findCollaboratorFromUsername(Username username);

  BaseUserForAdmin findAdminCollaboratorFromUsername(Username username);

  void createCollaborator(UserForAdmin collaborator);

  void updateCollaborator(UserForAdmin collaborator);

  void deleteCollaborator(Username username);
}
