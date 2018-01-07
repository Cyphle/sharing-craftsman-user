package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.admin.AdminPerson;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HRAdminAdapterTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private DateService dateService;
  private HRAdminAdapter hrAdminAdapter;

  @Before
  public void setUp() throws Exception {
    hrAdminAdapter = new HRAdminAdapter(userRepository, dateService);
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
  }

  @Test
  public void should_get_all_collaborators() throws Exception {
    UserEntity userEntity = new UserEntity("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe");
    userEntity.setActive(true);
    userEntity.setCreationDate(new Date());
    userEntity.setLastUpdateDate(new Date());
    userEntity.setChangePasswordKey("");
    userEntity.setChangePasswordExpirationDate(null);
    UserEntity userEntityTwo = new UserEntity("foo@bar.fr", "password", "Foo", "Bar", "foo@bar.fr", "www.foobar.fr", "github.com/foobar", "linkedin.com/foobar");
    userEntityTwo.setActive(true);
    userEntityTwo.setCreationDate(new Date());
    userEntityTwo.setLastUpdateDate(new Date());
    userEntityTwo.setChangePasswordKey("");
    userEntityTwo.setChangePasswordExpirationDate(null);
    given(userRepository.findAll()).willReturn(Arrays.asList(userEntity, userEntityTwo));

    hrAdminAdapter.getAllCollaborators();

    verify(userRepository).findAll();
  }

  @Test
  public void should_delete_user() throws Exception {
    hrAdminAdapter.deleteCollaborator(usernameBuilder.from("hello@world.fr"));

    verify(userRepository).delete(any(UserEntity.class));
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userRepository.findByUsername("john@doe.fr")).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    BaseUser collaborator = hrAdminAdapter.findCollaboratorFromUsername(usernameBuilder.from("john@doe.fr"));

    User expected = User.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false));
    assertThat((User) collaborator).isEqualTo(expected);
  }

  @Test
  public void should_get_user_by_username_in_admin_collaborator_object() throws Exception {
    UserEntity userEntity = new UserEntity("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    userEntity.setActive(true);
    userEntity.setCreationDate(new Date());
    userEntity.setLastUpdateDate(new Date());
    userEntity.setChangePasswordKey("");
    userEntity.setChangePasswordExpirationDate(null);
    given(userRepository.findByUsername("admin@toto.fr")).willReturn(userEntity);

    AdminPerson collaborator = hrAdminAdapter.findAdminCollaboratorFromUsername(usernameBuilder.from("admin@toto.fr"));

    AdminCollaborator expectedCollaborator = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    assertThat(collaborator).isEqualTo(expectedCollaborator);
  }

  @Test
  public void should_update_user() throws Exception {
    UserEntity userEntityToUpdate = new UserEntity("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    given(userRepository.findByUsername("admin@toto.fr")).willReturn(userEntityToUpdate);

    AdminCollaborator collaboratorToUpdate = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    hrAdminAdapter.updateCollaborator(collaboratorToUpdate);

    UserEntity updatedUserEntity = new UserEntity("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    updatedUserEntity.setActive(true);
    updatedUserEntity.setCreationDate(new Date());
    updatedUserEntity.setLastUpdateDate(new Date());
    updatedUserEntity.setChangePasswordKey("");
    updatedUserEntity.setChangePasswordExpirationDate(null);
    verify(userRepository).save(updatedUserEntity);
  }

  @Test
  public void should_create_user() throws Exception {
    AdminCollaborator collaborator = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    hrAdminAdapter.createCollaborator(collaborator);

    UserEntity newUserEntity = new UserEntity("admin@toto.fr", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    verify(userRepository).save(newUserEntity);
  }
}
