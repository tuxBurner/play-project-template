package services.auth

import securesocial.core.{Authenticator, AuthenticatorStore}
import play.api.{Mode, Application}
import play.api.cache.Cache
import play.api.Play.current
import scala.util.Marshal
import java.io.{FileInputStream, FileOutputStream, File, PrintWriter}
import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * User: tuxburner
 * Date: 9/24/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
class DevSessionAuthStore(app: Application) extends AuthenticatorStore(app) {
  def save(authenticator: Authenticator): Either[Error, Unit] = {

    if (app.mode == Mode.Dev) {
      DevSessionAuthStore.saveToFile(authenticator);
    } else {
      Cache.set(authenticator.id, authenticator)
    }
    Right(())
  }

  def find(id: String): Either[Error, Option[Authenticator]] = {

    if (app.mode == Mode.Dev) {
      DevSessionAuthStore.find(id);
    } else {
      Right(Cache.getAs[Authenticator](id))
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
}

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

  def find(id: String): Either[Error, Option[Authenticator]] = {

    if (id.isEmpty == true) {
      Right(None)
    } else {

      val file = getDevSessionsFilePath(id);
      if (file.exists() == true) {
        val in = new FileInputStream(file)
        val bytes = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
        val mmm = Marshal.load[Authenticator](bytes)
        Right(Option.apply(mmm))
      } else {
        Left(new Error(file.getAbsoluteFile+" does not exist."))
      }
    }
  }


  def saveToFile(authenticator: Authenticator) {
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
