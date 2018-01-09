package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.ports.ChangePasswordTokenRepository;
import fr.sharingcraftsman.user.infrastructure.models.ChangePasswordTokenEntity;
import fr.sharingcraftsman.user.infrastructure.pivots.ChangePasswordTokenPivot;
import fr.sharingcraftsman.user.infrastructure.pivots.UserPivot;
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
    User user = UserPivot.fromInfraToDomain(userRepository.findByUsername(username.getUsername()));
    return ChangePasswordTokenPivot.fromInfraToDomain(user, changePasswordTokenRepository.findByUsername(username.getUsername()));
  }

  @Override
  public void deleteChangePasswordKeyOf(Username username) {
    ChangePasswordTokenEntity token = changePasswordTokenRepository.findByUsername(username.getUsername());
    changePasswordTokenRepository.delete(token);
  }

  @Override
  public ChangePasswordToken createChangePasswordKeyFor(ChangePasswordToken changePasswordToken) throws CredentialsException {
    ChangePasswordTokenEntity token = ChangePasswordTokenPivot.fromDomainToInfra(changePasswordToken);
    User user = UserPivot.fromInfraToDomain(userRepository.findByUsername(changePasswordToken.getUsername()));
    return ChangePasswordTokenPivot.fromInfraToDomain(user, changePasswordTokenRepository.save(token));
  }
}
