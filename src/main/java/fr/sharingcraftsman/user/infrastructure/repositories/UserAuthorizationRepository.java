package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.UserAuthorizationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAuthorizationRepository extends CrudRepository<UserAuthorizationEntity, Long> {
  @Query("Select u from UserAuthorizationEntity u where u.username = ?1")
  List<UserAuthorizationEntity> findByUsername(String username);
  @Query("Select u from UserAuthorizationEntity u where u.username = ?1 and u.group = ?2")
  UserAuthorizationEntity findByUsernameAndGroup(String username, String group);
}
