package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.infrastructure.models.UserGroup;
import fr.sharingcraftsman.user.infrastructure.repositories.UserGroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GroupManagerAdapterTest {
  private GroupManagerAdapter groupManagerAdapter;
  @Mock
  private UserGroupRepository userGroupRepository;

  @Before
  public void setUp() throws Exception {
    groupManagerAdapter = new GroupManagerAdapter(userGroupRepository);
  }

  @Test
  public void should_get_groups_of_user_by_username() throws Exception {
    given(userGroupRepository.findByUsername("john@doe.fr")).willReturn(Collections.singletonList(new UserGroup("john@doe.fr", "USERS")));

    List<Group> groups = groupManagerAdapter.findGroupsOf(usernameBuilder.from("john@doe.fr"));

    assertThat(groups).containsExactly(new Group("USERS"));
  }

  @Test
  public void should_add_group_to_user() throws Exception {
    given(userGroupRepository.save(any(UserGroup.class))).willReturn(new UserGroup("john@doe.fr", "USERS"));

    groupManagerAdapter.addGroupToCollaborator(usernameBuilder.from("john@doe.fr"), Groups.USERS);

    verify(userGroupRepository).save(new UserGroup("john@doe.fr", "USERS"));
  }

  @Test
  public void should_remove_group_from_user() throws Exception {
    given(userGroupRepository.findByUsernameAndGroup("hello@world.fr", Groups.USERS.name())).willReturn(new UserGroup("hello@world.fr", Groups.USERS.name()));

    groupManagerAdapter.removeGroupFromCollaborator(usernameBuilder.from("hello@world.fr"), Groups.USERS);

    verify(userGroupRepository).delete(new UserGroup("hello@world.fr", "USERS"));
  }
}
