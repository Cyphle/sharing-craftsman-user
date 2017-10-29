package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.models.UserLoginType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @Before
  public void setUp() throws Exception {
    User user = new User();
    user.setId(1);
    user.setPrincipalId("abcd");
    user.setToken("abc");
    user.setEmail("john@doe.fr");
    user.setFullName("John Doe");
    user.setLoginType(UserLoginType.GITHUB);

    this.userRepository.save(user);
  }

  @After
  public void tearDown() throws Exception {
    userRepository.deleteAll();
  }

  @Test
  public void should_find_user_by_principal_id() {
    // WHEN
    User user = userRepository.findByPrincipalId("abcd");
    // THEN
    assertThat(user.getEmail()).isEqualTo("john@doe.fr");
    assertThat(user.getFullName()).isEqualTo("John Doe");
  }

//  @Test
//  public void should_find_all_users_by_slug_like() {
//    // WHEN
//    List<SUser> users = this.repository.findAllBySlugLike("fake-user");
//    // THEN
//    assertThat(users.size()).isEqualTo(2);
//  }
}
