package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationEntityAuthorizationManagerTest {
  private AuthorizationManager authorizationManager;
  private Credentials credentials;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;

  @Before
  public void setUp() throws Exception {
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);

    credentials = Credentials.build("john@doe.fr", "NOPASSWORD");
  }

  @Test
  public void should_get_authorizations_of_collaborator() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(credentials.getUsername())).willReturn(Collections.singletonList(new Group("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(new Role("ROLE_USER")));

    Authorization authorization = authorizationManager.getAuthorizationsOf(credentials);

    Role role = new Role("ROLE_USER");
    Group group = new Group("USERS");
    group.addRole(role);
    Authorization expectedAuthorization = new Authorization();
    expectedAuthorization.addGroup(group);
    assertThat(authorization).isEqualTo(expectedAuthorization);
  }

  @Test
  public void should_add_given_group_to_collaborator() throws Exception {
    authorizationManager.addGroup(credentials, Groups.USERS);

    verify(userAuthorizationRepository).addGroupToCollaborator(credentials.getUsername(), Groups.USERS);
  }

  @Test
  public void should_not_add_group_if_collaborator_already_has_the_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(credentials.getUsername())).willReturn(Collections.singletonList(new Group("USERS")));

    authorizationManager.addGroup(credentials, Groups.USERS);

    verify(userAuthorizationRepository, never()).addGroupToCollaborator(any(Username.class), any(Groups.class));
  }

  @Test
  public void should_get_all_groups_and_roles() throws Exception {
    authorizationManager.getAllRolesWithTheirGroups();

    verify(authorizationRepository).getAllRolesWithTheirGroups();
  }

  @Test
  public void should_not_remove_group_when_collaborator_does_not_have_it() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(credentials.getUsername())).willReturn(Collections.singletonList(new Group("ÆDMINS")));

    authorizationManager.removeGroup(credentials, Groups.USERS);

    verify(userAuthorizationRepository, never()).removeGroupFromCollaborator(any(Username.class), any(Groups.class));
  }

  @Test
  public void should_remove_group_when_collaborator_has_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(credentials.getUsername())).willReturn(Collections.singletonList(new Group("USERS")));

    authorizationManager.removeGroup(credentials, Groups.USERS);

    verify(userAuthorizationRepository).removeGroupFromCollaborator(any(Username.class), any(Groups.class));
  }

  @Test
  public void should_create_new_group() throws Exception {
    Group groupWithRoles = new Group("SUPER_ADMIN", new HashSet<>(Arrays.asList(new Role("ROLE_ROOT"), new Role("ROLE_ADMIN"), new Role("ROLE_USER"))));

    authorizationManager.createNewGroupWithRoles(groupWithRoles);

    Group groupRoot = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_ROOT"))));
    Group groupAdmin = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_ADMIN"))));
    Group groupUser = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_USER"))));
    verify(authorizationRepository).createNewGroupsWithRole(Arrays.asList(groupRoot, groupAdmin, groupUser));
  }

  @Test
  public void should_not_create_group_if_already_exists() throws Exception {
    Group superAdminUser = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_USER"))));
    given(authorizationRepository.getAllRolesWithTheirGroups()).willReturn(new HashSet<>(Collections.singletonList(superAdminUser)));
    Group groupWithRoles = new Group("SUPER_ADMIN", new HashSet<>(Arrays.asList(new Role("ROLE_ROOT"), new Role("ROLE_ADMIN"), new Role("ROLE_USER"))));

    authorizationManager.createNewGroupWithRoles(groupWithRoles);

    Group groupRoot = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_ROOT"))));
    Group groupAdmin = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_ADMIN"))));
    verify(authorizationRepository).createNewGroupsWithRole(Arrays.asList(groupRoot, groupAdmin));
  }

  @Test
  public void should_remove_role_from_group() throws Exception {
    Group groupWithRoles = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_USER"))));

    authorizationManager.removeRoleFromGroup(groupWithRoles);

    Group groupUser = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_USER"))));
    verify(authorizationRepository).removeRoleFromGroup(groupUser);
  }

  @Test
  public void should_remove_first_role_given_from_group() throws Exception {
    Group groupWithRoles = new Group("SUPER_ADMIN", new HashSet<>(Arrays.asList(new Role("ROLE_ADMIN"), new Role("ROLE_USER"))));

    authorizationManager.removeRoleFromGroup(groupWithRoles);

    Group groupUser = new Group("SUPER_ADMIN", new HashSet<>(Collections.singletonList(new Role("ROLE_ADMIN"))));
    verify(authorizationRepository).removeRoleFromGroup(groupUser);
  }
}
