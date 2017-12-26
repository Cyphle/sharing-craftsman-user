package fr.sharingcraftsman.user.domain.client;

public interface ClientStock {
  Client findClient(Client client);

  Client findClientByName(Client client);

  Client createClient(Client client);
}
