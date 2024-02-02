package toyoidc.domain.clients

import zio.IO

trait ClientRegistry {
  def add(client: Client): IO[ClientRegistryUpdateError, Unit]

  def find(clientId: ClientId): IO[ClientRegistryReadError, Client]
}
