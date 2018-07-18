package library.util;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExpiredList<T> {

	private long expiredInterval;
	private long lastUpdated;
	private Node<T> head;
	private Lock lock;

	public ExpiredList(long expiredInterval) {
		this.expiredInterval = expiredInterval;
		this.lastUpdated = -1;
		lock = new ReentrantLock();
	}

	public void add(T value) {
		lock.lock();
		Node<T> node = new Node<T>(value);
		node.next = head;
		head = node;
		if (lastUpdated != -1
				&& System.currentTimeMillis() - lastUpdated >= expiredInterval) {
			clean();
		}
		//Arrays.asList(a)
		lock.unlock();

	}

	public List<T> get() {
		lock.lock();
		try {
			List<T> objects = new ArrayList<T>();
			clean();
			Node<T> current = head;
			while (current != null) {
				objects.add(current.value);
				current = current.next;
			}
			return objects;
		} finally {
			lock.unlock();
		}
	}

	private void clean() {
		long now = System.currentTimeMillis();
		lastUpdated = now;

		if (head == null)
			return;

		if (isExpired(head, now)) {
			head = null;
			return;
		}

		Node<T> prev = null;
		Node<T> current = head;
		while (current != null && !isExpired(current, now)) {
			prev = current;
			current = current.next;
		}
		if (current != null) {
			prev.next = null;
		}

	}

	private boolean isExpired(Node<T> node, long now) {
		return now - node.timestamp > expiredInterval;
	}

	private static class Node<T> {
		private T value;
		private long timestamp;
		private Node<T> next;

		public Node(T value) {
			this.value = value;
			this.timestamp = System.currentTimeMillis();
		}
	}

	
	public static void main(String[] args) throws InterruptedException {
		ExpiredList<String> list = new ExpiredList<String>(1000);
		list.add("Test");
		Thread.sleep(1500);
		System.out.println(list.get());
		list.add("Test1");
		Thread.sleep(500);
		list.add("Test2");
		Thread.sleep(600);
		list.add("Test3");
		System.out.println(list.get());
		list.add("Test4");
		list.add("Test5");
		list.add("Test6");
		Thread.sleep(1005);
		System.out.println(list.get());
	}
}
