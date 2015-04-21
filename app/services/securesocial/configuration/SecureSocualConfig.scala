package services.securesocial.configuration

import neo4j.models.users.NeoUser
import securesocial.core.RuntimeEnvironment
import services.securesocial.Neo4JSecureSocialService

/**
 * The runtime environment for this sample app.
 */
object SecureSocialConfigEnv extends RuntimeEnvironment.Default[NeoUser] {
  override lazy val userService: Neo4JSecureSocialService = new Neo4JSecureSocialService()
  override lazy val viewTemplates: SecureSocialTemplatsHtml = new SecureSocialTemplatsHtml(this);
}

