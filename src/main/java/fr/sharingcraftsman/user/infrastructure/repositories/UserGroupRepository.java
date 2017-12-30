package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {
  @Query("Select u from UserGroup u where u.username = ?1")
  List<UserGroup> findByUsername(String username);
}
