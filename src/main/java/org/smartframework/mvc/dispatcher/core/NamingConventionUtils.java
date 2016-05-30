package org.smartframework.mvc.dispatcher.core;

import org.apache.commons.lang3.StringUtils;

/**
 * Define some utilities to convert among different names.
 * @author Miller
 * @date Mar 18, 2016 9:45:33 PM
 */
public class NamingConventionUtils {

	/**
	 * Convert a controller name to url string.
	 * Like: OrderBookController to /order/book String
	 * @param controllerName
	 * @return
	 */
	public static String toURL(String ctrlName) {
		if (!ctrlName.endsWith(Constants.CONTROLLER_SUFFIX)) {
			throw new ConversionException("Not a valid controller name:" + ctrlName);
		}
		StringBuilder sb = new StringBuilder();
		String str = StringUtils.removeEnd(ctrlName, Constants.CONTROLLER_SUFFIX);
		String normal = str.substring(0, 1).toUpperCase()
				+ str.substring(1);
		char[] chars = normal.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			if (Character.isUpperCase(chars[i])) {
				sb.append('/');
				sb.append(Character.toLowerCase(chars[i]));
			} else {
				sb.append(chars[i]);
			}
		}
		return sb.toString();
	}
}
