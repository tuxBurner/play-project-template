package services.securesocial

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.Date

import play.api.Mode
import play.api.Play.current
import play.api.cache.Cache
import securesocial.core.authenticator.{Authenticator, AuthenticatorStore}
import securesocial.core.services.CacheService

import scala.reflect.ClassTag
import scala.util.Marshal

/**
 * Created with IntelliJ IDEA.
 * User: tuxburner
 * Date: 9/24/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
/*class DevSessionAuthStore[A <: Authenticator[_]](cacheService: CacheService,app: play.api.Application) extends AuthenticatorStore[A] {
  def save(authenticator: A,timeoutInSeconds: Int): Either[Error, Unit] = {

    if (app.mode == Mode.Dev) {
      DevSessionAuthStore.saveToFile(authenticator);
    } else {
      Cache.set(authenticator.id, authenticator)
    }
    Right(())
  }

  def find(id: String)(implicit ct: ClassTag[A]): Either[Error, Option[Authenticator[_]]] = {

    if (app.mode == Mode.Dev) {
      DevSessionAuthStore.find(id);
    } else {
      Right(Cache.getAs[A](id))
    }
  }

  def delete(id: String): Either[Error, Unit] = {
    if (app.mode == Mode.Dev) {
      DevSessionAuthStore.delete(id);
    } else {
      Cache.set(id, "", 1)
    }
    Right(())
  }



} */

/**
 * Helper for storing the current authenticator when the app is in dev mode
 */
object DevSessionAuthStore {


  def delete(id: String) {
    val file = getDevSessionsFilePath(id);
    if (file.exists() == true) {
      file.delete();
    }
  }

  def find(id: String): Right[Nothing, Option[Authenticator[_]]] = {

    if (id.isEmpty == true) {
      Right(None)
    } else {

      val file = getDevSessionsFilePath(id);
      if (file.exists() == true) {
        val in = new FileInputStream(file)
        val bytes = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
        val mmm = Marshal.load[Authenticator[_]](bytes)
        Right(Option.apply(mmm))
      } else {
        Left(new Error(file.getAbsoluteFile+" does not exist."))
        Right(None)
      }
    }
  }


  def saveToFile(authenticator: Authenticator[_]) {
    val file = getDevSessionsFilePath(authenticator.id);

    val fos = new FileOutputStream(file, false)
    val bla = Marshal.dump(authenticator);
    fos.write(bla);
  }

  def getDevSessionsFilePath(id: String): File = {

    val rootPath = new File("target/devsessions")
    if (rootPath.exists() == false) {
      rootPath.mkdir()
    }


    val idSub = id.substring(0, 16);

    // check if we have an old file her older than 10 mins
    val files = rootPath.listFiles();
    val currDate = new Date().getTime - 1000 * 60 * 10;
    for(file <- files) {
      if(file.getName != idSub) {
        if(file.lastModified() < currDate) {
          file.delete()
        }
      }
    }

    new File(rootPath, idSub)
  }
}
