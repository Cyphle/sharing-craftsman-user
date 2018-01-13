package fr.sharingcraftsman.user.infrastructure.adapters;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AbstractUserInfo;
import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.admin.UnknownUserInfo;
import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.UnknownUser;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserAdapter implements AdminUserRepository {
  private UserJpaRepository userRepository;
  private DateService dateService;

  @Autowired
  public AdminUserAdapter(UserJpaRepository userRepository, DateService dateService) {
    this.userRepository = userRepository;
    this.dateService = dateService;
  }

  @Override
  public AbstractUser findUserFromUsername(Username username) {
    UserEntity foundUserEntity = userRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownUser();

    try {
      return UserEntity.fromInfraToDomain(foundUserEntity);
    } catch (CredentialsException e) {
      return new UnknownUser();
    }
  }

  @Override
  public AbstractUserInfo findUserInfoFromUsername(Username username) {
    UserEntity foundUserEntity = userRepository.findByUsername(username.getUsername());

    if (foundUserEntity == null)
      return new UnknownUserInfo();

    try {
      return UserEntity.fromInfraToAdminDomain(foundUserEntity);
    } catch (UsernameException | PasswordException e) {
      return new UnknownUserInfo();
    }
  }

  @Override
  public List<User> getAllUsers() {
    return Lists.newArrayList(userRepository.findAll())
            .stream()
            .map(this::fromInfraToDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<Profile> getAllProfiles() {
    return Lists.newArrayList(userRepository.findAll())
            .stream()
            .map(this::fromInfraToDomainProfile)
            .collect(Collectors.toList());
  }

  @Override
  public List<TechnicalUserDetails> getAllTechnicalUserDetails() {
    return Lists.newArrayList(userRepository.findAll())
            .stream()
            .map(this::fromInfraToDomainTechnicalUserDetails)
            .collect(Collectors.toList());
  }

  @Override
  public void createUser(UserInfo user) {
    UserEntity userEntityToCreate = UserEntity.fromDomainToInfra(user);
    userEntityToCreate.setCreationDate(dateService.nowInDate());
    userEntityToCreate.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(userEntityToCreate);
  }

  @Override
  public void updateUser(UserInfo userToUpdate) {
    UserEntity foundUserEntity = userRepository.findByUsername(userToUpdate.getUsernameContent());
    foundUserEntity.updateFromAdmin(userToUpdate);
    foundUserEntity.setLastUpdateDate(dateService.nowInDate());
    userRepository.save(foundUserEntity);
  }

  @Override
  public void deleteUser(Username username) {
    UserEntity foundUserEntity = userRepository.findByUsername(username.getUsername());
    userRepository.delete(foundUserEntity);
  }

  private User fromInfraToDomain(UserEntity user) {
    try {
      return UserEntity.fromInfraToDomain(user);
    } catch (CredentialsException e) {
      return null;
    }
  }

  private Profile fromInfraToDomainProfile(UserEntity profile) {
    try {
      return UserEntity.fromInfraToDomainProfile(profile);
    } catch (UsernameException e) {
      return null;
    }
  }

  private TechnicalUserDetails fromInfraToDomainTechnicalUserDetails(UserEntity detail) {
    try {
      return TechnicalUserDetails.fromInfraToDomain(detail);
    } catch (UsernameException e) {
      return null;
    }
  }
}
