package im.dig.trial.messenger.services.auth.rest.controller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import im.dig.trial.messenger.services.auth.cluster.AuthApi
import im.dig.trial.messenger.services.auth.rest.controller.marshalling.{Marshallers, Unmarshallers}
import im.dig.trial.messenger.services.auth.rest.view.{JsonCodecs, Responses}
import im.dig.trial.messenger.services.model.Nickname

import scala.concurrent.ExecutionContext

final class RestApi(
  private val authApi: AuthApi
) extends RouteGroup
     with CustomRejectionHandler
     with CustomExceptionHandler
{

  import JsonCodecs._
  import Marshallers._
  import Unmarshallers._

  override def routes(implicit ec: ExecutionContext): Route = {
    pathPrefix("auth") {
      path("users") {
        post { formFields('nickname.as[Nickname]) { nickname =>
          onSuccess(authApi.registerUser(nickname)) {
            case Right(userId) => complete(Responses.ok("userId", userId))
            case Left(message) => complete(Responses.error(500, "INTERNAL_ERROR", message))
          }
        }}
      } ~
      path("user" / SHA256Segment) { userId =>
        get {
          complete {
            for {
              user <- authApi.getUserInfo(userId)
            } yield Responses.ok(user)
          }
        }
      } ~
      path("user" / "search") {
        get { parameters('nickname.as[Nickname]) { nickname =>
          complete {
            for {
              user <- authApi.findUserByNickname(nickname)
            } yield Responses.ok(user)
          }
        }}
      } ~
      path("sessions") {
        post { formFields('nickname.as[Nickname]) { nickname =>
          onSuccess(authApi.startSession(nickname)) {
            case Right(sessionId) => complete(Responses.ok("sessionId", sessionId))
            case Left(message) => complete(Responses.error(500, "INTERNAL_ERROR", message))
          }
        }}
      } ~
      path("session" / SHA256Segment) { sessionId =>
        delete {
          complete {
            for {
              stoppedSessions <- authApi.stopSession(sessionId)
            } yield Responses.ok("stoppedSessions", stoppedSessions)
          }
        }
      }
    }
  }

  def run()(implicit system: ActorSystem): Unit = {
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher
    Http().bindAndHandle(routes, "localhost", 8002)
  }

}
