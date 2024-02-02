package toyoidc.domain.clients

sealed trait ClientRegistryUpdateError

sealed trait ClientRegistryReadError

object ClientRegistryError {
  case object ServiceUnavailable extends ClientRegistryReadError with ClientRegistryUpdateError
  case object ClientNotFound extends ClientRegistryReadError
  case object Conflict extends ClientRegistryUpdateError
}
