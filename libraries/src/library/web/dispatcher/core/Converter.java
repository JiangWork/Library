package library.web.dispatcher.core;

/**
 * A interface to convert String to desired object, or vice versa.
 * @author Miller
 * @date Mar 17, 2016 9:14:41 PM
 * @param <T>
 */
public interface Converter<T> {
	/**
	 * Convert the input string into typed object.
	 * 
	 * @param value the input string.
	 * @return the desired object.
	 */
	public T convert(String value);
	
	
	/**
	 * Convert the object into string form.
	 * @param obj the input object.
	 * @return the string representation.
	 */
	public String stringify(T obj);
}
