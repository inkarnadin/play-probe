package repository

import config.DatabaseExecutionContext
import javax.inject.{Inject, Singleton}
import play.api.db.DBApi

import scala.util.{Failure, Success}
import anorm._
import anorm.SqlParser.{ get, str }
import scala.concurrent.Future

case class Star(id: Option[Long] = None, name: String)

@Singleton
class StarRepository @Inject() (dbapi: DBApi) (implicit dec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  private[repository] val simple = {
    get[Option[Long]]("star.id") ~ str("star.name") map {
      case id ~ name => Star(id, name)
    }
  }

  def options: Future[Seq[(String,String)]] = Future(db.withConnection { implicit connection =>
    SQL"select * from star".
      fold(Seq.empty[(String, String)], ColumnAliaser.empty) { (acc, row) =>
        row.as(simple) match {
          case Failure(parseErr) => {
            println(s"Fails to parse $row: $parseErr")
            acc
          }
          case Success(Star(Some(id), name)) => (id.toString -> name) +: acc
          case Success(Star(None, _)) => acc
        }
      }
  }).flatMap {
    case Left(err :: _) => Future.failed(err)
    case Left(_) => Future(Seq.empty)
    case Right(acc) => Future.successful(acc.reverse)
  }

}
