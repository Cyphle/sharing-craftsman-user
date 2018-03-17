package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Api(description = "Endpoints to manage users authorizations")
public class UserAuthorizationAdminController {
  private UserAuthorizationAdminService userAuthorizationAdminService;

  @Autowired
  public UserAuthorizationAdminController(UserAuthorizationAdminService userAuthorizationAdminService) {
    this.userAuthorizationAdminService = userAuthorizationAdminService;
  }

  @ApiOperation(value = "Endpoint to add group to user", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/groups")
  public ResponseEntity addGroupToUser(@RequestHeader("client") String client,
                                 @RequestHeader("secret") String secret,
                                 @RequestHeader("username") String username,
                                 @RequestHeader("access-token") String accessToken,
                                 @RequestBody UserGroupDTO userGroupDTO) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return userAuthorizationAdminService.addGroupToUser(clientDTO, tokenDTO, userGroupDTO);
  }

  @ApiOperation(value = "Endpoint to remove a group from a user", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/groups/delete")
  public ResponseEntity removeGroup(@RequestHeader("client") String client,
                                    @RequestHeader("secret") String secret,
                                    @RequestHeader("username") String username,
                                    @RequestHeader("access-token") String accessToken,
                                    @RequestBody UserGroupDTO userGroupDTO) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return userAuthorizationAdminService.removeGroupToUser(clientDTO, tokenDTO, userGroupDTO);
  }
}
