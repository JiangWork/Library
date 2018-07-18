package demo.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedLockCasComparison {
	
	private int primitiveInt;
	private AtomicInteger atomicInt;
	private Lock lock;
	
	SynchronizedLockCasComparison() {
		primitiveInt = 0;
		atomicInt = new AtomicInteger(0);
		lock = new ReentrantLock();
	}
	
	public synchronized void synchronizedAdd() {
		primitiveInt += 1;
	}
	
	public void lockAdd() {
		lock.lock();
		primitiveInt += 1;
		lock.unlock();
	}
	
	public void casAdd() {
		atomicInt.incrementAndGet();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final SynchronizedLockCasComparison cmp = new SynchronizedLockCasComparison();
		
		Runnable syncRunnable = new Runnable() {
			@Override
			public void run() {
				cmp.synchronizedAdd();
				System.out.println("SYNC: " + System.currentTimeMillis());
			}
		};
		
		Runnable lockRunnable = new Runnable() {
			@Override
			public void run() {
				cmp.lockAdd();
				System.out.println("LOCK: " + System.currentTimeMillis());
			}			
		};
		
		Runnable casRunnable = new Runnable() {
			@Override
			public void run() {
				cmp.casAdd();
				System.out.println("CAS: " + System.currentTimeMillis());
			}			
		};
		int THREADS_NUM = 100;
		System.out.println("Time consuming using sync: " + measureTime(THREADS_NUM, syncRunnable));
		System.out.println("Time consuming using lock: " + measureTime(THREADS_NUM, lockRunnable));
		System.out.println("Time consuming using cas: " + measureTime(THREADS_NUM, casRunnable));
		// start to run
	}
	
	
	public static long measureTime(int num, Runnable runnable) {
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < num; ++i) {
			threads.add(new Thread(runnable));
		}
		long start = System.currentTimeMillis();
		for(Thread th: threads) {
			th.start();
		}
		for (Thread th: threads) {
			try {
				th.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return System.currentTimeMillis() - start;
	}
	
	
	

}
