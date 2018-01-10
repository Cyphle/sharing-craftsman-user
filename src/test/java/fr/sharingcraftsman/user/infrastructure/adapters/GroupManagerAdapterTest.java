package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.infrastructure.models.UserAuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserAuthorizationJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GroupManagerAdapterTest {
  private GroupManagerAdapter groupManagerAdapter;
  @Mock
  private UserAuthorizationJpaRepository userAuthorizationJpaRepository;

  @Before
  public void setUp() throws Exception {
    groupManagerAdapter = new GroupManagerAdapter(userAuthorizationJpaRepository);
  }

  @Test
  public void should_get_groups_of_user_by_username() throws Exception {
    given(userAuthorizationJpaRepository.findByUsername("john@doe.fr")).willReturn(Collections.singletonList(new UserAuthorizationEntity("john@doe.fr", "USERS")));

    List<Group> groups = groupManagerAdapter.findGroupsOf(Username.from("john@doe.fr"));

    assertThat(groups).containsExactly(Group.from("USERS"));
  }

  @Test
  public void should_add_group_to_user() throws Exception {
    given(userAuthorizationJpaRepository.save(any(UserAuthorizationEntity.class))).willReturn(new UserAuthorizationEntity("john@doe.fr", "USERS"));

    groupManagerAdapter.addGroupToUser(Username.from("john@doe.fr"), Groups.USERS);

    verify(userAuthorizationJpaRepository).save(new UserAuthorizationEntity("john@doe.fr", "USERS"));
  }

  @Test
  public void should_remove_group_from_user() throws Exception {
    given(userAuthorizationJpaRepository.findByUsernameAndGroup("hello@world.fr", Groups.USERS.name())).willReturn(new UserAuthorizationEntity("hello@world.fr", Groups.USERS.name()));

    groupManagerAdapter.removeGroupFromUser(Username.from("hello@world.fr"), Groups.USERS);

    verify(userAuthorizationJpaRepository).delete(new UserAuthorizationEntity("hello@world.fr", "USERS"));
  }
}
