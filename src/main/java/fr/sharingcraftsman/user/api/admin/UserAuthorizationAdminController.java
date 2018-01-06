package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.GroupDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Api(description = "Endpoints to for admin")
public class UserAuthorizationAdminController {
  private AdminService adminService;

  @Autowired
  public UserAuthorizationAdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @ApiOperation(value = "Endpoint to get groups", response = GroupDTO.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/groups/add")
  public ResponseEntity addGroup(@RequestHeader("client") String client,
                                 @RequestHeader("secret") String secret,
                                 @RequestHeader("username") String username,
                                 @RequestHeader("access-token") String accessToken,
                                 @RequestBody UserGroupDTO userGroupDTO) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.addGroupToUser(clientDTO, tokenDTO, userGroupDTO);
  }

  @ApiOperation(value = "Endpoint to get groups", response = GroupDTO.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/groups/remove")
  public ResponseEntity removeGroup(@RequestHeader("client") String client,
                                    @RequestHeader("secret") String secret,
                                    @RequestHeader("username") String username,
                                    @RequestHeader("access-token") String accessToken,
                                    @RequestBody UserGroupDTO userGroupDTO) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.removeGroupToUser(clientDTO, tokenDTO, userGroupDTO);
  }
}
