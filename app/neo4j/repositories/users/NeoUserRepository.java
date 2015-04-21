package neo4j.repositories.users;


import neo4j.TypeAliasNames;
import neo4j.models.users.NeoUser;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * GraphRepository for the users
 */
public interface NeoUserRepository extends GraphRepository<NeoUser> {


  /**
   * Find user by his identity and providerId
   *
   * @param userId
   * @param providerId
   * @return
   */
  @Query("MATCH (user:" + TypeAliasNames.USER + ") WHERE user.userId = {0} AND user.providerId = {1} RETURN user")
  NeoUser findByIdentity(final String userId, final String providerId);

  /**
   * Find user by his identity and email
   *
   * @param email
   * @param providerId
   * @return
   */
  @Query("MATCH (user:" + TypeAliasNames.USER + ") WHERE user.email = {0} AND user.providerId = {1} RETURN user")
  NeoUser findByEmailAndProvider(final String email, final String providerId);
}
