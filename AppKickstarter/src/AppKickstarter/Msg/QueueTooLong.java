package AppKickstarter.Msg;

import AppKickstarter.Server.Client;

public class QueueTooLong extends Command {
	private Client client;

	public QueueTooLong(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return this.client;
	}

	@Override
	public String toString() {
		return String.format("QueueTooLong: %s %s", client.getClientID(), client.getnPerson());
	}

}
