package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.admin.UserForAdmin;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserPivot {
  public static UserEntity fromDomainToInfra(User user) {
    return new UserEntity(user.getUsername(), user.getPassword());
  }

  public static User fromInfraToDomain(UserEntity userEntity) throws CredentialsException {
    return User.from(userEntity.getUsername(), userEntity.getPassword());
  }

  public static UserEntity fromDomainToInfra(UserForAdmin collaborator) {
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

  public static List<UserForAdmin> fromInfraToAdminDomain(List<UserEntity> userEntities) {
    return userEntities.stream()
            .map(user -> UserForAdmin.from(
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getWebsite(),
                    user.getGithub(),
                    user.getLinkedin(),
                    user.isActive(),
                    user.getCreationDate(),
                    user.getLastUpdateDate()
            ))
            .collect(Collectors.toList());
  }

  public static UserForAdmin fromInfraToAdminDomain(UserEntity userEntity) {
    return UserForAdmin.from(
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.getFirstname(),
            userEntity.getLastname(),
            userEntity.getEmail(),
            userEntity.getWebsite(),
            userEntity.getGithub(),
            userEntity.getLinkedin(),
            userEntity.isActive(),
            userEntity.getCreationDate(),
            userEntity.getLastUpdateDate());
  }

  public static Profile fromInfraToDomainProfile(UserEntity userEntity) throws UsernameException {
    return Profile.from(
            Username.from(userEntity.getUsername()),
            Name.of(userEntity.getFirstname()),
            Name.of(userEntity.getLastname()),
            Email.from(userEntity.getEmail()),
            Link.to(userEntity.getWebsite()),
            Link.to(userEntity.getGithub()),
            Link.to(userEntity.getLinkedin()));
  }

  public static UserEntity fromDomainToInfraProfile(Profile profile) {
    return new UserEntity(profile.getUsernameContent(), profile.getFirstnameContent(), profile.getLastnameContent(), profile.getEmailContent(), profile.getWebsiteContent(), profile.getGithubContent(), profile.getLinkedinContent());
  }
}
