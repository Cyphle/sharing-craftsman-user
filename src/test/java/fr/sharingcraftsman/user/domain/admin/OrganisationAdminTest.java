package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.UnknownCollaborator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrganisationAdminTest {
  @Mock
  private HRAdminManager hrAdminManager;

  private OrganisationAdmin organisation;

  @Before
  public void setUp() throws Exception {
    organisation = new OrganisationAdmin(hrAdminManager);
  }

  @Test
  public void should_get_all_collaborators() throws Exception {
    organisation.getAllCollaborators();

    verify(hrAdminManager).getAllCollaborators();
  }

  @Test
  public void should_delete_collaborator_if_exists() throws Exception {
    organisation.deleteCollaborator(usernameBuilder.from("hello@world.fr"));

    verify(hrAdminManager).findCollaboratorFromUsername(usernameBuilder.from("hello@world.fr"));
    verify(hrAdminManager).deleteCollaborator(usernameBuilder.from("hello@world.fr"));
  }

  @Test
  public void should_throw_unknown_collaborator_exception_if_collaborator_not_found() throws Exception {
    try {
      given(hrAdminManager.findCollaboratorFromUsername(any(Username.class))).willReturn(new UnknownCollaborator());
      organisation.deleteCollaborator(usernameBuilder.from("hello@world.fr"));
      fail("Should have throw collaborator exception when not found");
    } catch (CollaboratorException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown collaborator");
    }
  }

  @Test
  public void should_update_collaborator() throws Exception {
    AdminCollaborator foundCollaborator = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    given(hrAdminManager.findAdminCollaboratorFromUsername(foundCollaborator.getUsername())).willReturn(foundCollaborator);

    AdminCollaborator collaboratorToUpdate = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    organisation.updateCollaborator(collaboratorToUpdate);

    AdminCollaborator expectedCollaborator = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    verify(hrAdminManager).findAdminCollaboratorFromUsername(expectedCollaborator.getUsername());
    verify(hrAdminManager).updateCollaborator(expectedCollaborator);
  }
}
