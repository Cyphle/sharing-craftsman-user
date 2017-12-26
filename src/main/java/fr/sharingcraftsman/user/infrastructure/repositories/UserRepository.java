package fr.sharingcraftsman.user.infrastructure.repositories;


import fr.sharingcraftsman.user.infrastructure.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  @Query("Select u from User u where u.username = ?1 and u.isActive = true")
  User findByUsername(String username);
  @Query("Select u from User u where u.username = ?1 and u.password = ?2 and u.isActive = true")
  User findByUsernameAndPassword(String username, String password);
}
