package fr.sharingcraftsman.user.view.controllers;

import fr.sharingcraftsman.user.config.OtherConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
  @Autowired
  private OtherConfig otherConfig;

  @RequestMapping("/user")
  public Principal user(Principal principal) {
    System.out.println(principal);
    System.out.println(otherConfig.myBean());
    return principal;
  }
}
