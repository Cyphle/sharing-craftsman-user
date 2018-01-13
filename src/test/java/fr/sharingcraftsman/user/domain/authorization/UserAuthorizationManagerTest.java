package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthorizationManagerTest {
  private UserAuthorizationManager userAuthorizationManager;
  private Username username;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;

  @Before
  public void setUp() throws Exception {
    userAuthorizationManager = new UserAuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);

    username = Username.from("john@doe.fr");
  }

  @Test
  public void should_get_authorizations_of_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));

    Authorization authorization = userAuthorizationManager.getAuthorizationsOf(username);

    Role role = Role.from("ROLE_USER");
    Group group = Group.from("USERS");
    group.addRole(role);
    Authorization expectedAuthorization = Authorization.get();
    expectedAuthorization.addGroup(group);
    assertThat(authorization).isEqualTo(expectedAuthorization);
  }

  @Test
  public void should_add_given_group_to_user() throws Exception {
    userAuthorizationManager.addGroupToUser(username, Groups.USERS);

    verify(userAuthorizationRepository).addGroupToUser(username, Groups.USERS);
  }

  @Test
  public void should_not_add_group_if_user_already_has_the_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("USERS")));

    userAuthorizationManager.addGroupToUser(username, Groups.USERS);

    verify(userAuthorizationRepository, never()).addGroupToUser(any(Username.class), any(Groups.class));
  }

  @Test
  public void should_not_remove_group_when_user_does_not_have_it() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("ÆDMINS")));

    userAuthorizationManager.removeGroupFromUser(username, Groups.USERS);

    verify(userAuthorizationRepository, never()).removeGroupFromUser(any(Username.class), any(Groups.class));
  }

  @Test
  public void should_remove_group_when_user_has_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("USERS")));

    userAuthorizationManager.removeGroupFromUser(username, Groups.USERS);

    verify(userAuthorizationRepository).removeGroupFromUser(any(Username.class), any(Groups.class));
  }
}
