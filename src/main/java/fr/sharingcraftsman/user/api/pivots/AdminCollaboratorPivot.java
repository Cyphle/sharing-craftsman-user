package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.admin.AdminUserDTO;
import fr.sharingcraftsman.user.domain.admin.UserForAdmin;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AdminCollaboratorPivot {
  public static UserForAdmin fromApiToDomain(AdminUserDTO user) {
    return UserForAdmin.from(
            user.getUsername(),
            user.getPassword(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.getWebsite(),
            user.getGithub(),
            user.getLinkedin(),
            user.isActive(),
            fromLongToLocalDateTime(user.getCreationDate()),
            fromLongToLocalDateTime(user.getLastUpdateDate())
    );
  }

  private static LocalDateTime fromLongToLocalDateTime(long value) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
  }
}
