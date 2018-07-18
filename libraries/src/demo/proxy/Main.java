package demo.proxy;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Store simpleStore = new SimpleStore();
		Store storeProxy = StoreSimpleProxy.bind(simpleStore);
		System.out.println(simpleStore);
		storeProxy.sell("Macos Computer");
		storeProxy.restock("MS computer");
		
	}

}
