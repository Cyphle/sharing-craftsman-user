package fr.sharingcraftsman.user.infrastructure.adapters;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.admin.AdminPerson;
import fr.sharingcraftsman.user.domain.admin.HRAdminManager;
import fr.sharingcraftsman.user.domain.admin.UnknownAdminCollaborator;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.Person;
import fr.sharingcraftsman.user.domain.company.UnknownCollaborator;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.pivots.UserPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HRAdminAdapter implements HRAdminManager {
  private UserRepository userRepository;
  private DateService dateService;

  @Autowired
  public HRAdminAdapter(UserRepository userRepository, DateService dateService) {
    this.userRepository = userRepository;
    this.dateService = dateService;
  }

  @Override
  public List<AdminCollaborator> getAllCollaborators() {
    List<User> users = Lists.newArrayList(userRepository.findAll());
    return UserPivot.fromInfraToAdminDomain(users);
  }

  @Override
  public void deleteCollaborator(Username username) {
    User foundUser = userRepository.findByUsername(username.getUsername());
    userRepository.delete(foundUser);
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
  public void updateCollaborator(AdminCollaborator collaborator) {
    User foundUser = userRepository.findByUsername(collaborator.getUsernameContent());
    foundUser.updateFromAdminCollaborator(collaborator);
    foundUser.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(foundUser);
  }

  @Override
  public AdminPerson findAdminCollaboratorFromUsername(Username username) {
    User foundUser = userRepository.findByUsername(username.getUsername());

    if (foundUser == null)
      return new UnknownAdminCollaborator();

    return UserPivot.fromInfraToAdminDomain(foundUser);
  }

  @Override
  public void createCollaborator(AdminCollaborator collaborator) {
    User userToCreate = UserPivot.fromDomainToInfra(collaborator);
    userToCreate.setCreationDate(dateService.nowInDate());
    userToCreate.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(userToCreate);
  }
}
