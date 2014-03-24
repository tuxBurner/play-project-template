package neo4j.repositories.users;


import neo4j.TypeAliasNames;
import neo4j.models.users.NeoToken;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface NeoTokenRepository extends GraphRepository<NeoToken> {

    public static String TYPE_LABEL = "`__TYPE__"+TypeAliasNames.TOKEN+"`";

    public NeoToken findByUuid(final String uuid);

    @Query("MATCH (tokens:"+TYPE_LABEL+") WHERE tokens.expirationTime < {0} RETURN tokens")
    public List<NeoToken> findAllExpiredTokens(long now);
}
