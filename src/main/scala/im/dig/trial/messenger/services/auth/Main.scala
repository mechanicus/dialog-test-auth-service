package im.dig.trial.messenger.services.auth

import akka.actor.{ActorSystem, Props}
import im.dig.trial.messenger.services.auth.cluster.{AuthApi, AuthService, CrudServiceClient}
import im.dig.trial.messenger.services.auth.rest.controller.RestApi

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MessengerBackend")
    val crudServiceClient = system.actorOf(Props[CrudServiceClient], "crud-service-client")
    system.actorOf(Props(classOf[AuthService], crudServiceClient), "auth-service")
    val authApi = new AuthApi(crudServiceClient)
    new RestApi(authApi).run()
  }
}
