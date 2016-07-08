package com.klatencor.klara.future.utils;

import java.lang.reflect.Field;

public class StringUtils {
	
	private final static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
	
	public static String toHex(byte val) {
		return HEX_CHARS[(val & 0xf0) >> 4] + "" + HEX_CHARS[(val & 0x0f)] ;
	}
	
	public static String toHex(byte[] vals) {
		StringBuilder sb = new StringBuilder();
		for (byte val: vals) {
			sb.append(" " + toHex(val));
		}
		return sb.toString();
	}
	
	/**
	 * Print the primitive or String properties in the objects.
	 * Each property occupys a line contains: name=value.
	 * @return
	 */
	public static String printProperties(Object obj) {
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field: fields) {
			field.setAccessible(true);
			Class<?> typeClazz = field.getType();
			if(typeClazz.isPrimitive() || typeClazz == String.class) {
				try {
					sb.append(field.getName() + "=" + field.get(obj).toString());
				} catch (Exception e) {
					//ignored
				}
			}
		}
		return sb.toString();
	}
}
