package demo.misc;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link PositionedThreadPool} is a {@link ThreadPoolExecutor} with functionalities: query the positions of {@link Runnable}.
 *
 *
 * @author jiangzhao
 * @date Jan 23, 2017
 * @version V1.0
 */
public class PositionedThreadPool extends ThreadPoolExecutor {
	
	public PositionedThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> blockingQueue) {
		super(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, blockingQueue);
	}

	/**
	 * Get the position of specific Runnable.
	 * @param runnable
	 * @return pos starts from 1, -1 if not found.
	 */
	public int getPosition(Runnable runnable) {
		if (runnable == null) {
			throw new IllegalArgumentException("Can't be null.");
		}
		Runnable[] runnables = getQueue().toArray(new Runnable[0]);
		for (int i = 0; i < runnables.length; ++i) {
			if (runnable.equals(runnables[i])) return ++i; 
		}
		return -1;
	}
	
	public static class Task implements Runnable {
		private int id;
		
		public Task(int id) {
			this.id = id;
		}
		
		@Override
		public void run() {
			System.out.println("A time-consuming task: " + id);
			try {
				Thread.sleep(1000 + new Random().nextInt(1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public int hashCode() {
			return System.identityHashCode(this) * 31 + id;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Task) {
				return this.id == ((Task)obj).id;
			}
			return false;
		}
		
	}
	public static void main(String[] args) throws InterruptedException {
		PositionedThreadPool pool = new PositionedThreadPool(20, 30, new ArrayBlockingQueue<Runnable>(100));
		Task[] tasks = new Task[100];
		for (int i = 0; i < tasks.length; ++i) {
			tasks[i] = new Task(i);
			pool.execute(tasks[i]);
		}
		
		System.out.println(pool.getPosition(tasks[10]));
		System.out.println(pool.getPosition(tasks[40]));
		for(;;) {
			Thread.sleep(10);
			int pos = pool.getPosition(tasks[tasks.length-1]);
			System.out.println("task 99 pos:" + pos);
			if (pos == -1) break;			
		}
		pool.shutdown();
		
	}

}
