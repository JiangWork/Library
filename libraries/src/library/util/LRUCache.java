package library.util;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A LRU cache implementation.
 * 
 * @author jiangzhao
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V>{

	private int maxSize = 20;
	private Map<K, EntryReference<K, V>> backingMap = new HashMap<K, EntryReference<K,V>>();
    
	// A linked list, the node ordered by the access timestamp
	// (Latest)->...-> (oldest)
	private EntryReference<K, V> head;
	private EntryReference<K, V> tail;
	
	
	public LRUCache(int maxSize) {
		this.maxSize = maxSize;
		backingMap = new HashMap<K, EntryReference<K,V>>(maxSize);
		head = null;
		tail = null;
	}
	
	public void put(K key, V value) {
		if(backingMap.containsKey(key)) {
			EntryReference<K, V> kv = backingMap.get(key);
			kv.setValue(value);
			kv.setTimestamp(System.currentTimeMillis());
			adjustLink(kv);
			return;
		} 
		EntryReference<K, V> kv = new EntryReference<K, V>(key, value);
		if(size() == 0) {
			head = kv;
			tail = kv;
			backingMap.put(key, kv);
			return;
		}
		if(size() >= maxSize) {
			// remove the last entry
			System.out.println("Removing " + tail);
			backingMap.remove(tail.getKey());
			tail.previous.next = null;
			tail = tail.previous;
		}
		head.previous = kv;
		kv.next = head;
		head = kv;
		backingMap.put(key, kv);
	}
	
	/**
	 * Get the value of certain key.
	 * Null if no such key.
	 * 
	 * <p>
	 * Modify the access timestamp and adjust the internal linked list.
	 * </p>
	 * @param key
	 * @return
	 */
	public V get(K key) {
		if(!backingMap.containsKey(key)) {
			return null;
		}
		EntryReference<K, V> kv = backingMap.get(key);
		kv.setTimestamp(System.currentTimeMillis());
		adjustLink(kv);
		return kv.getValue();
	}
	
	/**
	 * Adjust the linked list: put the in-parameter node at the first.
	 * @param kv
	 */
	public void adjustLink(EntryReference<K, V> kv) {
		if(kv == head) {  // no need to adjust
			return;
		}
		kv.previous.next = kv.next;
		if(kv.next != null) {
			kv.next.previous = kv.previous;
		}
		head.previous = kv;
		kv.previous = null;
		kv.next = head;
		head = kv;
	}
	
	public int size() {
		return backingMap.size();
	}
	
	public void printInOrder() {
		System.out.println("Total size: " + size());
		EntryReference<K, V> node =  head;
		while(node != null) {
			System.out.println(node);
			node = node.next;
		}
	}
	
	private static class EntryReference<K, V> {
		private K key;
		private V value;
		
		// last access time
		private long timestamp;
		
		private EntryReference<K, V> previous;
		private EntryReference<K, V> next;
		
		public EntryReference(K key, V value) {
			this.key = key;
			this.value = value;
			timestamp = System.currentTimeMillis();
		}
		
		public K getKey() {
			return key;
		}
		public void setKey(K key) {
			this.key = key;
		}
		public V getValue() {
			return value;
		}
		public void setValue(V value) {
			this.value = value;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		public EntryReference<K, V> getNext() {
			return next;
		}
		public void setNext(EntryReference<K, V> next) {
			this.next = next;
		}
		public EntryReference<K, V> getPrevious() {
			return previous;
		}
		public void setPrevious(EntryReference<K, V> previous) {
			this.previous = previous;
		}
		public String toString() {
			return String.format("key: %s, value: %s, timstamp: %s.", key, value, new Date(timestamp));
		}
	}
	
	public static void putWithLatency(LRUCache<String, String> cache, String key, String value) {
		try {
			//System.out.println(new Random().nextInt());
			Thread.sleep(Math.abs(new Random().nextInt())%5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cache.put(key, value);
	}
	public static void main(String[] args) {
		LRUCache<String, String> cache = new LRUCache<String, String>(2);
		putWithLatency(cache, "key1", "value1");
		putWithLatency(cache, "key1", "value2");
		putWithLatency(cache, "key2", "value2");
		cache.printInOrder();
		System.out.println("---------------------------------------------");
		putWithLatency(cache, "key3", "value3");
		cache.printInOrder();
		System.out.println("---------------------------------------------");
	}
}
