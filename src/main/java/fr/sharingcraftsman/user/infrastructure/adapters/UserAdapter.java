package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.pivots.UserPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Person;
import fr.sharingcraftsman.user.domain.company.UnknownCollaborator;
import org.springframework.beans.factory.annotation.Autowired;

public class UserAdapter implements HumanResourceAdministrator {
  private UserRepository userRepository;
  private DateService dateService;

  @Autowired
  public UserAdapter(UserRepository userRepository, DateService dateService) {
    this.userRepository = userRepository;
    this.dateService = dateService;
  }

  @Override
  public void createNewCollaborator(Collaborator collaborator) {
    User user = UserPivot.fromDomainToInfra(collaborator);
    user.setCreationDate(dateService.now());
    user.setLastUpdateDate(dateService.now());
    userRepository.save(user);
  }

  @Override
  public Person getCollaborator(Username username) {
    User foundUser = userRepository.findByUsername(username.getUsername());

    if (foundUser == null)
      return new UnknownCollaborator();

    try {
      return UserPivot.fromInfraToDomain(foundUser);
    } catch (CredentialsException e) {
      return new UnknownCollaborator();
    }
  }

  @Override
  public Person findFromCredentials(Credentials credentials) {
    User foundUser = userRepository.findByUsernameAndPassword(credentials.getUsernameContent(), credentials.getPasswordContent());

    if (foundUser == null)
      return new UnknownCollaborator();

    try {
      return UserPivot.fromInfraToDomain(foundUser);
    } catch (CredentialsException e) {
      return new UnknownCollaborator();
    }
  }
}
