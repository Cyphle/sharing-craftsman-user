package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AbstractUserInfo;
import fr.sharingcraftsman.user.domain.admin.UserInfoOld;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HRAdminAdapterTest {
  @Mock
  private UserJpaRepository userJpaRepository;
  @Mock
  private DateService dateService;
  private UserForAdminAdapter hrAdminAdapter;

  @Before
  public void setUp() throws Exception {
    hrAdminAdapter = new UserForAdminAdapter(userJpaRepository, dateService);
    given(dateService.nowInDate()).willReturn(DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0)));
  }

  @Test
  public void should_get_all_users() throws Exception {
    UserEntity userEntity = new UserEntity("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe");
    userEntity.setActive(true);
    userEntity.setCreationDate(new Date());
    userEntity.setLastUpdateDate(new Date());
    UserEntity userEntityTwo = new UserEntity("foo@bar.fr", "password", "Foo", "Bar", "foo@bar.fr", "www.foobar.fr", "github.com/foobar", "linkedin.com/foobar");
    userEntityTwo.setActive(true);
    userEntityTwo.setCreationDate(new Date());
    userEntityTwo.setLastUpdateDate(new Date());
    given(userJpaRepository.findAll()).willReturn(Arrays.asList(userEntity, userEntityTwo));

    hrAdminAdapter.getAllUsers();

    verify(userJpaRepository).findAll();
  }

  @Test
  public void should_delete_user() throws Exception {
    hrAdminAdapter.deleteUser(Username.from("hello@world.fr"));

    verify(userJpaRepository).delete(any(UserEntity.class));
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    AbstractUser user = hrAdminAdapter.findUserFromUsername(Username.from("john@doe.fr"));

    User expected = User.from(Credentials.buildWithEncryption("john@doe.fr", "password"));
    assertThat((User) user).isEqualTo(expected);
  }

  @Test
  public void should_get_user_by_username_in_admin_user_object() throws Exception {
    UserEntity userEntity = new UserEntity("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    userEntity.setActive(true);
    userEntity.setCreationDate(new Date());
    userEntity.setLastUpdateDate(new Date());
    given(userJpaRepository.findByUsername("admin@toto.fr")).willReturn(userEntity);

    AbstractUserInfo user = hrAdminAdapter.findAdminUserFromUsername(Username.from("admin@toto.fr"));

    UserInfoOld expectedUser = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    assertThat(user).isEqualTo(expectedUser);
  }

  @Test
  public void should_update_user() throws Exception {
    UserEntity userEntityToUpdate = new UserEntity("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    given(userJpaRepository.findByUsername("admin@toto.fr")).willReturn(userEntityToUpdate);

    UserInfoOld userToUpdate = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    hrAdminAdapter.updateUser(userToUpdate);

    UserEntity updatedUserEntity = new UserEntity("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    updatedUserEntity.setActive(true);
    updatedUserEntity.setCreationDate(new Date());
    updatedUserEntity.setLastUpdateDate(new Date());
    verify(userJpaRepository).save(updatedUserEntity);
  }

  @Test
  public void should_create_user() throws Exception {
    UserInfoOld user = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    hrAdminAdapter.createUser(user);

    UserEntity newUserEntity = new UserEntity("admin@toto.fr", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    verify(userJpaRepository).save(newUserEntity);
  }
}
