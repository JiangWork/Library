package library.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class StringUtils {

	public static boolean isEmpty(String value) {
		return org.apache.commons.lang.StringUtils.isEmpty(value);
	}
	
	public static String stringifyException(Exception e) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		e.printStackTrace(new PrintWriter(new OutputStreamWriter(bos)));
		return new String(bos.toByteArray());
	}
}
