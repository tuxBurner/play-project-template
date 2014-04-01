package neo4j.repositories.users;


import neo4j.TypeAliasNames;
import neo4j.models.users.NeoToken;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 * Repository for managing the login tokens
 */
public interface NeoTokenRepository extends GraphRepository<NeoToken> {

  /**
   * Find a token by its uuid
   *
   * @param uuid
   * @return
   */
  public NeoToken findByUuid(final String uuid);

  /**
   * Find all expired tokens
   *
   * @param now
   * @return
   */
  @Query("MATCH (tokens:" + TypeAliasNames.TOKEN + ") WHERE tokens.expirationTime < {0} RETURN tokens")
  public List<NeoToken> findAllExpiredTokens(long now);
}
