package fr.sharingcraftsman.user.command.repositories;


import fr.sharingcraftsman.user.command.common.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  @Query("Select u from User u where u.username = ?1 and u.isActive = true")
  User findByUsername(String username);
}
