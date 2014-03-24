package controllers

import play.api.mvc._
import plugins.jsAnnotations.JSRouteScala
import securesocial.core.providers.UsernamePasswordProvider

object ApplicationController extends Controller {

  /**
   * Action which is displayed when the user first comes to the page
   * @return
   */
  def index = Action {
    implicit request =>
      Ok(views.html.index.render(Some(UsernamePasswordProvider.loginForm), request))
  }


  /**
   * Returns the jsRoutes
   * @return
   */
  def jsRoutes = JSRouteScala.getJsRoutesResult

}