package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.ChangePasswordTokenEntity;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.ChangePasswordTokenJpaRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordTokenAdapterTest {
  @Mock
  private ChangePasswordTokenJpaRepository changePasswordTokenJpaRepository;
  @Mock
  private UserJpaRepository userRepository;
  private ChangePasswordTokenAdapter changePasswordTokenAdapter;

  @Before
  public void setUp() throws Exception {
    changePasswordTokenAdapter = new ChangePasswordTokenAdapter(userRepository, changePasswordTokenJpaRepository);
  }

  @Test
  public void should_delete_user_change_password_token() throws Exception {
    given(changePasswordTokenJpaRepository.findByUsername("john@doe.fr")).willReturn(new ChangePasswordTokenEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw==", new Date()));

    changePasswordTokenAdapter.deleteChangePasswordTokenOf(Username.from("john@doe.fr"));

    ChangePasswordTokenEntity token = new ChangePasswordTokenEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw==", new Date());
    verify(changePasswordTokenJpaRepository).delete(token);
  }

  @Test
  public void should_create_change_password_token_with_change_password_token() throws Exception {
    given(userRepository.findByUsername(any(String.class))).willReturn(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));
    given(changePasswordTokenJpaRepository.save(any(ChangePasswordTokenEntity.class))).willReturn(new ChangePasswordTokenEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw==", new Date()));

    ChangePasswordToken changePasswordToken = ChangePasswordToken.from(
            User.from("john@doe.fr", "aaa"),
            "aaa",
            LocalDateTime.of(2017, 12, 25, 12, 0)
    );
    changePasswordTokenAdapter.createChangePasswordTokenFor(changePasswordToken);

    ChangePasswordTokenEntity token = new ChangePasswordTokenEntity("john@doe.fr", "aaa", new Date());
    verify(changePasswordTokenJpaRepository).save(token);
  }
}
