package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.api.authentication.LoginDTO;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Api(description = "Endpoints for user actions")
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @ApiOperation(value = "Endpoint to request a change password token", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/request-change-password")
  public ResponseEntity requestChangePasswordToken(@RequestHeader("client") String client,
                                                   @RequestHeader("secret") String secret,
                                                   @RequestHeader("username") String username,
                                                   @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return userService.getChangePasswordToken(clientDTO, tokenDTO);
  }

  @ApiOperation(value = "Endpoint to change password from received token", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/change-password")
  public ResponseEntity changePassword(@RequestHeader("client") String client,
                                       @RequestHeader("secret") String secret,
                                       @RequestHeader("username") String username,
                                       @RequestHeader("access-token") String accessToken,
                                       @RequestBody ChangePasswordDTO changePasswordDTO) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return userService.changePassword(clientDTO, tokenDTO, changePasswordDTO);
  }

  @ApiOperation(value = "Endpoint to request a lost password token to change it", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/lost-password")
  public ResponseEntity requestLostPassword(@RequestHeader("client") String client,
                                            @RequestHeader("secret") String secret,
                                            @RequestHeader("username") String username) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    // TODO Note that this is an unsecured version that sends a change password token as response but should send it in a mail. This is only for a demo to not bother with mail management
    return userService.getLostPasswordToken(clientDTO, username);
  }

  @ApiOperation(value = "Endpoint to change password from lost password token", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/change-lost-password")
  public ResponseEntity changeLostPassword(@RequestHeader("client") String client,
                                       @RequestHeader("secret") String secret,
                                       @RequestHeader("username") String username,
                                       @RequestBody ChangePasswordDTO changePasswordDTO) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username);
    return userService.changeLostPassword(clientDTO, tokenDTO, changePasswordDTO);
  }

  @ApiOperation(value = "Endpoint for a user to register", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized"),
          @ApiResponse(code = 400, message = "Error with the request")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/register")
  public ResponseEntity registerUser(@RequestHeader("client") String client,
                                     @RequestHeader("secret") String secret,
                                     @RequestBody LoginDTO loginDTO) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    return userService.registerUser(clientDTO, loginDTO);
  }

  @ApiOperation(value = "Endpoint for a user to update its profile", response = ProfileDTO.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/update-profile")
  public ResponseEntity updateProfile(@RequestHeader("client") String client,
                                       @RequestHeader("secret") String secret,
                                       @RequestHeader("username") String username,
                                       @RequestHeader("access-token") String accessToken,
                                       @RequestBody ProfileDTO profileDTO) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return userService.updateProfile(clientDTO, tokenDTO, profileDTO);
  }
}
