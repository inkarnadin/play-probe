package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import repository.StarRepository
import javax.inject.Inject

import scala.concurrent.ExecutionContext

@Singleton
class StarController @Inject() (starService: StarRepository,
                                cc: ControllerComponents) (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def get = Action {
    Ok("one star")
  }

  def list(page: Int) = Action.async { implicit request =>
    starService.findAll(page = page).map { page =>
      Ok(page.items.toString())
    }
  }

}
