package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.client.AbstractClient;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.infrastructure.models.ClientEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientAdapterTest {
  private ClientAdapter clientAdapter;
  @Mock
  private ClientJpaRepository clientJpaRepository;

  @Before
  public void setUp() throws Exception {
    clientAdapter = new ClientAdapter(clientJpaRepository);
  }

  @Test
  public void should_get_client() throws Exception {
    given(clientJpaRepository.findByNameAndSecret("client", "secret")).willReturn(new ClientEntity("client", "secret"));

    AbstractClient foundClient = clientAdapter.findClient(Client.from("client", "secret"));

    assertThat(foundClient).isEqualTo(Client.from("client", "secret"));
  }

  @Test
  public void should_return_unknown_client_if_client_is_not_known() throws Exception {
    AbstractClient foundClient = clientAdapter.findClient(Client.from("client", "secret"));

    assertThat(foundClient.isKnown()).isFalse();
  }

  @Test
  public void should_create_new_client() throws Exception {
    given(clientJpaRepository.save(any(ClientEntity.class))).willReturn(new ClientEntity("sharingcraftsman", "secret"));
    Client client = Client.from("sharingcraftsman", "secret");

    clientAdapter.createClient(client);

    verify(clientJpaRepository).save(new ClientEntity("sharingcraftsman", "secret"));
  }
}
