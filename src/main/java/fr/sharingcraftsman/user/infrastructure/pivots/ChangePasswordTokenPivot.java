package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.ChangePasswordTokenEntity;

public class ChangePasswordTokenPivot {
  public static ChangePasswordToken fromInfraToDomain(User user, ChangePasswordTokenEntity token) {
    return ChangePasswordToken.from(user, token.getChangePasswordToken(), DateConverter.fromDateToLocalDateTime(token.getChangePasswordExpirationDate()));
  }

  public static ChangePasswordTokenEntity fromDomainToInfra(ChangePasswordToken changePasswordToken) {
    return new ChangePasswordTokenEntity(changePasswordToken.getUsername(), changePasswordToken.getToken(), DateConverter.fromLocalDateTimeToDate(changePasswordToken.getExpirationDate()));
  }
}
