package im.dig.trial.messenger.services.auth.rest.controller.marshalling

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import argonaut.Argonaut._
import argonaut._
import im.dig.trial.messenger.services.auth.rest.view.JsonCodecs._
import im.dig.trial.messenger.services.auth.rest.view.JsonResponse

object Marshallers {

  implicit def jsonResponseMarshaller[A : EncodeJson]: ToResponseMarshaller[JsonResponse[A]] = {
    Marshaller.withFixedContentType(ContentTypes.`application/json`) { response =>
      HttpResponse(StatusCodes.custom(response.code, response.status), entity = HttpEntity(response.asJson.spaces4))
    }
  }

}
