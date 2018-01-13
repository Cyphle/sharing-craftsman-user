package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApplication.class})
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccessAbstractTokenJpaRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AccessTokenJpaRepository accessTokenJpaRepository;

  @Before
  public void setUp() throws Exception {
    AccessTokenEntity accessTokenEntity = AccessTokenEntity.from("client", "john@doe.fr", "aaa", "bbb", DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0)));
    entityManager.persist(accessTokenEntity);
  }

  @Test
  public void should_delete_tokens_of_user() throws Exception {
    accessTokenJpaRepository.deleteByUsername("john@doe.fr", "client");

    assertThat(accessTokenJpaRepository.findAll()).isEmpty();
  }

  @Test
  public void should_find_token_by_username_client_and_access_token() throws Exception {
    AccessTokenEntity foundToken = accessTokenJpaRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa");

    assertThat(foundToken.getAccessToken()).isEqualTo("aaa");
  }

  @Test
  public void should_not_find_token_by_username_client_and_access_token() throws Exception {
    AccessTokenEntity foundToken = accessTokenJpaRepository.findByUsernameClientAndAccessToken("hello@world.com", "client", "aaa");

    assertThat(foundToken).isNull();
  }

  @Test
  public void should_find_token_by_username_client_and_refresh_token() throws Exception {
    AccessTokenEntity foundToken = accessTokenJpaRepository.findByUsernameClientAndRefreshToken("john@doe.fr", "client", "bbb");

    assertThat(foundToken.getAccessToken()).isEqualTo("aaa");
  }

  @Test
  public void should_not_find_token_by_username_client_and_refresh_token() throws Exception {
    AccessTokenEntity foundToken = accessTokenJpaRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "ccc");

    assertThat(foundToken).isNull();
  }
}
