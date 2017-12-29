package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.models.OAuthClient;
import fr.sharingcraftsman.user.api.models.OAuthToken;
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
                                     @RequestBody Login login) {
    OAuthClient oAuthClient = new OAuthClient(client, secret);
    return userService.registerUser(oAuthClient, login);
  }

  @ApiOperation(value = "Request to change password - Send change password token", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Response containing the token to change password"),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/request-change-password")
  public ResponseEntity verify(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    OAuthClient oAuthClient = new OAuthClient(client, secret);
    OAuthToken oAuthToken = new OAuthToken(username, accessToken);
    return userService.requestChangePassword(oAuthClient, oAuthToken);
  }

  /*
  Change password:
  - Post request with token for change password request -> return change password token (in user table) with validity date
  - Post request with change password token and oauth token and old and new password, invalidate token -> return OK
   */
}
