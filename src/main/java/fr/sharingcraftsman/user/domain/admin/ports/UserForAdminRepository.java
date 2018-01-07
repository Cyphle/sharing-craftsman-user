package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.BaseUserForAdmin;
import fr.sharingcraftsman.user.domain.admin.UserForBaseUserForAdmin;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.BaseUser;

import java.util.List;

public interface UserForAdminRepository {
  List<UserForBaseUserForAdmin> getAllCollaborators();

  void deleteCollaborator(Username username);

  BaseUser findCollaboratorFromUsername(Username username);

  void updateCollaborator(UserForBaseUserForAdmin collaborator);

  BaseUserForAdmin findAdminCollaboratorFromUsername(Username username);

  void createCollaborator(UserForBaseUserForAdmin collaborator);
}
