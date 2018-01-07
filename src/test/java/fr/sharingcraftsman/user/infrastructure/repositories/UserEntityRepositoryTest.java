package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApplication.class})
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserEntityRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @After
  public void tearDown() throws Exception {
    userRepository.deleteAll();
  }

  @Test
  public void should_save_a_new_user() throws Exception {
    userRepository.save(new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    UserEntity expectedUserEntity = new UserEntity("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    expectedUserEntity.setId(1);
    assertThat(Lists.newArrayList(userRepository.findAll())).containsExactly(
            expectedUserEntity
    );
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    entityManager.persist(new UserEntity("john@doe.fr", "password"));
    entityManager.persist(new UserEntity("hello@world.fr", "toto"));

    UserEntity expectedUserEntity = new UserEntity("hello@world.fr", "toto");
    expectedUserEntity.setId(2);
    assertThat(userRepository.findByUsername("hello@world.fr")).isEqualTo(expectedUserEntity);
  }

  @Test
  public void should_delete_change_password_token_for_user() throws Exception {
    UserEntity userEntity = new UserEntity("hello@world.com", "password");
    userEntity.setChangePasswordKey("aaa");
    userEntity.setChangePasswordExpirationDate(new Date());
    entityManager.persist(userEntity);

    userEntity.setChangePasswordKey("");
    userEntity.setChangePasswordExpirationDate(null);
    userRepository.save(userEntity);
    UserEntity foundUserEntity = userRepository.findByUsername("hello@world.com");

    assertThat(foundUserEntity.getChangePasswordKey()).isEmpty();
    assertThat(foundUserEntity.getChangePasswordExpirationDate()).isNull();
  }
}
