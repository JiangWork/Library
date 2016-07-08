package com.klatencor.klara.future.server.metrics;

import java.util.Stack;

/**
 * Provide a utility to calculate the elapsed time.
 * The implemention is based on {@link Stack} wherein 
 * a push operation indicates the begin of timing
 * and pop operation will get the elapsed time and pop
 * the element in the Stack.
 * 
 * @author jiangzhao
 * @date  Jun 19, 2016
 * @version V1.0
 */
public class ElapsedTimer {
	
	private Stack<Long> stack;
	
	public ElapsedTimer() {
		stack = new Stack<Long>();
	}
	
	/**
	 * Record the current time {@code eventNumver} times
	 * to indicate {@code eventNumver} events begin.
	 * {@code eventNumver} time will be pushed into Stack.
	 * @param eventNumber
	 */
	public void mark(int eventNumber) {
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < eventNumber; ++i) {
			stack.push(currentTime);
		}
	}
	
	public void mark() {
		this.mark(1);
	}
	
	/**
	 * Get the elapsed time in millisecond through substracting top element in Stack
	 * from current time.
	 * 
	 * @return elapsed time. 
	 */
	public long unmark() {
		long time = stack.pop();
		return System.currentTimeMillis() - time;
	}

}
