package fr.sharingcraftsman.user.view.controllers;

import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.models.UserLoginType;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @RequestMapping("/user")
  public Principal user(Principal principal) {
    userRepository.deleteAll();
    User user = new User();
    user.setPrincipalId("abc");
    user.setToken("mytoken");
    user.setEmail("john@doe.fr");
    user.setFullName("John Doe");
    user.setLoginType(UserLoginType.GITHUB);
    userRepository.save(user);

    User foundUser = userRepository.findByPrincipalId("abc");
    System.out.println(foundUser);
    System.out.println(principal);
    return principal;
  }
}
