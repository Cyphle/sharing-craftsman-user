package fr.sharingcraftsman.user.domain.client;

import fr.sharingcraftsman.user.domain.utils.SecretGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientAdministratorTest {
  private ClientAdministrator clientAdministrator;
  @Mock
  private ClientStock clientStock;
  @Mock
  private SecretGenerator secretGenerator;

  @Before
  public void setUp() throws Exception {
    clientAdministrator = new ClientAdministrator(clientStock, secretGenerator);
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

  @Test
  public void should_create_new_client() throws Exception {
    given(secretGenerator.generateSecret()).willReturn("secret");
    given(clientStock.findClientByName(any(Client.class))).willReturn(Client.unkownClient());
    Client client = Client.from("sharingcraftsman", "");

    clientAdministrator.createNewClient(client);

    verify(clientStock).createClient(Client.from("sharingcraftsman", "secret"));
  }

  @Test
  public void should_throw_already_existing_client_if_client_already_exists() throws Exception {
    given(secretGenerator.generateSecret()).willReturn("secret");
    given(clientStock.findClientByName(any(Client.class))).willReturn(Client.knownClient("sharingcraftsman", "secret"));
    Client client = Client.from("sharingcraftsman", "");

    try {
      clientAdministrator.createNewClient(client);
      fail("Should throw already existing client exception");
    } catch (ClientException e) {
      assertThat(e.getMessage()).isEqualTo("Already existing client");
    }
  }
}
