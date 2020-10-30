package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import repository.StarRepository

@Singleton
class StarController @Inject() (starService: StarRepository,
                                cc: ControllerComponents) extends AbstractController(cc) {

  def get = Action {
    Ok("one star")
  }

  def list = Action.async { implicit request =>
    starService.options.map { options =>
      Ok(options)
    }
  }

}
