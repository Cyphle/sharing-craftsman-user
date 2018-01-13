package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.infrastructure.models.AuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.AuthorizationJpaRepository;
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
  private AuthorizationJpaRepository authorizationJpaRepository;

  @Before
  public void setUp() throws Exception {
    roleManagerAdapter = new RoleManagerAdapter(authorizationJpaRepository);
  }

  @Test
  public void should_get_roles_of_given_group() throws Exception {
    given(authorizationJpaRepository.findByGroup("USERS")).willReturn(Collections.singletonList(AuthorizationEntity.from("USERS", "ROLE_USER")));

    List<Role> roles = roleManagerAdapter.getRolesOf("USERS");

    List<Role> expectedRoles = Collections.singletonList(Role.from("ROLE_USER"));
    assertThat(roles).isEqualTo(expectedRoles);
  }

  @Test
  public void should_get_all_roles_with_groups() throws Exception {
    List<AuthorizationEntity> roles = Arrays.asList(
            AuthorizationEntity.from("USERS", "ROLE_USER"),
            AuthorizationEntity.from("ADMINS", "ROLE_ADMIN"),
            AuthorizationEntity.from("ADMINS", "ROLE_USER")
    );
    given(authorizationJpaRepository.findAll()).willReturn(roles);

    Set<Group> fetchedRoles = roleManagerAdapter.getAllRolesWithTheirGroups();

    Group users = Group.from("USERS");
    users.addRole(Role.from("ROLE_USER"));
    Group admins = Group.from("ADMINS");
    admins.addRoles(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    assertThat(fetchedRoles).containsExactlyInAnyOrder(
            users,
            admins
    );
  }

  @Test
  public void should_create_new_group() throws Exception {
    roleManagerAdapter.createNewGroupsWithRole(Collections.singletonList(Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ROOT"))))));

    verify(authorizationJpaRepository).save(AuthorizationEntity.from("SUPER_ADMIN", "ROLE_ROOT"));
  }

  @Test
  public void should_delete_group() throws Exception {
    given(authorizationJpaRepository.findFromGroupNameAndRole("SUPER_ADMIN", "ROLE_ROOT")).willReturn(AuthorizationEntity.from("SUPER_ADMIN", "ROLE_ROOT"));

    roleManagerAdapter.removeRoleFromGroup(Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ROOT")))));

    verify(authorizationJpaRepository).delete(AuthorizationEntity.from("SUPER_ADMIN", "ROLE_ROOT"));
  }
}
