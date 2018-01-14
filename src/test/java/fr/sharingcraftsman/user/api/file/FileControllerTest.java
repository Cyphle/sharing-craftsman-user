package fr.sharingcraftsman.user.api.file;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes={UserApplication.class})
@TestPropertySource(locations = {"classpath:application-test.properties"})
public class FileControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private StorageService storageService;

  @Autowired
  private WebApplicationContext context;

  private ClientDTO clientDTO;
  private TokenDTO tokenDTO;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    clientDTO = ClientDTO.from("secret", "clientsecret");
    tokenDTO = TokenDTO.from("john@doe.fr", "aaa");
  }

  @Test
  public void should_save_uploaded_file() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());

    this.mvc.perform(fileUpload("/upload").file(multipartFile)
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk())
            .andReturn();
  }

  @Test
  public void should404WhenMissingFile() throws Exception {
    given(this.storageService.loadAsResource(clientDTO, tokenDTO, "test.txt")).willReturn(
            ResponseEntity
            .notFound()
            .build()
    );

    this.mvc.perform(get("/storage/files/test.txt")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isNotFound());
  }
}
