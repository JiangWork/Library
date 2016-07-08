package com.klatencor.klara.future.support;

import java.nio.charset.Charset;

/**
 * Convert byte array returned by JNI to {@link String}.
 * 
 * @author jiangzhao
 * @date  Jul 1, 2016
 * @version V1.0
 */
public class StringBytesConverter implements BytesConverter<String>  {

	public final static StringBytesConverter INSTANCE = new StringBytesConverter();
	
	/**
	 * The fromIndex must be from 0.
	 */
	@Override
	public String to(BytesWapper buf) throws ConversionException {
		if(buf == null || buf.getBuffer() == null) {
			throw new ConversionException("null input.");
		}
		if (buf.getPos() != 0) {
			throw new ConversionException("fromIndex must be from 0,  actual:" + buf.getPos());
		}
		return new String(buf.getBuffer(), Charset.forName("UTF-8"));
	}

}
