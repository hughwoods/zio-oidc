package toyoidc.http.routes

import toyoidc.domain.clients.Client.SinglePageClient
import toyoidc.domain.clients.ClientRegistry
import zio.http.URL
import zio.{Chunk, Random, ZLayer}

case class ClientRegistrationService(clientRegistry: ClientRegistry) {
  def createSinglePageClient(name: String, redirectUrls: Chunk[URL]) =
    for {
      id <- Random.nextUUID
    } yield SinglePageClient(name, id.toString, redirectUrls)
}
object ClientRegistrationService{
 val live = ZLayer.fromFunction(ClientRegistrationService.apply _)
}
