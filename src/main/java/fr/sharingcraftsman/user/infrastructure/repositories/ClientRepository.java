package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.ApiClient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ApiClient, Long> {
  @Query("Select c from ApiClient c where c.name = ?1 and c.secret = ?2 and u.isActive = true")
  ApiClient findByNameAndSecret(String name, String secret);
}
