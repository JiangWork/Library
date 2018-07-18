package demo.misc;

import java.util.ArrayList;
import java.util.List;

public class EscapeAnalysisTest {
	
	public static void alloc() {
		byte[] b = new byte[2];
		b[1] = 1;
	}
	
	public static void primitiveTest() {

		long start = System.currentTimeMillis();
		for(int i = 0; i < 100000000; ++i) {
			alloc();
		}
		
		System.out.println("primitiveTest:" + (System.currentTimeMillis() - start));
	}
	
	
	public static void ObjectAllocationTest() {
		long start = System.currentTimeMillis();
		//List<Body> bodies = new ArrayList<EscapeAnalysisTest.Body>();
		for(int i = 0; i < 100000000; ++i) {
			Body body = new Body();
			//bodies.add(body);
		}
		
		System.out.println("ObjectAllocationTest: " + (System.currentTimeMillis() - start));
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Run1: -server -XX:-DoEscapeAnalysis -Xmx10m -Xms10m -XX:+PrintGC
		// Run2: -server -XX:+DoEscapeAnalysis -Xmx10m -Xms10m -XX:+PrintGC
		primitiveTest();
		ObjectAllocationTest();
		
		// Run1:
		// primitiveTest:1135
		// ObjectAllocationTest: 851
		
		// Run2:
		// primitiveTest:10
		// ObjectAllocationTest: 11
		
	}
	
	private static class Body {
		private String value;
		private int age;
		
		public Body() {
			age = 100;
		}
	}

}
