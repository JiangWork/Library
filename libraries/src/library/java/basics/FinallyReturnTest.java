package library.java.basics;



public class FinallyReturnTest {

	public static int run() {
		try {
			int a = 10;
			return a;
		} finally {
			int b = 100;
			return b;
		}
	}
	
//	 stack=1, locals=5, args_size=0
//	         0: bipush        10
//	         2: istore_0
//	         3: iload_0
//	         4: istore_1
//	         5: bipush        100
//	         7: istore_2
//	         8: iload_2
//	         9: ireturn
//	        10: astore_3
//	        11: bipush        100
//	        13: istore        4
//	        15: iload         4
//	        17: ireturn
//    Exception table:
//        from    to  target type
//            0     5    10   any
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("你好你hi".charAt(0));
		run();
	}

}
