package org.smartframework.oxm;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

public class SimpleBeanTest {

	public static void main(String[] args) throws MarshalException, ValidationException, IOException, MappingException {
		// TODO Auto-generated method stub
		SimpleBean sb = new SimpleBean();
		sb.setNative(true);
		sb.setName("simplebean");
		sb.setNumber(10);
		List<String> list = new ArrayList<String>();
		list.add("Here.");
		list.add("There.");
		sb.setValues(list);
		Mapping map = new Mapping();
		map.loadMapping("mapping.xml");	
		File file = new File("test.xml");
		Writer writer = new FileWriter(file);
		Marshaller marshaller = new Marshaller(writer);
		marshaller.setMapping(map);
		marshaller.marshal(sb);
		
		File readfile = new File("test.xml");
		Reader reader = new FileReader(readfile);
		Unmarshaller unmarshaller = new Unmarshaller(map);
		
		SimpleBean read = (SimpleBean)unmarshaller.unmarshal(reader);
		
		System.out.println(read);
	}

}
