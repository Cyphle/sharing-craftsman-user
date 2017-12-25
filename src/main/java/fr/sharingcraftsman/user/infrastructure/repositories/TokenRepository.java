package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.OAuthToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<OAuthToken, Long> {
  @Query("Delete from OAuthToken t where t.username = ?1 and t.client = ?2")
  void deleteByUsername(String username, String client);
}
