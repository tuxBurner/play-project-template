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
   * @param identUserId
   * @param identProviderId
   * @return
   */
  @Query("MATCH (user:" + TypeAliasNames.USER + ") WHERE user.identUserId = {0} AND user.identProviderId = {1} RETURN user")
  public NeoUser findByIdentity(final String identUserId, final String identProviderId);

  /**
   * Find user by his identity and email
   *
   * @param email
   * @param providerId
   * @return
   */
  @Query("MATCH (user:" + TypeAliasNames.USER + ") WHERE user.email = {0} AND user.identProviderId = {1} RETURN user")
  public NeoUser findByEmailAndProvider(final String email, final String providerId);
}
