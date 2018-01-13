package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.infrastructure.models.ClientEntity;
import org.junit.Before;
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
public class ClientJpaRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ClientJpaRepository clientJpaRepository;

  @Before
  public void setUp() throws Exception {
    entityManager.persist(ClientEntity.from("client", "secret"));
  }

  @Test
  public void should_get_client_by_name_and_secret() throws Exception {
    ClientEntity client = clientJpaRepository.findByNameAndSecret("client", "secret");

    assertThat(client.getName()).isEqualTo("client");
    assertThat(client.getSecret()).isEqualTo("secret");
  }
}
