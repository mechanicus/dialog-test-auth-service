package im.dig.trial.messenger.services.auth.cluster

import akka.actor.{Actor, ActorRef}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import im.dig.trial.messenger.services.messages.{GetUserId, ReadSession}
import im.dig.trial.messenger.services.model.Session

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/** Элемент кластера, предоставляющий Auth-сервис для других микросервисов */
final class AuthService(crudServiceClient: ActorRef) extends Actor {

  private implicit val ec: ExecutionContext = context.dispatcher
  private implicit val timeout: Timeout = Timeout(1.second)

  override def receive: Receive = {
    case GetUserId(sessionId) =>
      (crudServiceClient ? ReadSession(sessionId)).mapTo[Option[Session]].map(_.map(_.userId)) pipeTo sender()
  }

}
