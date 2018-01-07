package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.admin.AdminUserDTO;
import fr.sharingcraftsman.user.domain.admin.UserForBaseUserForAdmin;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AdminCollaboratorPivot {
  public static UserForBaseUserForAdmin fromApiToDomain(AdminUserDTO user) {
    return UserForBaseUserForAdmin.from(
            user.getUsername(),
            user.getPassword(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.getWebsite(),
            user.getGithub(),
            user.getLinkedin(),
            user.getChangePasswordKey(),
            fromLongToLocalDateTime(user.getChangePasswordKeyExpirationDate()),
            user.isActive(),
            fromLongToLocalDateTime(user.getCreationDate()),
            fromLongToLocalDateTime(user.getLastUpdateDate())
    );
  }

  private static LocalDateTime fromLongToLocalDateTime(long value) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
  }
}
