package services.securesocial

import neo4j.models.users.{SecureUser, NeoToken, NeoUser}
import org.joda.time.DateTime
import play.api.Logger
import securesocial.core.{PasswordInfo, BasicProfile}
import securesocial.core.providers.MailToken
import securesocial.core.services.{SaveMode, UserService}

import scala.concurrent.Future

/**
 * Handles the SecureSocial stuff like login etc with neo4j
 *
 */
class Neo4JSecureSocialService extends UserService[NeoUser] {


  override  def find(providerId: String, userId: String): Future[Option[BasicProfile]] = {
    NeoUser.findUserByIdAndProviderId(userId, providerId) match {
      case user: NeoUser => Future.successful(Option.apply(neoUserToBasicProfile(user)));
      case _ => Future.successful(Option.empty)
    }
  }

  override  def findByEmailAndProvider(email: String, providerId: String): Future[Option[BasicProfile]] = {
    NeoUser.findByEmailAndProvider(email, providerId) match {
      case Some(user: NeoUser) => Future.successful(Option.apply(neoUserToBasicProfile(user)));
      case _ => Future.successful(Option.empty)
    }
  }

  override  def save(profile: BasicProfile, mode: SaveMode): Future[NeoUser] = {
    Future.successful(NeoUser.save(profile,mode));
  }


  override  def saveToken(token: MailToken): Future[MailToken]= {
    Future.successful(NeoToken.create(token).toScalaToken);
  }

  override  def findToken(uuid: String):  Future[Option[MailToken]] = {
    Future.successful(NeoToken.findToken(uuid));
  }

  override  def deleteToken(uuid: String): Future[Option[MailToken]]= {
    Future.successful(Option.apply(NeoToken.deleteTokenByUuid(uuid).toScalaToken));
  }

  override  def deleteExpiredTokens() {
    NeoToken.deleteExpiredTokens();
  }

  override  def link(current: NeoUser, to: BasicProfile): Future[NeoUser] = {
    Logger.error("IMPLEMENT MEE !!!! link");
    Future.successful(null);
  }

  override def passwordInfoFor(user: NeoUser): Future[Option[PasswordInfo]] = {
    Logger.error("IMPLEMENT MEE !!!! passwordInfoFor");
    Future.successful(Option.empty);
  }

  override def updatePasswordInfo(user: NeoUser, info: PasswordInfo): Future[Option[BasicProfile]] = {
    Logger.error("IMPLEMENT MEE !!!! updatePasswordInfo");
    Future.successful(Option.empty);
  }

  /**
   * Transforms the given NeoUser to a BasicProfile
   * @param neoUser
   * @return
   */
  def neoUserToBasicProfile(neoUser: NeoUser): BasicProfile = {
    BasicProfile.apply(neoUser.providerId(), neoUser.userId(), neoUser.firstName(), neoUser.lastName(), neoUser.fullName(), neoUser.email(), neoUser.avatarUrl(), neoUser.authMethod(), Option.empty, Option.empty,neoUser.passwordInfo());
  }

}