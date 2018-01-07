package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserEntityAdapterTest {
  @Mock
  private UserJpaRepository userJpaRepository;
  @Mock
  private DateService dateService;
  private UserRepository userAdapter;

  @Before
  public void setUp() throws Exception {
    userAdapter = new UserAdapter(userJpaRepository, dateService);
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
  }

  @Test
  public void should_save_user_in_repository() throws Exception {
    BaseUser collaborator = User.from(Credentials.buildWithEncryption("john@doe.fr", "password"));

    userAdapter.createNewUser((User) collaborator);

    UserEntity expectedUserEntity = new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    expectedUserEntity.setCreationDate(dateService.nowInDate());
    expectedUserEntity.setLastUpdateDate(dateService.nowInDate());
    verify(userJpaRepository).save(expectedUserEntity);
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    BaseUser collaborator = userAdapter.findUserFromUsername(usernameBuilder.from("john@doe.fr"));

    User expected = User.from(Credentials.buildWithEncryption("john@doe.fr", "password"));
    assertThat((User) collaborator).isEqualTo(expected);
  }

  @Test
  public void should_find_user_by_username_and_password() throws Exception {
    given(userJpaRepository.findByUsernameAndPassword("john@doe.fr", "T49xWf/l7gatvfVwethwDw==")).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    BaseUser collaborator = userAdapter.findUserFromCredentials(Credentials.buildWithEncryption("john@doe.fr", "password"));

    assertThat((User) collaborator).isEqualTo(User.from(Credentials.buildWithEncryption("john@doe.fr", "password")));
  }

  @Test
  public void should_delete_user_change_password_key() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    userAdapter.deleteChangePasswordKeyOf(Credentials.build("john@doe.fr", "NOPASSWORD"));

    UserEntity userEntity = new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    verify(userJpaRepository).save(userEntity);
  }

  @Test
  public void should_update_user_with_change_password_key() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    ChangePasswordKey changePasswordKey = ChangePasswordKey.from(
            User.from("john@doe.fr", "aaa"),
            "aaa",
            LocalDateTime.of(2017, 12, 25, 12, 0)
    );
    userAdapter.createChangePasswordKeyFor(changePasswordKey);

    UserEntity userEntity = new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    userEntity.setChangePasswordKey("aaa");
    verify(userJpaRepository).save(userEntity);
  }

  @Test
  public void should_update_user_with_new_password() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    User user = User.from("john@doe.fr", "newpassword");
    userAdapter.updateUserPassword(user);

    UserEntity userEntity = new UserEntity("john@doe.fr", "newpassword");
    verify(userJpaRepository).save(userEntity);
  }

  @Test
  public void should_find_profile_from_username() throws Exception {
    UserEntity userEntity = new UserEntity("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe");
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(userEntity);

    BaseProfile foundBaseProfile = userAdapter.findProfileOf(usernameBuilder.from("john@doe.fr"));

    Profile expectedProfile = new ProfileBuilder().withUsername(usernameBuilder.from("john@doe.fr")).withFirstname(Name.of("John")).withLastname(Name.of("Doe")).withEmail(Email.from("john@doe.fr")).withWebsite(Link.to("www.johndoe.fr")).withGithub(Link.to("github.com/johndoe")).withLinkedin(Link.to("linkedin.com/johndoe")).build();
    assertThat((Profile) foundBaseProfile).isEqualTo(expectedProfile);
    verify(userJpaRepository).findByUsername("john@doe.fr");
  }

  @Test
  public void should_save_new_profile() throws Exception {
    UserEntity userEntity = new UserEntity("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe");
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(userEntity);
    given(userJpaRepository.save(any(UserEntity.class))).willReturn(userEntity);
    Profile profile = new ProfileBuilder().withUsername(usernameBuilder.from("john@doe.fr")).withFirstname(Name.of("John")).withLastname(Name.of("Doe")).withEmail(Email.from("john@doe.fr")).withWebsite(Link.to("www.johndoe.fr")).withGithub(Link.to("github.com/johndoe")).withLinkedin(Link.to("linkedin.com/johndoe")).build();

    BaseProfile foundBaseProfile = userAdapter.updateProfileOf(profile);

    assertThat((Profile) foundBaseProfile).isEqualTo(profile);
    verify(userJpaRepository).findByUsername("john@doe.fr");
  }
}
