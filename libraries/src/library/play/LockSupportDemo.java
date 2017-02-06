package library.play;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {
	private final static CountDownLatch startLatch = new CountDownLatch(1);
	private final static CountDownLatch parkLatch = new CountDownLatch(1);
	static class LockThread extends Thread {
		public void run() {
			startLatch.countDown();			
			System.out.println("Working ... ");
			parkLatch.countDown();
			LockSupport.park(this);
			System.out.println("Working after park ...");
		}
		
//		public void block() {
//			
//			LockSupport.park();
//		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		LockThread th = new LockThread();
		th.start();
		startLatch.await();
		System.out.println("Before park, thread state: " + th.getState());
		parkLatch.await();
		System.out.println("After park, thread state: " + th.getState());
		Thread.sleep(1000);
		System.out.println("Unparking.. " + th.getState());
		th.interrupt();
	}

}
