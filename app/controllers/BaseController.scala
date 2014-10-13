package controllers

import java.util.concurrent.TimeUnit

import play.api.mvc._
import neo4j.models.users.NeoUser
import securesocial.core.{GenericProfile, SecureSocial}
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration.Duration

trait BaseController extends Controller with securesocial.core.SecureSocial[NeoUser] {

  /**
   * Gets the current user from the SecuredRequest
   * @param securedRequest
   * @return
   */
  def getNeoUser(securedRequest: SecuredRequest[_]): NeoUser = {
    if (securedRequest.user.isInstanceOf[NeoUser] == false) {
      throw new IllegalArgumentException("User in request is not of the type: NeoUser")
    }

    securedRequest.user.asInstanceOf[NeoUser]
  }

}

object BaseController {

  /**
   * Key for flash to display success messages
   */
  val FLASH_SUCCESS_KEY = "success"

  /**
   * Key for flash to display error messages
   */
  val FLASH_ERROR_KEY = "error"

  /**
   * Returns the user from the default RequestHeader
   * @param request
   * @return
   */
  def currentUser(implicit request: RequestHeader,env: securesocial.core.RuntimeEnvironment[neo4j.models.users.NeoUser]): Option[neo4j.models.users.NeoUser] = {
    val mu = SecureSocial.currentUser[neo4j.models.users.NeoUser];
    Await.result(mu,Duration(10,TimeUnit.SECONDS))

    //SecureSocial.currentUser[NeoUser].onComplete(f => f.get);
  }
  /*def currentUser(implicit request: RequestHeader, env:securesocial.core.RuntimeEnvironment[_]): Option[NeoUser] = {
    SecureSocial.currentUser[NeoUser].map { maybeUser =>
      maybeUser;
    }
  } */
}