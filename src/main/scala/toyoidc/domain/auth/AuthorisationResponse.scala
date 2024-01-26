package toyoidc.domain.auth

sealed trait AuthorisationResponse
object AuthorisationResponse{
  case class Authorised(code: String) extends AuthorisationResponse
  case class Unauthorised(message: String) extends AuthorisationResponse
}