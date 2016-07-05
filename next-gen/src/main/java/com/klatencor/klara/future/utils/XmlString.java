package com.klatencor.klara.future.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * A simple utility to parse and generate Xml String.
 *
 * @author jiangzhao
 * @date Jun 27, 2016
 * @version V1.0
 */
public class XmlString {

	private Map<String, String> paramMap;

	public XmlString() {
		
	}
	
	public XmlString(String xml) {
		this.parseXml(xml, true);
	}
	
	public void clear() {
		getParamMap().clear();
	}
	
	private Map<String, String> getParamMap() {
		if (paramMap == null) {
			paramMap = new LinkedHashMap<String,String>();
		}
		return paramMap;
	}
	
	public void putParameter(String key, String value) {
		getParamMap().put(key, value);
	}
	
	public void putParameter(String key, boolean value) {
		getParamMap().put(key, String.valueOf(value));
	}
	
	public void putParameter(String key, byte value) {
		getParamMap().put(key, String.valueOf(value));
	}
	
	public void putParameter(String key, short value) {
		getParamMap().put(key, String.valueOf(value));
	}
	
	public void putParameter(String key, int value) {
		getParamMap().put(key, String.valueOf(value));
	}
	
	public void putParameter(String key, long value) {
		getParamMap().put(key, String.valueOf(value));
	}
	
	public void putParameter(String key, double value) {
		getParamMap().put(key, String.valueOf(value));
	}
	
	public void putParameter(String key, Object value) {
		getParamMap().put(key, String.valueOf(value));
	}
	
	public void removeParameter(String key) {
		getParamMap().remove(key);
	}
	
	public String getParameter(String key) {
		return getParamMap().get(key);
	}
	
	public String getParameter(String key, String defaultValue) {
		String val = getParameter(key);
		if (val == null || val.length() == 0) {
			return defaultValue;
		}
		return val;
	}
	
	public Map<String, String> parseXmlAfterClear(String parameters) {
		return parseXml(parameters, true);
		
	}
	
	public static Map<String, String> parseXml(String parameters) {
		XmlString pU = new XmlString();
		pU.parseXml(parameters, true);
		return pU.getParamMap();
	}
	
	public Map<String, String> parseXml(String parameters, boolean needClear) {
		Map<String, String> map = getParamMap();
		if (needClear) {
			map.clear();
		}
		if (parameters != null) {
			String key, value;
			int pos = 0;
			while(true) {
				int beginTagL = parameters.indexOf("<", pos);
				int beginTagR = parameters.indexOf(">", beginTagL + 1);
				if (beginTagL == -1 || beginTagR == -1)	break;
				key = parameters.substring(beginTagL + 1, beginTagR);
				int endTag = parameters.indexOf("</" + key + ">", beginTagR + 1);
				if (endTag == -1)	break;
				value = parameters.substring(beginTagR + 1, endTag);
				map.put(key, value);
				pos = endTag + ("</" + key + ">").length();				
			}
		}
		return getParamMap();
	}
	
	
	public String generateXml() {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, String>> iterator = getParamMap().entrySet().iterator();
		String key, value;
		while(iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>)iterator.next();
			key = (String) entry.getKey();
			value = (String) entry.getValue();
			sb.append("<" + key + ">");
			sb.append(value);
			sb.append("</" + key + ">");
		}
		return sb.toString();
	}
	
	public static String getValue(String inStr, String key, String defaultValue, boolean trim) {
		String startFlag = "<" + key +">";
		String endFlag = "</" + key +">";
		int start = inStr.indexOf(startFlag);
		int end = inStr.indexOf(endFlag, start + startFlag.length());
		if (start >= 0  && end > start) {
			String value = inStr.substring(startFlag.length(), end);
			if (trim) {
				return value.trim();
			} else {
				return value;
			}
		}
		return defaultValue;
	}
}
