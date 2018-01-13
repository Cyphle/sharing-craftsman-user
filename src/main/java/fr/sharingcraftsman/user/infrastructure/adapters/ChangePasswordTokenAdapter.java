package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.ports.ChangePasswordTokenRepository;
import fr.sharingcraftsman.user.infrastructure.models.ChangePasswordTokenEntity;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.ChangePasswordTokenJpaRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordTokenAdapter implements ChangePasswordTokenRepository {
  private UserJpaRepository userRepository;
  private ChangePasswordTokenJpaRepository changePasswordTokenRepository;

  @Autowired
  public ChangePasswordTokenAdapter(UserJpaRepository userRepository, ChangePasswordTokenJpaRepository changePasswordTokenJpaRepository) {
    this.userRepository = userRepository;
    this.changePasswordTokenRepository = changePasswordTokenJpaRepository;
  }

  @Override
  public ChangePasswordToken findByUsername(Username username) throws CredentialsException {
    User user = UserEntity.fromInfraToDomain(userRepository.findByUsername(username.getUsername()));
    return ChangePasswordTokenEntity.fromInfraToDomain(user, changePasswordTokenRepository.findByUsername(username.getUsername()));
  }

  @Override
  public ChangePasswordToken createChangePasswordTokenFor(ChangePasswordToken changePasswordToken) throws CredentialsException {
    ChangePasswordTokenEntity token = ChangePasswordTokenEntity.fromDomainToInfra(changePasswordToken);
    User user = UserEntity.fromInfraToDomain(userRepository.findByUsername(changePasswordToken.getUsername()));
    return ChangePasswordTokenEntity.fromInfraToDomain(user, changePasswordTokenRepository.save(token));
  }

  @Override
  public void deleteChangePasswordTokenOf(Username username) {
    ChangePasswordTokenEntity token = changePasswordTokenRepository.findByUsername(username.getUsername());
    if (token != null)
      changePasswordTokenRepository.delete(token);
  }
}
