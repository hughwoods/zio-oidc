package toyoidc.domain.auth

import zio.{Task, UIO}

trait ResourceOwner {
  def authorise(request: AuthorisationRequest): UIO[AuthorisationResponse]
}
