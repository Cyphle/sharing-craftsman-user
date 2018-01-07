package fr.sharingcraftsman.user.infrastructure.repositories;


import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserJpaRepository extends CrudRepository<UserEntity, Long> {
  @Query("Select u from UserEntity u where u.username = ?1 and u.isActive = true")
  UserEntity findByUsername(String username);
  @Query("Select u from UserEntity u where u.username = ?1 and u.password = ?2 and u.isActive = true")
  UserEntity findByUsernameAndPassword(String username, String password);
}
