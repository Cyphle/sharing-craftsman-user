package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.BaseUserForAdmin;
import fr.sharingcraftsman.user.domain.admin.UserForAdmin;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.BaseUser;

import java.util.List;

public interface UserForAdminRepository {
  List<UserForAdmin> getAllCollaborators();

  void deleteCollaborator(Username username);

  BaseUser findCollaboratorFromUsername(Username username);

  void updateCollaborator(UserForAdmin collaborator);

  BaseUserForAdmin findAdminCollaboratorFromUsername(Username username);

  void createCollaborator(UserForAdmin collaborator);
}
