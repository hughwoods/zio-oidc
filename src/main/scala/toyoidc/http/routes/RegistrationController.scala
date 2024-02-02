package toyoidc.http.routes

import toyoidc.http.responses.{BadRequest, ErrorDetail, ValidationError}
import zio.ZIO.service
import zio._
import zio.http.Response.Patch.SetStatus
import zio.http.Status.Created
import zio.http.URL.Location.Absolute
import zio.http._
import zio.json.EncoderOps
import zio.prelude.{Validation, ZValidation}

case class RegistrationController(clientRegistry: ClientRegistrationService) {
  object Parameters {
    val name = "name"
    val redirectLocations = "redirectLocations"
  }
  val route = Method.PUT / "client" -> Handler.fromFunctionZIO(handlerFunction)

  def handlerFunction(request: Request) = {
    parse(request) match {
      case ZValidation.Failure(_, errors) =>
        ZIO.succeed(Response.json(BadRequest(errors).toJson).copy(status = Status.BadRequest))
      case ZValidation.Success(_, value) =>
        clientRegistry
          .createSinglePageClient(value._1, value._2)
          .map(_ => Response.ok.patch(SetStatus(Created)))
    }
  }

  def parse(request: Request) = {
    val name = extractName(request.url.queryParams)
    val redirectLocations = extractRedirectLocations(request.url.queryParams)
    Validation.validateWith(name, redirectLocations)((a, b) => (a, b))
  }

  def extractName(query: QueryParams) = {
    val name = query.get(Parameters.name)
    Validation.fromOptionWith(ValidationError.missingParameter(Parameters.name))(name)
  }

  def extractRedirectLocations(query: QueryParams): Validation[ValidationError, Chunk[URL]] = {
    val redirectLocation =
      for {
        strings <-
          query
            .get(Parameters.redirectLocations)
            .toRight(ValidationError.missingParameter(Parameters.redirectLocations))
        urlValidationResults = strings.split(',').map(decodeAbsoluteUrl)
        urls <- urlsOrFirstError(urlValidationResults)
      } yield urls
    Validation.fromEither(redirectLocation)
  }

  def urlsOrFirstError(validationResults: Seq[Either[ValidationError, URL]]): Either[ValidationError, Chunk[URL]] = {
    validationResults.foldLeft(Right(Chunk.empty): Either[ValidationError, Chunk[URL]])((acc, nextResult) =>
      acc match {
        case Right(urls) =>
          nextResult match {
            case Right(url)  => Right(urls :+ url)
            case Left(error) => Left(error)
          }
        case Left(error) => Left(error)
      }
    )
  }

  def decodeAbsoluteUrl(candidate: String): Either[ValidationError, URL] = {
    lazy val error = ValidationError(
      Parameters.redirectLocations,
      new ErrorDetail {
        def errorCode = "InvalidLocation"
      }
    )

    //   redirect URI must be absolute and have no fragment
    //   https://datatracker.ietf.org/doc/html/rfc6749#section-3.1.2
    URL.decode(candidate) match {
      case Right(URL(path, abs: Absolute, query, fragment)) => Right(URL(path, abs, query, None))
      case _                                                => Left(error)
    }
  }
}

object RegistrationController {
  val live = ZLayer.fromFunction(RegistrationController.apply _)

  val liveRoute = {
    def handlerz(request: Request) =
      for {
        client <- service[RegistrationController]
        response <- client.handlerFunction(request)
      } yield response

    Method.PUT / "client" -> Handler.fromFunctionZIO(handlerz)
  }
}
