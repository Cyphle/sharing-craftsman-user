package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.pivots.UserPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public void createNewCollaborator(User user) {
    UserEntity userEntity = UserPivot.fromDomainToInfra(user);
    userEntity.setCreationDate(dateService.nowInDate());
    userEntity.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(userEntity);
  }

  @Override
  public BaseUser findCollaboratorFromUsername(Username username) {
    UserEntity foundUserEntity = userRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownUser();

    try {
      return UserPivot.fromInfraToDomain(foundUserEntity);
    } catch (CredentialsException e) {
      return new UnknownUser();
    }
  }

  @Override
  public BaseUser findCollaboratorFromCredentials(Credentials credentials) {
    UserEntity foundUserEntity = userRepository.findByUsernameAndPassword(credentials.getUsernameContent(), credentials.getPasswordContent());

    if (foundUserEntity == null)
      return new UnknownUser();

    try {
      return UserPivot.fromInfraToDomain(foundUserEntity);
    } catch (CredentialsException e) {
      return new UnknownUser();
    }
  }

  @Override
  public void deleteChangePasswordKeyOf(Credentials credentials) {
    UserEntity userEntity = userRepository.findByUsername(credentials.getUsernameContent());
    userEntity.setChangePasswordKey("");
    userEntity.setChangePasswordExpirationDate(null);
    userEntity.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(userEntity);
  }

  @Override
  public ChangePasswordKey createChangePasswordKeyFor(ChangePasswordKey changePasswordKey) {
    UserEntity userEntity = userRepository.findByUsername(changePasswordKey.getUsername());
    userEntity.setChangePasswordKey(changePasswordKey.getKey());
    userEntity.setChangePasswordExpirationDate(Date.from(changePasswordKey.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant()));
    userEntity.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(userEntity);
    return changePasswordKey;
  }

  @Override
  public void updateCollaboratorPassword(User user) {
    UserEntity userEntity = userRepository.findByUsername(user.getUsername());
    userEntity.setPassword(user.getPassword());
    userEntity.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(userEntity);
  }

  @Override
  public Profile findProfileOf(Username username) {
    UserEntity userEntity = userRepository.findByUsername(username.getUsername());

    if (userEntity == null)
      return new UnknownProfile();

    try {
      return UserPivot.fromInfraToDomainProfile(userEntity);
    } catch (UsernameException e) {
      return new UnknownProfile();
    }
  }

  @Override
  public Profile updateProfileOf(KnownProfile profileToUpdate) {
    UserEntity userEntity = userRepository.findByUsername(profileToUpdate.getUsernameContent());
    userEntity.updateFromProfile(UserPivot.fromDomainToInfraProfile(profileToUpdate));
    userEntity.setLastUpdateDate(dateService.nowInDate());
    UserEntity updatedUserEntity = userRepository.save(userEntity);
    try {
      return UserPivot.fromInfraToDomainProfile(updatedUserEntity);
    } catch (UsernameException e) {
      return new UnknownProfile();
    }
  }
}
