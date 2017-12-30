package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.company.*;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.pivots.UserPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

@Service
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
    user.setCreationDate(dateService.nowInDate());
    user.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(user);
  }

  @Override
  public Person findCollaboratorFromUsername(Username username) {
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
  public Person findCollaboratorFromCredentials(Credentials credentials) {
    User foundUser = userRepository.findByUsernameAndPassword(credentials.getUsernameContent(), credentials.getPasswordContent());

    if (foundUser == null)
      return new UnknownCollaborator();

    try {
      return UserPivot.fromInfraToDomain(foundUser);
    } catch (CredentialsException e) {
      return new UnknownCollaborator();
    }
  }

  @Override
  public void deleteChangePasswordKeyOf(Credentials credentials) {
    User user = userRepository.findByUsername(credentials.getUsernameContent());
    user.setChangePasswordKey("");
    user.setChangePasswordExpirationDate(null);
    userRepository.save(user);
  }

  @Override
  public ChangePasswordKey createChangePasswordKeyFor(ChangePasswordKey changePasswordKey) {
    User user = userRepository.findByUsername(changePasswordKey.getUsername());
    user.setChangePasswordKey(changePasswordKey.getKey());
    user.setChangePasswordExpirationDate(Date.from(changePasswordKey.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant()));
    userRepository.save(user);
    return changePasswordKey;
  }

  @Override
  public void updateCollaborator(Collaborator collaborator) {
    User user = userRepository.findByUsername(collaborator.getUsername());
    user.setPassword(collaborator.getPassword());
    userRepository.save(user);
  }

  @Override
  public Profile findProfileOf(Username username) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Profile updateProfileOf(Profile profileToUpdate) {
    throw new UnsupportedOperationException();
  }
}
