package toyoidc.http.routes

import toyoidc.domain.auth._
import toyoidc.http.responses.{BadRequest, ErrorDetail, ValidationError}
import zio.ZIO
import zio.http.URL.Location.Absolute
import zio.http.{Method, QueryParams, Request, Response, Status, URL, handler}
import zio.json.EncoderOps
import zio.prelude.Validation
import zio.prelude.ZValidation.{Failure, Success}

case class Auth(resourceOwner: ResourceOwner) {
  object Parameters {
    val responseType = "response_type"
    val clientId = "client_id"
    val redirectLocation = "redirect_uri"
    val scope = "scope"
    val state = "state"
  }

  val route = Method.GET / "auth" ->
    handler { req: Request =>
      parseRequest(req) match {
        case Success(_, req) =>
          resourceOwner.authorise(req).map(resp => authorisationResponse(req, resp))
        case Failure(_, errors) => ZIO.succeed(Response.json(BadRequest(errors).toJson).copy(status = Status.BadRequest))
      }
    }

  def authorisationResponse(request: AuthorisationRequest, response: AuthorisationResponse): Response =
    response match {
      case AuthorisationResponse.Authorised(code) =>
        val extendedQuery = request.redirectUri.queryParams.add("code", code)
        Response.seeOther(request.redirectUri.copy(queryParams = extendedQuery))
      case AuthorisationResponse.Unauthorised(message) => Response.unauthorized(message)
    }


  def parseRequest(request: Request): Validation[ValidationError, AuthorisationRequest] = {
    val query = request.url.queryParams
    Validation.validateWith(
      extractResponseType(query),
      extractClientId(query),
      extractRedirectLocation(query),
      extractScope(query),
      extractState(query)
    )(AuthorisationRequest)
  }

  def extractResponseType(query: QueryParams): Validation[ValidationError, ResponseType] = {
    val responseType =
      for {
        string <- query.get(Parameters.responseType).toRight(ValidationError.missingParameter(Parameters.responseType))
        responseType <- responseTypeLookup.get(string).toRight(
          ValidationError(
            Parameters.responseType,
            new ErrorDetail {
              def errorCode = "InvalidResponseType"
            }
          )
        )
      } yield responseType

    Validation.fromEither(responseType)
  }

  def extractClientId(query: QueryParams): Validation[ValidationError, String] = {
    val clientId = query.get(Parameters.clientId)
    Validation.fromOptionWith(ValidationError.missingParameter(Parameters.clientId))(clientId)
  }

  def extractScope(query: QueryParams): Validation[ValidationError, Scope] = {
    val scope = query.get(Parameters.scope).map(Scope)
    Validation.fromOptionWith(ValidationError.missingParameter(Parameters.scope))(scope)
  }

  def extractState(query: QueryParams): Validation[ValidationError, State] = {
    val state = query.get(Parameters.state).map(State)
    Validation.fromOptionWith(ValidationError.missingParameter(Parameters.state))(state)
  }

  def extractRedirectLocation(query: QueryParams): Validation[ValidationError, URL] = {
    val redirectLocation =
      for {
        string <- query.get(Parameters.redirectLocation).toRight(ValidationError.missingParameter(Parameters.redirectLocation))
        url <- decodeAbsoluteUrl(string)
      } yield url
    Validation.fromEither(redirectLocation)
  }

  def decodeAbsoluteUrl(candidate: String): Either[ValidationError, URL] = {
    lazy val error = ValidationError(
      Parameters.redirectLocation,
      new ErrorDetail {
        def errorCode = "InvalidLocation"
      }
    )

//   redirect URI must be absolute and have no fragment
//   https://datatracker.ietf.org/doc/html/rfc6749#section-3.1.2
    URL.decode(candidate) match {
      case Right(URL(path, abs: Absolute, query, fragment)) => Right(URL(path, abs, query, None))
      case _ => Left(error)
    }
  }

  def responseTypeLookup = Map("code" -> ResponseType.Code)
}
