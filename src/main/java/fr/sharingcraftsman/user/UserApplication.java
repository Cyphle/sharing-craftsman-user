package fr.sharingcraftsman.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApplication {
  public static void main(String[] args) {
    System.out.println("MY USER APPLICATION IS STARTING");
    SpringApplication.run(UserApplication.class, args);
  }
}
