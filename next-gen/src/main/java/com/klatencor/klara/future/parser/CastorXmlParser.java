package com.klatencor.klara.future.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;

/**
 * 
 * A XML parser using {@link org.exolab.castor} library.
 *
 * @author jiangzhao
 * @date Jun 1, 2016
 * @version V1.0
 */
public class CastorXmlParser<T> implements XmlParser<T> {

	private static final Logger logger = Logger.getLogger(CastorXmlParser.class);
	
	private String mappingFile;
	
	public CastorXmlParser() {
		this(null);
	}
	
	public CastorXmlParser(String mappingFile) {
		this.mappingFile = mappingFile;
	}
	
	@Override
	public T parse(String xmlPath) throws ParsingException {
		long startTime = System.currentTimeMillis();
		try {
			T object = parseInternal(xmlPath);
			long endTime = System.currentTimeMillis();
			logger.info(String.format("%s parsed, elapsed time %d ms.", xmlPath, 
					(endTime-startTime)));
			return object;
		} catch (ParsingException e) {
			throw e;
		} 
	}

	
	public T parseInternal(String xmlPath) throws ParsingException {
		if (xmlPath == null) {
			throw new ParsingException("input xml is null.");
		}
		File file = new File(xmlPath);
		if (!file.canRead()) {
			throw new ParsingException("can't read file: " + xmlPath);
		}
		Reader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// ignored, shouldn't happen
		}
		logger.info(String.format("Parsing file %s using mapping file %s.", 
				xmlPath, mappingFile));
		Unmarshaller unmarshaller = new Unmarshaller();
		unmarshaller.setIgnoreExtraAttributes(true);
		unmarshaller.setIgnoreExtraElements(true);
		if (mappingFile != null) {
			Mapping map = new Mapping();
			try {
				map.loadMapping(mappingFile);
				unmarshaller.setMapping(map);
			} catch (Exception e) {  // re-throw
				throw new ParsingException(e.getMessage(), e);
			}
		}
		try {
			@SuppressWarnings("unchecked")
			T result = (T) unmarshaller.unmarshal(reader);
			return result;
		} catch (Exception e) {
			throw new ParsingException(e.getMessage(), e);
		}
	
	}
}
