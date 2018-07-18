package library.java.basics;

public class Interface {
	
	interface People {
		public void sayHello();
	}
	
	static class Chinese implements People {
		public void sayHello() {
			System.out.println("你好！");
		}
	}
	
	static class American implements People {
		public void sayHello() {
			System.out.println("Hello!");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		People people1 = new Chinese();
		people1.sayHello(); //你好！
		People people2 = new American();
		people2.sayHello();//Hello!
	}

}
