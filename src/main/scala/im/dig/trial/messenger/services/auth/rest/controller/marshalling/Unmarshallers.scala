package im.dig.trial.messenger.services.auth.rest.controller.marshalling

import akka.http.scaladsl.unmarshalling.{FromStringUnmarshaller, Unmarshaller}
import im.dig.trial.messenger.services.model.Read
import im.dig.trial.messenger.services.model.ReadSyntax._


object Unmarshallers {
  implicit def readUnmarshaller[A : Read]: FromStringUnmarshaller[A] =
    Unmarshaller.strict(_.read[A])
}
