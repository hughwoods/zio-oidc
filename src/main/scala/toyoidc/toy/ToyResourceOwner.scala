package toyoidc.toy

import toyoidc.domain.auth.{AuthorisationRequest, AuthorisationResponse, ResourceOwner}
import zio.Random

class ToyResourceOwner extends ResourceOwner {
  override def authorise(request: AuthorisationRequest) =
    for {
      guid <- Random.nextUUID
    } yield AuthorisationResponse.Authorised(guid.toString)
}
