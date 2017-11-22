package AppKickstarter.Msg;

public class CheckOut extends Command {

	private int TableNo;
	private int TotalSpending;

	public CheckOut(int TableNo, int TotalSpending) {
		this.TableNo = TableNo;
		this.TotalSpending = TotalSpending;
	}

	public int getTableNo() {
		return this.TableNo;
	}

	public int getTotalSpending() {
		return this.TotalSpending;
	}

	@Override
	public String toString() {
		return String.format("CheckOut: %s %s", this.TableNo, this.TotalSpending);
	}

}
