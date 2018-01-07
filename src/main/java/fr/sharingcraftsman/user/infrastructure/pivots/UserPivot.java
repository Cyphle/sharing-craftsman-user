package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.admin.UserForBaseUserForAdmin;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.CollaboratorBuilder;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.ProfileBuilder;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class UserPivot {
  public static UserEntity fromDomainToInfra(User user) {
    return new UserEntity(user.getUsername(), user.getPassword());
  }

  public static User fromInfraToDomain(UserEntity userEntity) throws CredentialsException {
    String changePasswordKey = userEntity.getChangePasswordKey() != null ? userEntity.getChangePasswordKey() : "";
    LocalDateTime changePasswordKeyExpirationDate = userEntity.getChangePasswordExpirationDate() != null ? LocalDateTime.ofInstant(userEntity.getChangePasswordExpirationDate().toInstant(), ZoneId.systemDefault()) : null;

    return (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from(userEntity.getUsername()))
            .withPassword(userEntity.getPassword() != null ? passwordBuilder.from(userEntity.getPassword()) : null)
            .withChangePasswordKey(changePasswordKey)
            .withChangePasswordKeyExpirationDate(changePasswordKeyExpirationDate)
            .build();
  }

  public static UserEntity fromDomainToInfra(UserForBaseUserForAdmin collaborator) {
    return new UserEntity(
            collaborator.getUsernameContent(),
            collaborator.getFirstname(),
            collaborator.getLastname(),
            collaborator.getEmail(),
            collaborator.getWebsite(),
            collaborator.getGithub(),
            collaborator.getLinkedin()
    );
  }

  public static List<UserForBaseUserForAdmin> fromInfraToAdminDomain(List<UserEntity> userEntities) {
    return userEntities.stream()
            .map(user -> UserForBaseUserForAdmin.from(
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getWebsite(),
                    user.getGithub(),
                    user.getLinkedin(),
                    user.getChangePasswordKey(),
                    user.getChangePasswordExpirationDate(),
                    user.isActive(),
                    user.getCreationDate(),
                    user.getLastUpdateDate()
            ))
            .collect(Collectors.toList());
  }

  public static UserForBaseUserForAdmin fromInfraToAdminDomain(UserEntity userEntity) {
    return UserForBaseUserForAdmin.from(
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.getFirstname(),
            userEntity.getLastname(),
            userEntity.getEmail(),
            userEntity.getWebsite(),
            userEntity.getGithub(),
            userEntity.getLinkedin(),
            userEntity.getChangePasswordKey(),
            userEntity.getChangePasswordExpirationDate(),
            userEntity.isActive(),
            userEntity.getCreationDate(),
            userEntity.getLastUpdateDate());
  }

  public static Profile fromInfraToDomainProfile(UserEntity userEntity) throws UsernameException {
    return new ProfileBuilder()
            .withUsername(usernameBuilder.from(userEntity.getUsername()))
            .withFirstname(Name.of(userEntity.getFirstname()))
            .withLastname(Name.of(userEntity.getLastname()))
            .withEmail(Email.from(userEntity.getEmail()))
            .withWebsite(Link.to(userEntity.getWebsite()))
            .withGithub(Link.to(userEntity.getGithub()))
            .withLinkedin(Link.to(userEntity.getLinkedin()))
            .build();
  }

  public static UserEntity fromDomainToInfraProfile(Profile profile) {
    return new UserEntity(profile.getUsernameContent(), profile.getFirstnameContent(), profile.getLastnameContent(), profile.getEmailContent(), profile.getWebsiteContent(), profile.getGithubContent(), profile.getLinkedinContent());
  }
}
