package library.java.basics;

public class Super {
	
	public void sing() {
		System.out.println("I am saying Hello.");
	}
	
	public void sing(String song) {
		System.out.println("I am saying " + song);
	}
	
	public static void main() {
		Super sup = new Super();
		sup.sing();  // "I am saying Hello."
		sup.sing("Around 3 AM"); // "I am saying Hello."
		
		Sub sub = new Sub();
		sub.sing(); //"I am saying Hello emotionally." 
		
		Super sup1 = new Sub();
		sup1.sing();  //"I am saying Hello emotionally." 
	}
}


class Sub extends Super {
	
	public void sing() {
		System.out.println("I am saying Hello emotionally." );
	}
}