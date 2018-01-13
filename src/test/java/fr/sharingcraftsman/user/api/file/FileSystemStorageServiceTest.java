package fr.sharingcraftsman.user.api.file;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.config.StorageConfig;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FileSystemStorageServiceTest {
  @Mock
  private StorageConfig properties;
  @Mock
  private Environment environment;
  @Mock
  private AuthorizationVerifierService authorizationVerifierService;

  private StorageService fileSystemStorageService;

  private ClientDTO clientDTO;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() {
    given(properties.getLocation()).willReturn("classpath:files");
    given(properties.getAuthorizedExtensions()).willReturn(Arrays.asList("jpeg", "jpg", "png"));
    String[] activeProfiles = {"dev"};
    given(environment.getActiveProfiles()).willReturn(activeProfiles);
    given(authorizationVerifierService.isUnauthorizedClient(any(ClientDTO.class))).willReturn(false);

    this.fileSystemStorageService = new FileSystemStorageService(properties, environment, authorizationVerifierService);

    clientDTO = ClientDTO.from("secret", "clientsecret");
    tokenDTO = TokenDTO.from("john@doe.fr", "aaa");
  }

  @Test
  public void should_generate_new_file_name() {
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "imgage/jpg", "Spring Framework".getBytes());

    String newName = (String) fileSystemStorageService.store(clientDTO, tokenDTO, multipartFile).getBody();

    assertThat(newName).startsWith("test_");
    assertThat(newName).endsWith(".jpg");
    assertThat(newName).isNotEqualTo("test.jpg");
  }

  @Test
  public void should_throw_exception_when_file_extension_is_not_authorized() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());

    try {
      fileSystemStorageService.store(clientDTO, tokenDTO, multipartFile);
      fail("Should have throw file system storage exception");
    } catch (StorageException e) {
      assertThat(e.getMessage()).isEqualTo("File unauthorized");
    }
  }
}
