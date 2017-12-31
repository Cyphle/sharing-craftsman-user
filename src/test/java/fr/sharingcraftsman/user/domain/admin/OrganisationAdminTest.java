package fr.sharingcraftsman.user.domain.admin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
}
