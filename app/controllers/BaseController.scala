package controllers

import play.api.mvc._
import neo4j.models.users.NeoUser
import securesocial.core.{SecureSocial, SecuredRequest}

trait BaseController extends Controller with securesocial.core.SecureSocial {

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
  def getNeoUserFromRequest(implicit request: RequestHeader): Option[NeoUser] = {
    SecureSocial.currentUser match {
      case Some(userIdent) => {
        if (userIdent.isInstanceOf[NeoUser]) {
          Option.apply(userIdent.asInstanceOf[NeoUser])
        } else {
          Option.empty
        }
      }
      case None => Option.empty
    }
  }
}