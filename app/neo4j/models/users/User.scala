package neo4j.models.users

import securesocial.core.BasicProfile

/**
 * Created by tuxburner on 11.10.14.
 */
case class SecureUser(main: NeoUser, identities: List[NeoUser])
