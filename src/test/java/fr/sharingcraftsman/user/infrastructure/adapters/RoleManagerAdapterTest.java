package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.infrastructure.models.GroupRole;
import fr.sharingcraftsman.user.infrastructure.repositories.GroupRoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RoleManagerAdapterTest {
  private RoleManagerAdapter roleManagerAdapter;
  @Mock
  private GroupRoleRepository groupRoleRepository;

  @Before
  public void setUp() throws Exception {
    roleManagerAdapter = new RoleManagerAdapter(groupRoleRepository);
  }

  @Test
  public void should_get_roles_of_given_group() throws Exception {
    given(groupRoleRepository.findByGroup("USERS")).willReturn(Collections.singletonList(new GroupRole("USERS", "ROLE_USER")));

    List<Role> roles = roleManagerAdapter.getRolesOf("USERS");

    List<Role> expectedRoles = Collections.singletonList(new Role("ROLE_USER"));
    assertThat(roles).isEqualTo(expectedRoles);
  }
}
