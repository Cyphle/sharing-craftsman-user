package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.UnknownUser;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserOrganisationImplAdminTest {
  @Mock
  private UserForAdminRepository userForAdminRepository;

  private AdministrationImpl organisation;

  @Before
  public void setUp() throws Exception {
    organisation = new AdministrationImpl(userForAdminRepository);
  }

  @Test
  public void should_get_all_users() throws Exception {
    organisation.getAllUsers();

    verify(userForAdminRepository).getAllUsers();
  }

  @Test
  public void should_delete_user_if_exists() throws Exception {
    given(userForAdminRepository.findUserFromUsername(Username.from("hello@world.fr"))).willReturn(User.from("hello@world.fr", "password"));

    organisation.deleteUser(Username.from("hello@world.fr"));

    verify(userForAdminRepository).findUserFromUsername(Username.from("hello@world.fr"));
    verify(userForAdminRepository).deleteUser(Username.from("hello@world.fr"));
  }

  @Test
  public void should_throw_unknown_user_exception_if_user_not_found() throws Exception {
    try {
      given(userForAdminRepository.findUserFromUsername(any(Username.class))).willReturn(new UnknownUser());
      organisation.deleteUser(Username.from("hello@world.fr"));
      fail("Should have throw user exception when not found");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown user");
    }
  }

  @Test
  public void should_update_user() throws Exception {
    UserInfoOld foundUser = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    given(userForAdminRepository.findAdminUserFromUsername(foundUser.getUsername())).willReturn(foundUser);

    UserInfoOld userToUpdate = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    organisation.updateUser(userToUpdate);

    UserInfoOld expectedUser = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    verify(userForAdminRepository).findAdminUserFromUsername(expectedUser.getUsername());
    verify(userForAdminRepository).updateUser(expectedUser);
  }

  @Test
  public void should_create_user() throws Exception {
    given(userForAdminRepository.findAdminUserFromUsername(any(Username.class))).willReturn(new UnknownUserInfo());

    UserInfoOld userToCreate = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    organisation.createUser(userToCreate);

    UserInfoOld expectedUser = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    verify(userForAdminRepository).findAdminUserFromUsername(expectedUser.getUsername());
    verify(userForAdminRepository).createUser(expectedUser);
  }
}
