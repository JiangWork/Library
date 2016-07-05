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
	 * 
	 * @param buffer
	 * @return
	 */
	public T to(byte[] buffer) throws ConversionException;
}
