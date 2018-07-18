package demo.proxy;

public class SimpleStore implements Store {

	@Override
	public void sell(String good) {
		System.out.println(this + ": selling " +  good);
		// restock won't be intercepted here via Proxy
		restock(good);
	}

	@Override
	public int restock(String good) {
		System.out.println(this + ": restocking " +  good);
		return 0;
	}

}
