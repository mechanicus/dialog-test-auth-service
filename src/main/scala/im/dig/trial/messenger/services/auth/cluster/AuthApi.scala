package im.dig.trial.messenger.services.auth.cluster

import cats.implicits._
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import im.dig.trial.messenger.services.messages._
import im.dig.trial.messenger.services.model._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

/** Основной класс, предоставляющий scala-api сервиса авторизации */
final class AuthApi(crudServiceClient: ActorRef) {

  private implicit val timeout: Timeout = Timeout(1.second)

  def registerUser(nickname: Nickname)(implicit ec: ExecutionContext): Future[Either[String, UserId]] = {
    val userId = SHA256.generate()
    (crudServiceClient ? CreateUser(userId, nickname)).mapTo[Either[String, Int]] map {
      case Right(_) => Right(userId)
      case Left(message) => Left(message)
    }
  }

  def findUserByNickname(nickname: Nickname): Future[Option[User]] =
    (crudServiceClient ? FindUserByNickname(nickname)).mapTo[Option[User]]

  def startSession(nickname: Nickname)(implicit ec: ExecutionContext): Future[Either[String, SessionId]] = {
    (crudServiceClient ? FindUserByNickname(nickname)).mapTo[Option[User]] flatMap {
      case Some(user) =>
        val sessionId = SHA256.generate()
        (crudServiceClient ? CreateSession(sessionId, user.userId)).map { _ => sessionId.asRight }
      case None =>
        Future.successful("unknown user".asLeft)
    }
  }

  def stopSession(sessionId: SessionId): Future[Int] =
    (crudServiceClient ? DeleteSession(sessionId)).mapTo[Int]

  def getUserInfo(userId: UserId): Future[Option[User]] =
    (crudServiceClient ? ReadUser(userId)).mapTo[Option[User]]

}
