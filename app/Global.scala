import java.lang.reflect.Constructor

import neo4j.models.users.NeoUser
import securesocial.core.RuntimeEnvironment
import services.auth.configuration.SecureSocialConfigEnv

/**
 * Created by tuxburner on 10.10.14.
 */
object Global extends play.api.GlobalSettings {


  /**
   * An implementation that checks if the controller expects a RuntimeEnvironment and
   * passes the instance to it if required.
   *
   * This can be replaced by any DI framework to inject it differently.
   *
   * @param controllerClass
   * @tparam A
   * @return
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    val instance = controllerClass.getConstructors.find { c =>
      val params = c.getParameterTypes
      params.length == 1 && params(0) == classOf[RuntimeEnvironment[NeoUser]]
    }.map {
      _.asInstanceOf[Constructor[A]].newInstance(SecureSocialConfigEnv)
    }
    instance.getOrElse(super.getControllerInstance(controllerClass))
  }

}
