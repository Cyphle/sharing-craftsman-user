package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.company.CollaboratorBuilder;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.company.Collaborator;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class UserPivot {
  public static User fromDomainToInfra(Collaborator collaborator) {
    return new User(collaborator.getUsername(), collaborator.getPassword());
  }

  public static Collaborator fromInfraToDomain(User user) throws CredentialsException {
    String changePasswordKey = user.getChangePasswordKey() != null ? user.getChangePasswordKey() : "";
    LocalDateTime changePasswordKeyExpirationDate = user.getChangePasswordExpirationDate() != null ? LocalDateTime.ofInstant(user.getChangePasswordExpirationDate().toInstant(), ZoneId.systemDefault()) : null;

    return (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from(user.getUsername()))
            .withPassword(passwordBuilder.from(user.getPassword()))
            .withChangePasswordKey(changePasswordKey)
            .withChangePasswordKeyExpirationDate(changePasswordKeyExpirationDate)
            .build();
  }
}
