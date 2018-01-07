package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface AccessTokenRepository extends CrudRepository<AccessTokenEntity, Long> {
  @Transactional
  @Modifying
  @Query("Delete from AccessTokenEntity t where t.username = ?1 and t.client = ?2")
  void deleteByUsername(String username, String client);
  @Query("Select t from AccessTokenEntity t where t.username = ?1 and t.client = ?2 and t.accessToken = ?3")
  AccessTokenEntity findByUsernameClientAndAccessToken(String username, String client, String accessToken);
  @Query("Select t from AccessTokenEntity t where t.username = ?1 and t.client = ?2 and t.refreshToken = ?3")
  AccessTokenEntity findByUsernameClientAndRefreshToken(String username, String client, String refreshToken);
}
