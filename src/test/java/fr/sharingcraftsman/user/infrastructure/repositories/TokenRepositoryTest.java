package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.infrastructure.models.OAuthToken;
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
public class TokenRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TokenRepository tokenRepository;

  @Before
  public void setUp() throws Exception {
    OAuthToken oAuthToken = new OAuthToken();
    oAuthToken.setClient("client");
    oAuthToken.setUsername("john@doe.fr");
    oAuthToken.setAccessToken("aaa");
    oAuthToken.setRefreshToken("bbb");
    oAuthToken.setExpirationDate(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));

    entityManager.persist(oAuthToken);
  }

  @Test
  public void should_delete_tokens_of_user() throws Exception {
    tokenRepository.deleteByUsername("john@doe.fr", "client");

    assertThat(tokenRepository.findAll()).isEmpty();
  }
}
