package com.klatencor.klara.future.support;

/**
 * 
 * Convert byte array into different objects.
 *
 * @author jiangzhao
 * @date Jun 29, 2016
 * @version V1.0
 */
public interface BytesConverter<T> {

	/**
	 * The caller use this method to convert the byte array into
	 * desired Objects.
	 * 
	 * @param buffer the byte buffer.
	 * @param fromIndex the index from which to decode.
	 * @return
	 */
	public T to(BytesWapper buf) throws ConversionException;
}
