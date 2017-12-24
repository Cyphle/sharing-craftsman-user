package fr.sharingcraftsman.user.command.adapters;

import fr.sharingcraftsman.user.command.common.User;
import fr.sharingcraftsman.user.command.pivots.UserPivot;
import fr.sharingcraftsman.user.command.repositories.UserRepository;
import fr.sharingcraftsman.user.domain.authentication.CredentialException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Person;
import fr.sharingcraftsman.user.domain.company.UnkownCollaborator;
import org.springframework.beans.factory.annotation.Autowired;

public class UserAdapter implements HumanResourceAdministrator {
  private UserRepository userRepository;

  @Autowired
  public UserAdapter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void saveCollaborator(Collaborator collaborator) {
    User user = UserPivot.fromDomainToInfra(collaborator);
    userRepository.save(user);
  }

  @Override
  public Person getCollaborator(Username username) {
    User foundUser = userRepository.findByUsername(username.getUsername());

    if (foundUser == null)
      return new UnkownCollaborator();

    try {
      return UserPivot.fromInfraToDomain(foundUser);
    } catch (CredentialException e) {
      return new UnkownCollaborator();
    }
  }
}
