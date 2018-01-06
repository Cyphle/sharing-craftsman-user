package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.infrastructure.models.GroupRole;
import fr.sharingcraftsman.user.infrastructure.repositories.GroupRoleRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RoleManagerAdapterTest {
  private RoleManagerAdapter roleManagerAdapter;
  @Mock
  private GroupRoleRepository groupRoleRepository;
  @Mock
  private RoleRepository roleRepository;

  @Before
  public void setUp() throws Exception {
    roleManagerAdapter = new RoleManagerAdapter(groupRoleRepository, roleRepository);
  }

  @Test
  public void should_get_roles_of_given_group() throws Exception {
    given(groupRoleRepository.findByGroup("USERS")).willReturn(Collections.singletonList(new GroupRole("USERS", "ROLE_USER")));

    List<Role> roles = roleManagerAdapter.getRolesOf("USERS");

    List<Role> expectedRoles = Collections.singletonList(new Role("ROLE_USER"));
    assertThat(roles).isEqualTo(expectedRoles);
  }

  @Test
  public void should_get_all_roles_with_groups() throws Exception {
    List<GroupRole> roles = Arrays.asList(
            new GroupRole("USERS", "ROLE_USER"),
            new GroupRole("ADMINS", "ROLE_ADMIN"),
            new GroupRole("ADMINS", "ROLE_USER")
    );
    given(roleRepository.findAll()).willReturn(roles);

    Set<Group> fetchedRoles = roleManagerAdapter.getAllRolesWithTheirGroups();

    Group users = new Group("USERS");
    users.addRole(new Role("ROLE_USER"));
    Group admins = new Group("ADMINS");
    admins.addRoles(Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
    assertThat(fetchedRoles).containsExactlyInAnyOrder(
            users,
            admins
    );
  }

  @Test
  public void should_create_new_group() throws Exception {
    roleManagerAdapter.createNewGroupsWithRole(Collections.singletonList(new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_ROOT"))))));

    verify(roleRepository).save(new GroupRole("SUPER_ADMIN", "ROLE_ROOT"));
  }
}
