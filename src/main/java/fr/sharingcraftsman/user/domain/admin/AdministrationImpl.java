package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.exceptions.AlreadyExistingUserException;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.admin.ports.Administration;

import java.util.List;

public class AdministrationImpl implements Administration {
  private UserForAdminRepository userForAdminRepository;

  public AdministrationImpl(UserForAdminRepository userForAdminRepository) {
    this.userForAdminRepository = userForAdminRepository;
  }

  @Override
  public List<UserForAdmin> getAllCollaborators() {
    return userForAdminRepository.getAllCollaborators();
  }

  @Override
  public void deleteCollaborator(Username username) throws UserException {
    AbstractUser user = userForAdminRepository.findCollaboratorFromUsername(username);

    if (!user.isKnown())
      throw new UnknownUserException("Unknown user");

    userForAdminRepository.deleteCollaborator(username);
  }

  @Override
  public void updateCollaborator(UserForAdmin collaborator) throws UnknownUserException {
    BaseUserForAdmin collaboratorToUpdate = userForAdminRepository.findAdminCollaboratorFromUsername(collaborator.getUsername());

    if (!collaboratorToUpdate.isKnown())
      throw new UnknownUserException("Unknown collaborator");

    ((UserForAdmin) collaboratorToUpdate).updateFields(collaborator);
    userForAdminRepository.updateCollaborator((UserForAdmin) collaboratorToUpdate);
  }

  @Override
  public void createCollaborator(UserForAdmin collaborator) throws AlreadyExistingUserException {
    BaseUserForAdmin foundCollaborator = userForAdminRepository.findAdminCollaboratorFromUsername(collaborator.getUsername());

    if (foundCollaborator.isKnown())
      throw new AlreadyExistingUserException("User already exists with username: " + collaborator.getUsernameContent());

    userForAdminRepository.createCollaborator(collaborator);
  }
}
