package com.klatencor.klara.future.support;

import java.nio.charset.Charset;

public class StringBytesConverter implements BytesConverter<String>  {

	@Override
	public String to(byte[] buffer) throws ConversionException {
		return new String(buffer, Charset.forName("UTF-8"));
	}

}
