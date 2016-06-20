package com.klatencor.klara.future.object;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class maps enum index to enum string representation, or vice virsa.
 *
 * @author jiangzhao
 * @date Jun 17, 2016
 * @version V1.0
 */
public class EnumMapping {
	private List<String> values = new ArrayList<String>();
	
	public EnumMapping(String enumsStr) {
		String[] enums = enumsStr.split("#");
		for(String e: enums) {
			values.add(e);
		}
	}
	
	/**
	 * Get the enum index (0-based) by enum string representation.
	 * -1 is returned if no such string representation.
	 * @param enumStr
	 * @return
	 */
	public int getEnumIndex(String enumStr) {
		return values.indexOf(enumStr);
	}
	
	/**
	 * Get the enum string representation at position index.
	 * "" is returned if index is not valid.
	 * @param index
	 * @return
	 */
	public String getEnumString(int index) {
		if(index >=0 && index < values.size()) {
			return values.get(index);
		}
		return "";
	}
}
