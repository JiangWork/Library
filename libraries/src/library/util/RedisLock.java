package library.util;


import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import redis.clients.jedis.Jedis;

/**
 * A lock utilize Redis to coordinate multiple machines.
 * 
 * @author jiangzhao
 *
 */
public class RedisLock {
	private static final String LOCK_SUCCES = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";
	private static final Long RELEASE_SUCCESS = 1L;
	
	private String name;
	private Jedis jedis;
	private String requestId; // the request id
	int expiredInSeconds;
	
	public RedisLock(String name, Jedis jedis) {
		this(name, jedis, Integer.MAX_VALUE);
	}

	/**
	 * @param name the lock name
	 * @param expire If meets the expire time, Redis will remove this key.
	 */
	public RedisLock(String name, Jedis jedis, int expire) {
		this.expiredInSeconds = expire;
		this.name = name;
		this.jedis = jedis;
		
	}

	/**
	 * Test if the lock is locked or not.
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean isLocked() {
		boolean ret = jedis.exists(getLockName());
		return ret;
	}

	

	/**
	 * Try to obtain the lock.
	 *<p> If the lock is locked by others, it will return false.
	 *<p> If the lock has already locked by the caller, return true.
	 *<p> Otherwise try to obtain the lock.
	 * 
	 * @return true if lock is obtained, otherwise false;
	 * @throws IOException
	 */
	public boolean obtain() {
		if(isOwned()) return true;
		if(isLocked()) return false;
		// try to obtain the lock
		String lockName = getLockName();
		requestId = UUID.randomUUID().toString();
		String ret = jedis.set(lockName, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expiredInSeconds);
		if(LOCK_SUCCES.equalsIgnoreCase(ret)) {
			return true;
		} else {
			requestId = null;
			return false;
		}
	}

	
	public boolean isOwned() {
		return requestId != null && requestId.equals(getRemoteLockId());
	}
	
	private String getRemoteLockId() {
		String lockName = getLockName();
		String id = jedis.get(lockName);
		return id;
	}

	/**
	 * Release the lock if hold by the caller.
	 * @return true if and only if caller hold the lock and release successfully.
	 * 
	 */
	public boolean release() {
		if(!isOwned()) {
			return false;
		}
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(getLockName()), Collections.singletonList(requestId));
		return RELEASE_SUCCESS.equals(result);
	}


	public String getName() {
		return name;
	}
	
	private String getLockName() {
		return name + ".lock";
	}
	
//	public static void main(String[] args) {
//		Runnable runnable = new Runnable() {
//
//			@Override
//			public void run() {
//				RedisConnector connector = new RedisConnector();
//				Jedis jedis = connector.createConnection().getShard("test");
//				RedisLock lock = new RedisLock("test", jedis);
//				System.out.println(Thread.currentThread() + " isLocked: " + lock.isLocked());
//				System.out.println(Thread.currentThread() + " isOwned: " + lock.isOwned());
//				System.out.println(Thread.currentThread() + " Release: " + lock.release());
//				System.out.println(Thread.currentThread() + " Obtain: " + lock.obtain());
//				System.out.println(Thread.currentThread() + " Obtain: " + lock.obtain());
//				System.out.println(Thread.currentThread() + " isLocked: " + lock.isLocked());
//				System.out.println(Thread.currentThread() + " isOwned: " + lock.isOwned());
//				System.out.println(Thread.currentThread() + " Release: " + lock.release());
//				System.out.println(Thread.currentThread() + " Release: " + lock.release());
//				System.out.println(Thread.currentThread() + " Obtain: " + lock.obtain());
//				System.out.println(Thread.currentThread() + " Release: " + lock.release());
//			}
//			
//		};
//		new Thread(runnable).start();
//		new Thread(runnable).start();
//	}

}
