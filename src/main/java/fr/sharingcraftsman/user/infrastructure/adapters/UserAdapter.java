package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAdapter implements UserRepository {
  private UserJpaRepository userJpaRepository;
  private DateService dateService;

  @Autowired
  public UserAdapter(UserJpaRepository userJpaRepository, DateService dateService) {
    this.userJpaRepository = userJpaRepository;
    this.dateService = dateService;
  }

  @Override
  public AbstractUser findUserFromUsername(Username username) {
    UserEntity foundUserEntity = userJpaRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownUser();

    try {
      return UserEntity.fromInfraToDomain(foundUserEntity);
    } catch (CredentialsException e) {
      return new UnknownUser();
    }
  }

  @Override
  public AbstractUser findUserFromCredentials(Credentials credentials) {
    UserEntity foundUserEntity = userJpaRepository.findByUsernameAndPassword(credentials.getUsernameContent(), credentials.getPasswordContent());

    if (foundUserEntity == null)
      return new UnknownUser();

    try {
      return UserEntity.fromInfraToDomain(foundUserEntity);
    } catch (CredentialsException e) {
      return new UnknownUser();
    }
  }

  @Override
  public AbstractProfile findProfileOf(Username username) {
    UserEntity userEntity = userJpaRepository.findByUsername(username.getUsername());

    if (userEntity == null)
      return new UnknownProfile();

    try {
      return UserEntity.fromInfraToDomainProfile(userEntity);
    } catch (UsernameException e) {
      return new UnknownProfile();
    }
  }

  @Override
  public void createNewUser(User user) {
    UserEntity userEntity = UserEntity.fromDomainToInfra(user);
    userEntity.setCreationDate(dateService.nowInDate());
    userEntity.setLastUpdateDate(dateService.nowInDate());
    userJpaRepository.save(userEntity);
  }

  @Override
  public void updateUserPassword(User user) {
    UserEntity userEntity = userJpaRepository.findByUsername(user.getUsername());
    userEntity.setPassword(user.getPassword());
    userEntity.setLastUpdateDate(dateService.nowInDate());
    userJpaRepository.save(userEntity);
  }

  @Override
  public AbstractProfile updateProfileOf(Profile profileToUpdate) {
    UserEntity userEntity = userJpaRepository.findByUsername(profileToUpdate.getUsernameContent());
    userEntity.updateFromProfile(UserEntity.fromDomainToInfraProfile(profileToUpdate));
    userEntity.setLastUpdateDate(dateService.nowInDate());
    UserEntity updatedUserEntity = userJpaRepository.save(userEntity);
    try {
      return UserEntity.fromInfraToDomainProfile(updatedUserEntity);
    } catch (UsernameException e) {
      return new UnknownProfile();
    }
  }
}
