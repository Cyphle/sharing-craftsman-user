package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.infrastructure.models.User;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApplication.class})
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void should_save_a_new_user() throws Exception {
    userRepository.save(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    User expectedUser = new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    expectedUser.setId(1);
    assertThat(Lists.newArrayList(userRepository.findAll())).containsExactly(
      expectedUser
    );
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    entityManager.persist(new User("john@doe.fr", "password"));
    entityManager.persist(new User("hello@world.fr", "toto"));

    User expectedUser = new User("hello@world.fr", "toto");
    expectedUser.setId(2);
    assertThat(userRepository.findByUsername("hello@world.fr")).isEqualTo(expectedUser);
  }
}
