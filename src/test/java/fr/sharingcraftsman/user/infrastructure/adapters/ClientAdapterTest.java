package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.infrastructure.models.OAuthClient;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
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
  private ClientRepository clientRepository;

  @Before
  public void setUp() throws Exception {
    clientAdapter = new ClientAdapter(clientRepository);
  }

  @Test
  public void should_get_client() throws Exception {
    given(clientRepository.findByNameAndSecret("client", "secret")).willReturn(new OAuthClient("client", "secret"));

    Client foundClient = clientAdapter.findClient(Client.from("client", "secret"));

    assertThat(foundClient).isEqualTo(Client.knownClient("client", "secret"));
  }

  @Test
  public void should_return_unknown_client_if_client_is_not_known() throws Exception {
    Client foundClient = clientAdapter.findClient(Client.from("client", "secret"));

    assertThat(foundClient.isKnown()).isFalse();
  }

  @Test
  public void should_create_new_client() throws Exception {
    given(clientRepository.save(any(OAuthClient.class))).willReturn(new OAuthClient("sharingcraftsman", "secret"));
    Client client = Client.from("sharingcraftsman", "secret");

    clientAdapter.createClient(client);

    verify(clientRepository).save(new OAuthClient("sharingcraftsman", "secret"));
  }
}