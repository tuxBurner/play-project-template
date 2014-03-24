package neo4j.repositories.users;


import neo4j.TypeAliasNames;
import neo4j.models.users.NeoUser;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NeoUserRepository extends GraphRepository<NeoUser> {

  public static String TYPE_LABEL = "`__TYPE__"+TypeAliasNames.USER+"`";

  @Query("MATCH (user:"+TYPE_LABEL+") WHERE user.identUserId = {0} AND user.identProviderId = {1} RETURN user")
  public NeoUser findByIdentity(final String identUserId, final String identProviderId);

  @Query("MATCH (user:"+TYPE_LABEL+") WHERE user.email = {0} AND user.identProviderId = {1} RETURN user")
  public NeoUser findByEmailAndProvider(final String email, final String providerId);
}
