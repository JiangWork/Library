package org.smartframework.mvc.dispatcher.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A primitive converter.
 * 
 * @author Miller
 * @date Mar 17, 2016 9:16:06 PM
 */
public class PrimitiveConverter {
	
	private static BooleanConverter booleanConverter = new BooleanConverter();
	private static ShortConverter shortConverter = new ShortConverter();
	private static IntegerConverter integerConverter = new IntegerConverter();
	private static LongConverter longConverter = new LongConverter();
	private static FloatConverter floatConverter = new FloatConverter();
	private static DoubleConverter doubleConverter = new DoubleConverter();
	private static ListConverter listConverter = new ListConverter();
	private static MapConverter mapConverter = new MapConverter();
	
	private static Map<Class<?>, Converter<?>> converterRegistery = new HashMap<Class<?>, Converter<?>>();
	
	static {
		converterRegistery.put(Boolean.class, booleanConverter);
		converterRegistery.put(Short.class, shortConverter);
		converterRegistery.put(Integer.class, integerConverter);
		converterRegistery.put(Long.class, longConverter);
		converterRegistery.put(Float.class, floatConverter);
		converterRegistery.put(Double.class, doubleConverter);
		converterRegistery.put(List.class, listConverter);
		converterRegistery.put(Map.class, mapConverter);
		converterRegistery.put(boolean.class, booleanConverter);
		converterRegistery.put(short.class, shortConverter);
		converterRegistery.put(int.class, integerConverter);
		converterRegistery.put(long.class, longConverter);
		converterRegistery.put(float.class, floatConverter);
		converterRegistery.put(double.class, doubleConverter);
	}
	
	public static Object convert(String value, Class<?> clazz) {
		if (clazz == String.class) {
			return value;
		} 
		if (converterRegistery.containsKey(clazz)) {
			return converterRegistery.get(clazz).convert(value);
		}
		throw new ConversionException(String.format("can't convert value %s to %s ", 
				value, clazz.getName()));
	}
	
	public static String stringify(Object value) {
		Class<?> clazz = value.getClass();
		if (clazz == String.class) {
			return value.toString();
		} 
		if (clazz == Boolean.class) {
			return stringifyBoolean((Boolean) value);
		} 
		if (clazz == Short.class) {
			return stringifyShort((Short)value);
		}
		if (clazz == Integer.class) {
			return stringifyInt((Integer)value);
		}
		if (clazz == Long.class) {
			return stringifyLong((Long)value);
		}
		if (clazz == Float.class) {
			return stringifyFloat((Float)value);
		}
		if (clazz == Double.class) {
			return stringifyDouble((Double)value);
		}
		if (List.class.isAssignableFrom(clazz)) {
			return stringifyList((List)value);
		}
		if (Map.class.isAssignableFrom(clazz)) {
			return stringifyMap((Map)value);
		}
		throw new ConversionException("Can't stringify " + value.getClass().getName());
	}
	
	private static String stringifyInt(Integer value) {
		return value.toString();
	}

	public static boolean toBoolean(String value) {
		return booleanConverter.convert(value).booleanValue();
	}
	
	public static String stringifyBoolean(boolean value) {
		return booleanConverter.stringify(Boolean.valueOf(value));
	}
	
	public static short toShort(String value) {
		return shortConverter.convert(value).shortValue();
	}
	
	public static String stringifyShort(short value) {
		return shortConverter.stringify(Short.valueOf(value));
	}
	
	public static int toInt(String value) {
		return integerConverter.convert(value).intValue();
	}
	
	public static String stringifyShort(int value) {
		return integerConverter.stringify(Integer.valueOf(value));
	}
	
	public static long toLong(String value) {
		return longConverter.convert(value).longValue();
	}
	
	public static String stringifyLong(long value) {
		return longConverter.stringify(Long.valueOf(value));
	}
	
	public static float toFloat(String value) {
		return floatConverter.convert(value).floatValue();
	}
	
	public static String stringifyFloat(float value) {
		return floatConverter.stringify(Float.valueOf(value));
	}
	
	public static double toDouble(String value) {
		return doubleConverter.convert(value).doubleValue();
	}
	
	public static String stringifyDouble(double value) {
		return doubleConverter.stringify(value);
	}
	
	public static List<String> toList(String value) {
		return listConverter.convert(value);
	}
	
	public static String stringifyList(List<String> value) {
		return listConverter.stringify(value);
	}
	
	public static Map<String, String> toMap(String value) {
		return mapConverter.convert(value);
	}
	
	public static String stringifyMap(Map<String, String> value) {
		return mapConverter.stringify(value);
	}
	
	public static class BooleanConverter implements Converter<Boolean> {

		@Override
		public Boolean convert(String value) {
			return Boolean.valueOf("true".equalsIgnoreCase(value));
		}

		@Override
		public String stringify(Boolean obj) {
			return obj.toString();
		}
		
	}
	
	public static class ShortConverter implements Converter<Short> {

		@Override
		public Short convert(String value) {
			try {
				return Short.valueOf(value);
			} catch(NumberFormatException e) {
				throw new ConversionException(e.getMessage());
			}
		}

		@Override
		public String stringify(Short obj) {
			return obj.toString();
		}
		
	}
	
	public static class IntegerConverter implements Converter<Integer> {

		@Override
		public Integer convert(String value) {
			try {
				return Integer.valueOf(value);
			} catch(NumberFormatException e) {
				throw new ConversionException(e.getMessage());
			}
		}

		@Override
		public String stringify(Integer obj) {
			return obj.toString();
		}		
	}
	
	public static class LongConverter implements Converter<Long> {

		@Override
		public Long convert(String value) {
			try {
				return Long.valueOf(value);
			} catch(NumberFormatException e) {
				throw new ConversionException(e.getMessage());
			}
		}

		@Override
		public String stringify(Long obj) {
			return obj.toString();
		}		
	}
	
	public static class FloatConverter implements Converter<Float> {

		@Override
		public Float convert(String value) {
			try {
				return Float.valueOf(value);
			} catch(NumberFormatException e) {
				throw new ConversionException(e.getMessage());
			}
		}

		@Override
		public String stringify(Float obj) {
			return obj.toString();
		}		
	}
	
	public static class DoubleConverter implements Converter<Double> {

		@Override
		public Double convert(String value) {
			try {
				return Double.valueOf(value);
			} catch(NumberFormatException e) {
				throw new ConversionException(e.getMessage());
			}
		}

		@Override
		public String stringify(Double obj) {
			return obj.toString();
		}		
	}
	
	public static class ListConverter implements Converter<List<String>> {

		@Override
		public List<String> convert(String value) {
			List<String> list = new ArrayList<String>();
			if(value == null || value.isEmpty()) {
				return list;
			}
			String[] fields = value.split(",");
			for (String field: fields) {
				list.add(field);
			}
			return list;
		}

		@Override
		public String stringify(List<String> obj) {
			StringBuilder sb = new StringBuilder();
			for (String value: obj) {
				sb.append(',');
				sb.append(value);
			}
			return sb.length() == 0 ? "" : sb.toString().substring(1);
		}
		
	}
	
	public static class MapConverter implements Converter<Map<String, String>> {

		@Override
		public Map<String, String> convert(String value) {
			Map<String, String> map = new HashMap<String, String>();
			if(value == null || value.isEmpty()) {
				return map;
			}
			String[] fields = value.split(",");
			for (String field: fields) {
				String[] kv = field.split(":");
				if (kv.length != 2) {
					throw new ConversionException("Corrupt key-value pair: " + field);
				}
				map.put(kv[0], kv[1]);
			}
			return map;
		}

		@Override
		public String stringify(Map<String, String> obj) {
			Iterator<Entry<String, String>> iter = obj.entrySet().iterator();
			StringBuilder sb = new StringBuilder();
			while(iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				sb.append(',');
				sb.append(entry.getKey());
				sb.append(':');
				sb.append(entry.getValue());
			}
			return sb.length() == 0 ? "" : sb.toString().substring(1);
		}
		
	}
}
