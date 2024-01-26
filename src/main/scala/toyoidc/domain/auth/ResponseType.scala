package toyoidc.domain.auth

sealed trait ResponseType

object ResponseType {
  case object Code extends ResponseType
}
