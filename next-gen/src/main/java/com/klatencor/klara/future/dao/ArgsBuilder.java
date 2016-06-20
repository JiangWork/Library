package com.klatencor.klara.future.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility to help to construct the SQL arguments.
 *
 * @author jiangzhao
 * @date Jun 17, 2016
 * @version V1.0
 */
public class ArgsBuilder {
	
	private List<Object> args;
	
	public ArgsBuilder() {
		args = new ArrayList<Object>();
	}
	
	public ArgsBuilder add(Object arg) {
		args.add(arg == null? "": arg);
		return this;
	}
	
	public ArgsBuilder add(Object... args) {
		for (Object obj: args) {
			this.add(obj);
		}
		return this;
	}
	
	public ArgsBuilder addBoolean(String arg) {
		Boolean bool = Boolean.parseBoolean(arg);
		return this.add(bool.booleanValue());
	}
	
	public ArgsBuilder addShort(String arg) {
		short val = 0;
		try {
			val = Short.parseShort(arg);
		} catch(Exception e) {
			// ignored
		}
		return this.add(val);
	}
	
	public ArgsBuilder addInt(String arg) {
		int val = 0;
		try {
			val = Integer.parseInt(arg);
		} catch(Exception e) {
			// ignored
		}
		return this.add(val);
	
	}
	
	public ArgsBuilder addLong(String arg) {
		long val = 0;
		try {
			val = Long.parseLong(arg);
		} catch(Exception e) {
			// ignored
		}
		return this.add(val);
	
	}
	
	public ArgsBuilder addDouble(String arg) {
		double val = 0;
		try {
			val = Double.parseDouble(arg);
		} catch(Exception e) {
			// ignored
		}
		return this.add(val);
	
	}
	
	public ArgsBuilder set(int index, Object arg) {
		this.args.set(index, arg);
		return this;
	}
	
	public void clear() {
		this.args.clear();
	}
	
	/**
	 * Convert the arguments to Object array.
	 * @return
	 */
	public Object[] buildArray() {
		return args.toArray(new Object[args.size()]);
	}
	
}
