package fr.sharingcraftsman.user.domain.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ClientAdministratorTest {
  private ClientAdministrator clientAdministrator;
  @Mock
  private ClientStock clientStock;

  @Before
  public void setUp() throws Exception {
    clientAdministrator = new ClientAdministrator(clientStock);
  }

  @Test
  public void should_validate_client() throws Exception {
    given(clientStock.findClient(any(Client.class))).willReturn(Client.knownClient("client", "clientsecret"));

    Client client = Client.from("client", "clientsecret");

    assertThat(clientAdministrator.clientExists(client)).isTrue();
  }

  @Test
  public void should_return_false_if_client_does_not_exists() throws Exception {
    given(clientStock.findClient(any(Client.class))).willReturn(Client.unkownClient());

    Client client = Client.from("client", "clientsecret");

    assertThat(clientAdministrator.clientExists(client)).isFalse();
  }
}
