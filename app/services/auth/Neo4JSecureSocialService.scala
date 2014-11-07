package services.auth

import neo4j.models.users.{SecureUser, NeoToken, NeoUser}
import org.joda.time.DateTime
import securesocial.core.{PasswordInfo, BasicProfile}
import securesocial.core.providers.MailToken
import securesocial.core.services.{SaveMode, UserService}

import scala.concurrent.Future

/**
 * Handles the SecureSocial stuff like login etc
 *
 */
class Neo4JSecureSocialService extends UserService[NeoUser] {


  def find(providerId: String, userId: String): Future[Option[BasicProfile]] = {
    NeoUser.findUserByIdAndProviderId(userId, providerId) match {
      case user: NeoUser => Future.successful(Option.apply(neoUserToBasicProfile(user)));
      case _ => Future.successful(Option.empty)
    }
  }

  def findByEmailAndProvider(email: String, providerId: String): Future[Option[BasicProfile]] = {
    NeoUser.findByEmailAndProvider(email, providerId) match {
      case user: NeoUser => Future.successful(Option.apply(neoUserToBasicProfile(user)));
      case _ => Future.successful(Option.empty)
    }
  }

  def save(profile: BasicProfile, mode: SaveMode): Future[NeoUser] = {
    Future.successful(NeoUser.save(profile));
  }


  def saveToken(token: MailToken): Future[MailToken]= {
    Future.successful(NeoToken.create(token).toScalaToken);
  }

  def findToken(uuid: String):  Future[Option[MailToken]] = {
    Future.successful(NeoToken.findToken(uuid));
  }

  def deleteToken(uuid: String): Future[Option[MailToken]]= {
    Future.successful(Option.apply(NeoToken.deleteTokenByUuid(uuid).toScalaToken));
  }

  def deleteExpiredTokens() {
    NeoToken.deleteExpiredTokens();
  }

  def link(current: NeoUser, to: BasicProfile): Future[NeoUser] = ???

  override def passwordInfoFor(user: NeoUser): Future[Option[PasswordInfo]] = ???

  override def updatePasswordInfo(user: NeoUser, info: PasswordInfo): Future[Option[BasicProfile]] = ???

  def neoUserToBasicProfile(neoUser: NeoUser): BasicProfile = {
    BasicProfile.apply(neoUser.providerId(), neoUser.userId(), neoUser.firstName(), neoUser.lastName(), neoUser.fullName(), neoUser.email(), neoUser.avatarUrl(), neoUser.authMethod(), Option.empty, Option.empty,neoUser.passwordInfo());
  }


}