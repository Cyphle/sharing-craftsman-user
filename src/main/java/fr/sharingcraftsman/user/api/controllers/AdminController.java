package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.admin.UserGroupDTO;
import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.api.services.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Api(description = "Endpoints to for admin")
public class AdminController {
  private AdminService adminService;

  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @ApiOperation(value = "Endpoint to get users", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/users")
  public ResponseEntity verify(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.getUsers(clientDTO, tokenDTO);
  }

  @ApiOperation(value = "Endpoint to delete a user", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.DELETE, value = "/users/{usernameToDelete}")
  public ResponseEntity verify(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken,
                               @PathVariable String usernameToDelete) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.deleteUser(clientDTO, tokenDTO, usernameToDelete);
  }

  @ApiOperation(value = "Endpoint to update informations of user", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.PUT, value = "/users")
  public ResponseEntity updateUser(@RequestHeader("client") String client,
                                      @RequestHeader("secret") String secret,
                                      @RequestHeader("username") String username,
                                      @RequestHeader("access-token") String accessToken,
                                      @RequestBody AdminUserDTO user) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.updateUser(clientDTO, tokenDTO, user);
  }

  @ApiOperation(value = "Endpoint to add user", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/users")
  public ResponseEntity addUser(@RequestHeader("client") String client,
                                   @RequestHeader("secret") String secret,
                                   @RequestHeader("username") String username,
                                   @RequestHeader("access-token") String accessToken,
                                   @RequestBody AdminUserDTO user) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.addUser(clientDTO, tokenDTO, user);
  }

  @ApiOperation(value = "Endpoint to get groups", response = GroupDTO.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/roles/groups")
  public ResponseEntity getGroups(@RequestHeader("client") String client,
                                @RequestHeader("secret") String secret,
                                @RequestHeader("username") String username,
                                @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.getGroups(clientDTO, tokenDTO);
  }

  @ApiOperation(value = "Endpoint to get groups", response = GroupDTO.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/roles/groups/add")
  public ResponseEntity addGroup(@RequestHeader("client") String client,
                                  @RequestHeader("secret") String secret,
                                  @RequestHeader("username") String username,
                                  @RequestHeader("access-token") String accessToken,
                                  @RequestBody UserGroupDTO userGroupDTO) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.addGroupToUser(clientDTO, tokenDTO, userGroupDTO);
  }
  /*
  - Get list of users with profiles -> OK
  - Remove user -> OK
  - Deactivate user + Modify user (send all user except authorizations) -> OK
  - Modify user ? -> OK
  - Add user -> OK
  - Get list of roles, groups -> OK
  - Add/remove roles groups (impact on user groups)
  - Get list of authorizations
  - Add/remove authorizations
  - Get list of tokens
  - Remove tokens
   */
}
