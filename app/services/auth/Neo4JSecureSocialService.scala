package services.auth

import play.api.{Logger, Application}
import securesocial.core.{Identity, IdentityId, UserServicePlugin}
import neo4j.models.users.{NeoToken, NeoUser}
import securesocial.core.providers.Token

/**
 * Handles the SecureSocial stuff like login etc
 * @param application
 */
class Neo4JSecureSocialService(application: Application) extends UserServicePlugin(application) {


    def find(id: IdentityId): Option[Identity] = {
      NeoUser.findByIdentityId(id);;
    }

    def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = {
      NeoUser.findByEmailAndProvider(email,providerId);
    }

    def save(user: Identity): Identity = {
      NeoUser.save(user);
    }

    def save(token: Token) {
      NeoToken.create(token);
    }

    def findToken(uuid: String): Option[Token] = {
      NeoToken.findToken(uuid);
    }

    def deleteToken(uuid: String) {
      NeoToken.deleteTokenByUuid(uuid);
    }

    def deleteExpiredTokens() {
      NeoToken.deleteExpiredTokens();
    }

}