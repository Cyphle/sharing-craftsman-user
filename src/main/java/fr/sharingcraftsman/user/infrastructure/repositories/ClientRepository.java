package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.OAuthClient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<OAuthClient, Long> {
  @Query("Select c from OAuthClient c where c.name = ?1 and c.secret = ?2 and c.isActive = true")
  OAuthClient findByNameAndSecret(String name, String secret);
  @Query("Select c from OAuthClient c where c.name = ?1 and c.isActive = true")
  OAuthClient findByName(String name);
}
