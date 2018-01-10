package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.exceptions.UnknownBaseUserForAdminCollaborator;
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
  public void should_get_all_collaborators() throws Exception {
    organisation.getAllCollaborators();

    verify(userForAdminRepository).getAllCollaborators();
  }

  @Test
  public void should_delete_collaborator_if_exists() throws Exception {
    given(userForAdminRepository.findCollaboratorFromUsername(Username.from("hello@world.fr"))).willReturn(User.from("hello@world.fr", "password"));

    organisation.deleteCollaborator(Username.from("hello@world.fr"));

    verify(userForAdminRepository).findCollaboratorFromUsername(Username.from("hello@world.fr"));
    verify(userForAdminRepository).deleteCollaborator(Username.from("hello@world.fr"));
  }

  @Test
  public void should_throw_unknown_collaborator_exception_if_collaborator_not_found() throws Exception {
    try {
      given(userForAdminRepository.findCollaboratorFromUsername(any(Username.class))).willReturn(new UnknownUser());
      organisation.deleteCollaborator(Username.from("hello@world.fr"));
      fail("Should have throw collaborator exception when not found");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown user");
    }
  }

  @Test
  public void should_update_collaborator() throws Exception {
    UserForAdmin foundCollaborator = UserForAdmin.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    given(userForAdminRepository.findAdminCollaboratorFromUsername(foundCollaborator.getUsername())).willReturn(foundCollaborator);

    UserForAdmin collaboratorToUpdate = UserForAdmin.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    organisation.updateCollaborator(collaboratorToUpdate);

    UserForAdmin expectedCollaborator = UserForAdmin.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    verify(userForAdminRepository).findAdminCollaboratorFromUsername(expectedCollaborator.getUsername());
    verify(userForAdminRepository).updateCollaborator(expectedCollaborator);
  }

  @Test
  public void should_create_collaborator() throws Exception {
    given(userForAdminRepository.findAdminCollaboratorFromUsername(any(Username.class))).willReturn(new UnknownBaseUserForAdminCollaborator());

    UserForAdmin collaboratorToCreate = UserForAdmin.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    organisation.createCollaborator(collaboratorToCreate);

    UserForAdmin expectedCollaborator = UserForAdmin.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    verify(userForAdminRepository).findAdminCollaboratorFromUsername(expectedCollaborator.getUsername());
    verify(userForAdminRepository).createCollaborator(expectedCollaborator);
  }
}
