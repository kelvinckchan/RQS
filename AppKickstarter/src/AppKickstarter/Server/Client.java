package AppKickstarter.Server;

public class Client {

	private String ClientID;
	private int nPerson;

	public Client(String ClientID, int nPerson) {
		this.ClientID = ClientID;
		this.nPerson = nPerson;
	}

	public String getClientID() {
		return this.ClientID;
	}

	public int getnPerson() {
		return this.nPerson;
	}

}