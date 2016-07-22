package library.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StringUtils {

	public static boolean isEmpty(String value) {
		return value.trim().isEmpty();
	}
	
	public static String stringifyException(Exception e) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		e.printStackTrace(new PrintWriter(new OutputStreamWriter(bos)));
		return new String(bos.toByteArray());
	}
	
	/**
	 * Print the primitive or String properties in the object.
	 * Each property is formatted as: name=value.
	 * @return
	 */
	public static String propertiesInfo(Object obj) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(obj + ":");
	    Class<?> clazz = obj.getClass();
	    Field[] fields = clazz.getDeclaredFields();
	    for (Field field: fields) {
	        if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
	            continue;
	        }   
	        field.setAccessible(true);
	        Class<?> typeClazz = field.getType();
	        if(typeClazz.isPrimitive() || typeClazz == String.class) {
	            try {
	                sb.append(field.getName() + "=" + field.get(obj).toString());
	                sb.append(" "); 
	            } catch (Exception e) {
	                //ignored
	            }   
	        }   
	    }   
	    return sb.toString();
	}
}
