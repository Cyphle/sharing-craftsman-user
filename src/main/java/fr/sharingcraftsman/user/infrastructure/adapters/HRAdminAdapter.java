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
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
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
    List<UserEntity> userEntities = Lists.newArrayList(userRepository.findAll());
    return UserPivot.fromInfraToAdminDomain(userEntities);
  }

  @Override
  public void deleteCollaborator(Username username) {
    UserEntity foundUserEntity = userRepository.findByUsername(username.getUsername());
    userRepository.delete(foundUserEntity);
  }

  @Override
  public Person findCollaboratorFromUsername(Username username) {
    UserEntity foundUserEntity = userRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownCollaborator();

    try {
      return UserPivot.fromInfraToDomain(foundUserEntity);
    } catch (CredentialsException e) {
      return new UnknownCollaborator();
    }
  }

  @Override
  public void updateCollaborator(AdminCollaborator collaborator) {
    UserEntity foundUserEntity = userRepository.findByUsername(collaborator.getUsernameContent());
    foundUserEntity.updateFromAdminCollaborator(collaborator);
    foundUserEntity.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(foundUserEntity);
  }

  @Override
  public AdminPerson findAdminCollaboratorFromUsername(Username username) {
    UserEntity foundUserEntity = userRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownAdminCollaborator();

    return UserPivot.fromInfraToAdminDomain(foundUserEntity);
  }

  @Override
  public void createCollaborator(AdminCollaborator collaborator) {
    UserEntity userEntityToCreate = UserPivot.fromDomainToInfra(collaborator);
    userEntityToCreate.setCreationDate(dateService.nowInDate());
    userEntityToCreate.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(userEntityToCreate);
  }
}
