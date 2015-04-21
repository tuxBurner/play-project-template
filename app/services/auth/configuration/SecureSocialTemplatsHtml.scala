package services.auth.configuration

import play.api.data.Form
import play.api.i18n.Lang
import play.api.mvc.RequestHeader
import play.twirl.api.Html

import securesocial.controllers.{ChangeInfo, RegistrationInfo, ViewTemplates}
import securesocial.core.RuntimeEnvironment

/**
 * Created by tuxburner on 10.10.14.
 */
class SecureSocialTemplatsHtml(env: RuntimeEnvironment[neo4j.models.users.NeoUser]) extends ViewTemplates {

  implicit val implicitEnv = env

  override def getLoginPage(form: Form[(String, String)], msg: Option[String])(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.securesocialtpl.login(form, msg)(request, lang, env)
  }

  override def getPasswordChangePage(form: Form[ChangeInfo])(implicit request: RequestHeader, lang: Lang): Html = {
    securesocial.views.html.passwordChange(form)(request, lang, env)
  }

  override def getNotAuthorizedPage(implicit request: RequestHeader, lang: Lang): Html = {
    securesocial.views.html.notAuthorized()(request, lang, env)
  }

  override def getStartSignUpPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.securesocialtpl.signup.startSignUp(form)
  }

  override def getSignUpPage(form: Form[RegistrationInfo], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.securesocialtpl.signup.signUp(form, token)(request, lang, env)
  }

  override def getResetPasswordPage(form: Form[(String, String)], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.securesocialtpl.resetpwd.resetPasswordPage(form, token)(request, lang, env)
  }

  override def getStartResetPasswordPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
    views.html.securesocialtpl.resetpwd.startResetPassword(form)(request, lang, env)
  }
}
