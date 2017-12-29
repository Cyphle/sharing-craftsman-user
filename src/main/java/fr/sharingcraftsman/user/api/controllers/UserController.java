package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.ChangePasswordDTO;
import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Api(description = "Endpoints to register in the application")
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @ApiOperation(value = "Post information to create a new client", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized"),
          @ApiResponse(code = 400, message = "Error with the request")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/register")
  public ResponseEntity registerUser(@RequestHeader("client") String client,
                                     @RequestHeader("secret") String secret,
                                     @RequestBody LoginDTO loginDTO) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    return userService.registerUser(clientDTO, loginDTO);
  }

  @ApiOperation(value = "Request to change password - Send change password token", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Response containing the token to change password"),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/request-change-password")
  public ResponseEntity requestChangePasswordKey(@RequestHeader("client") String client,
                                                 @RequestHeader("secret") String secret,
                                                 @RequestHeader("username") String username,
                                                 @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return userService.requestChangePassword(clientDTO, tokenDTO);
  }

  @ApiOperation(value = "Change password endpoint", response = ResponseEntity.class)
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
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return userService.changePassword(clientDTO, tokenDTO, changePasswordDTO);
  }
}
