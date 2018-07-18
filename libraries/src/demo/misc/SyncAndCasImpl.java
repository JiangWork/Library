package demo.misc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncAndCasImpl {

	interface Addable {
		public int add(int inc);
		public int getVal();
	}
	
	static class CasAdder implements Addable {
		private volatile int val = 0;
		private Lock lock;
		public CasAdder() {lock = new ReentrantLock();}
		
		public int add(int inc) {
			for(;;) {
				int oldVal = getVal();
				if (cas(oldVal+inc, oldVal)) {
					return oldVal;
				}
			}
		}
		
		private boolean cas(int newVal, int expectedVal) {
			// simulate atomic compare and swap operations
			lock.lock();
			try {
			if (getVal() == expectedVal) {
				val = newVal; // broken here: above two ops should be together
				return true;
			}} finally {
				lock.unlock();
			}
			return false;
		}
		
		public int getVal() {
			return val;
		}
	}
	
	static class SyncAdder implements Addable {

		private int val = 0;
		public SyncAdder() {}
		
		@Override
		public synchronized int add(int inc) {
			int oldVal = val;
			val += inc;
			return oldVal;
		}

		public synchronized int getVal() {
			return val;
		}
		
	}
	
	
	static class AddWorker extends Thread {
		Addable addable;
		
		public AddWorker(Addable addable) {
			this.addable = addable;
		}
		
		@Override
		public void run() {
			int times = 10000;
			for (int  i = 0; i < times; ++i) {
				addable.add(1);  // add one
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Addable addable = new CasAdder();
		int workerNum = 20;
		AddWorker[] workers = new AddWorker[workerNum]; // 20 threads
		for (int i = 0; i < workerNum; ++i) {
			workers[i] = new AddWorker(addable);
		}
		long startTime = System.currentTimeMillis();
		// starting
		for (int i = 0; i < workerNum; ++i) {
			workers[i].start();
		}
		// waiting
		for (int i = 0; i < workerNum; ++i) {
			workers[i].join();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Final value: " + addable.getVal());
		System.out.println("Time cost: " + (endTime-startTime));
	}
	

}
