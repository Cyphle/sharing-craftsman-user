package fr.sharingcraftsman.user.api.authentication;

import fr.sharingcraftsman.user.api.client.ClientDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@Api(description = "Endpoints to manage authentication of users")
public class AuthenticationController {
  private AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @ApiOperation(value = "Endpoint to check token validity", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/verify")
  public ResponseEntity verifyToken(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return authenticationService.checkToken(clientDTO, tokenDTO);
  }

  @ApiOperation(value = "Endpoint to log out user", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/logout")
  public ResponseEntity logOut(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return authenticationService.logout(clientDTO, tokenDTO);
  }

  @ApiOperation(value = "Endpoint to get a new access token from refresh token", response = TokenDTO.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/refresh-token")
  public ResponseEntity refreshToken(@RequestHeader("client") String client,
                                     @RequestHeader("secret") String secret,
                                     @RequestHeader("username") String username,
                                     @RequestHeader("refresh-token") String refreshToken) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, "", refreshToken);
    return authenticationService.refreshToken(clientDTO, tokenDTO);
  }

  @ApiOperation(value = "Endpoint to log in user", response = TokenDTO.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public ResponseEntity logIn(@RequestHeader("client") String client,
                              @RequestHeader("secret") String secret,
                              @RequestBody LoginDTO loginDTO) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    return authenticationService.login(clientDTO, loginDTO);
  }
}
