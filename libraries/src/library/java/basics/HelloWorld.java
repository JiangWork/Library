package library.java.basics;

import java.util.ArrayList;
import java.util.List;

public class HelloWorld {
	
	private List<String> blacklist;
	
	public HelloWorld() {
		blacklist = new ArrayList<String>();
	}
	
	public void sayHello(String who, String msg) {
		if (!blacklist.contains(who)) {
			System.out.println(msg + " from " + who);
		}
	}

	public static void main(String[] args) {
		HelloWorld hw = new HelloWorld();
		hw.sayHello("Jiang", "Hello");
	}

}
