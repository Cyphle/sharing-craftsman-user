package fr.sharingcraftsman.user.infrastructure.adapters;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.UserForBaseUserForAdmin;
import fr.sharingcraftsman.user.domain.admin.BaseUserForAdmin;
import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.admin.exceptions.UnknownBaseUserForAdminCollaborator;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.BaseUser;
import fr.sharingcraftsman.user.domain.user.UnknownUser;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.pivots.UserPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserForAdminAdapter implements UserForAdminRepository {
  private UserJpaRepository userJpaRepository;
  private DateService dateService;

  @Autowired
  public UserForAdminAdapter(UserJpaRepository userJpaRepository, DateService dateService) {
    this.userJpaRepository = userJpaRepository;
    this.dateService = dateService;
  }

  @Override
  public List<UserForBaseUserForAdmin> getAllCollaborators() {
    List<UserEntity> userEntities = Lists.newArrayList(userJpaRepository.findAll());
    return UserPivot.fromInfraToAdminDomain(userEntities);
  }

  @Override
  public void deleteCollaborator(Username username) {
    UserEntity foundUserEntity = userJpaRepository.findByUsername(username.getUsername());
    userJpaRepository.delete(foundUserEntity);
  }

  @Override
  public BaseUser findCollaboratorFromUsername(Username username) {
    UserEntity foundUserEntity = userJpaRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownUser();

    try {
      return UserPivot.fromInfraToDomain(foundUserEntity);
    } catch (CredentialsException e) {
      return new UnknownUser();
    }
  }

  @Override
  public void updateCollaborator(UserForBaseUserForAdmin collaborator) {
    UserEntity foundUserEntity = userJpaRepository.findByUsername(collaborator.getUsernameContent());
    foundUserEntity.updateFromAdminCollaborator(collaborator);
    foundUserEntity.setLastUpdateDate(dateService.nowInDate());
    userJpaRepository.save(foundUserEntity);
  }

  @Override
  public BaseUserForAdmin findAdminCollaboratorFromUsername(Username username) {
    UserEntity foundUserEntity = userJpaRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownBaseUserForAdminCollaborator();

    return UserPivot.fromInfraToAdminDomain(foundUserEntity);
  }

  @Override
  public void createCollaborator(UserForBaseUserForAdmin collaborator) {
    UserEntity userEntityToCreate = UserPivot.fromDomainToInfra(collaborator);
    userEntityToCreate.setCreationDate(dateService.nowInDate());
    userEntityToCreate.setLastUpdateDate(dateService.nowInDate());
    userJpaRepository.save(userEntityToCreate);
  }
}
