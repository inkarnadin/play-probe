package repository

import config.DatabaseExecutionContext
import javax.inject.{Inject, Singleton}
import play.api.db.DBApi

import anorm._
import anorm.SqlParser.{get, scalar}

import scala.concurrent.Future

case class Star(id: Option[Long] = None,
                name: String,
                visualBrightness: Option[Double],
                absoluteMagnitude: Option[Double],
                metallicity: Option[Double])

object Star {
  implicit def toParameters: ToParameterList[Star] =
    Macro.toParameters[Star]
}

case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

@Singleton
class StarRepository @Inject() (dbapi: DBApi) (implicit dec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  private val simple = {
    get[Option[Long]]("star.id") ~
      get[String]("star.name") ~
      get[Option[Double]]("star.visual_brightness") ~
      get[Option[Double]]("star.absolute_magnitude") ~
      get[Option[Double]]("star.metallicity") map {
      case id ~ name ~ visualBrightness ~ absoluteMagnitude ~ metallicity =>
        Star(id, name, visualBrightness, absoluteMagnitude, metallicity)
    }
  }

  def findAll(page: Int = 0, pageSize: Int = 25): Future[Page[Star]] = Future {
    val offset = pageSize * page
    db.withConnection { implicit connection =>
      val stars = SQL"select * from star".as(simple.*)
      val total = SQL"select count(1) from star".as(scalar[Long].single)
      Page(stars, page, offset, total)
    }
  } (dec)

  def findById(id: Long): Future[Option[Star]] = Future {
    db.withConnection { implicit connection =>
      SQL"select * from star where id = $id".as(simple.singleOpt)
    }
  } (dec)

}
