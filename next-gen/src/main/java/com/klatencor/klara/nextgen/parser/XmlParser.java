package com.klatencor.klara.nextgen.parser;

import java.io.InputStream;

/**
 * 
 * TODO
 * @author jiangzhao
 * @date  May 22, 2016
 * @version V1.0
 * @param <T>
 */
public interface XmlParser <T> {
	
	public T parse(String filePath) throws ParsingException;
}
