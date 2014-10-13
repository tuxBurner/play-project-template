package controllers

import neo4j.models.users.NeoUser
import play.api.mvc.Action
import jsAnnotations.JSRouteScala
import jsAnnotations.JSRoute
import securesocial.core.RuntimeEnvironment
import securesocial.core.providers.UsernamePasswordProvider


class ApplicationController(override implicit val env: RuntimeEnvironment[NeoUser]) extends securesocial.core.SecureSocial[NeoUser] {

//object ApplicationController extends Controller {

  /**
   * Action which is displayed when the user first comes to the page
   * @return
   */
  def index =  Action {
    implicit request =>
      Ok(views.html.index.render(Some(UsernamePasswordProvider.loginForm), request, env))
  }


  /**
   * Returns the jsRoutes
   * @return
   */
  def jsRoutes = JSRouteScala.getJsRoutesResult

}