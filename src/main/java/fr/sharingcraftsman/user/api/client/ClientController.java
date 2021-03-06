package fr.sharingcraftsman.user.api.client;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
@Api(description = "Endpoints to manage clients of application")
public class ClientController {
  private ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @ApiOperation(value = "Endpoint to create a new client", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/register")
  public ResponseEntity registerUser(@RequestBody ClientDTO ClientDTO) {
    if (ClientDTO.getName().equals("sharingcraftsman"))
      return clientService.register(ClientDTO);
    else
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
  }
}
