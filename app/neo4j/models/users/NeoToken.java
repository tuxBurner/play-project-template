package neo4j.models.users;

import neo4j.TypeAliasNames;
import neo4j.models.AbstractNeoNode;
import neo4j.Neo4JServiceProvider;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import play.Logger;
import scala.Option;
import securesocial.core.java.Token;

import java.util.List;

// TODO: hooold me in the ram and be onyl Token ? Than use the scala impl from the sample in  Neo4jSecureSocialProvider
@NodeEntity
@TypeAlias(value = TypeAliasNames.TOKEN)
public class NeoToken extends AbstractNeoNode {


    @Indexed(unique = true)
    public String uuid;

    public String email;

    public long expirationTime;

    public boolean isSignUp;

    /**
     * Transforms the token to a scala token
     * @return
     */
    private securesocial.core.providers.Token toScalaToken() {
        return securesocial.core.providers.Token$.MODULE$.apply(
                uuid, email, new DateTime(created), new DateTime(expirationTime), isSignUp
        );
    }

    /**
     * Creates a token
     *
     * @param scalaToken
     * @return
     */
    public static NeoToken create(securesocial.core.providers.Token scalaToken) {
        final Token token = Token.fromScala(scalaToken);
        final NeoToken tokenAction = new NeoToken();
        tokenAction.uuid = token.uuid;
        tokenAction.email = token.email;
        tokenAction.expirationTime = token.expirationTime.getMillis();
        tokenAction.isSignUp = token.isSignUp;
        Neo4JServiceProvider.get().neoTokenRepository.save(tokenAction);
        return tokenAction;
    }

    /**
     * finds the token by the id
     * @param uuid
     * @return
     */
    private static NeoToken findTokenByUuid(final String uuid) {
        final NeoToken byUuid = Neo4JServiceProvider.get().neoTokenRepository.findByUuid(uuid);
        return byUuid;
    }

    /**
     * Deletes the token by the given uuid
     * @param uuid
     */
    public static void deleteTokenByUuid(final String uuid) {
        final NeoToken token = findTokenByUuid(uuid);
        if(token != null) {
            Neo4JServiceProvider.get().neoTokenRepository.delete(token);
        }
    }

    /**
     * Gets the token by the given uuid
     *
     * @param uuid
     * @return
     */
    public static Option<securesocial.core.providers.Token> findToken(final String uuid) {
        final NeoToken token = findTokenByUuid(uuid);
        if (token == null) {
            return null;
        }
        return Option.apply(token.toScalaToken());
    }


    /**
     * Deletes all tokens which are expired
     */
    public static void deleteExpiredTokens() {
        final List<NeoToken> allExpiredTokens = Neo4JServiceProvider.get().neoTokenRepository.findAllExpiredTokens(DateTime.now().getMillis());
        if(CollectionUtils.isEmpty(allExpiredTokens) == false) {
            if(Logger.isDebugEnabled() ==true) {
                Logger.debug("Found "+allExpiredTokens.size()+" expired tokens.");
            }
            Neo4JServiceProvider.get().neoTokenRepository.delete(allExpiredTokens);
        }
    }


}
