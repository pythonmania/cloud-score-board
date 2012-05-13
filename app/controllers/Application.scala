package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import models._

object Application extends Controller {
  val scoreForm = Form(
    mapping(
      "userid" -> nonEmptyText,
      "score" -> longNumber)(Score.apply)(Score.unapply))

  def index = Action {
    Ok(views.html.index("test app", Score.all()))
  }

  def testSubmitScore = Action {
    Ok(views.html.test(scoreForm))
  }

  def submitScore = Action { implicit request =>
    scoreForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(errors.toString(), Score.all())),
      score => {
        Score.create(score)
        Redirect(routes.Application.index)
      })
  }
}