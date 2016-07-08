package com.klatencor.klara.future.support;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.thrift.common.TSenseSlider;
import com.klatencor.klara.future.utils.IOUtils;

public class SenseSlidersBytesConverter implements BytesConverter<List<TSenseSlider>> {
	private final static Logger logger = Logger.getLogger(SenseSlidersBytesConverter.class);
	
	public final static SenseSlidersBytesConverter INSTANCE = new SenseSlidersBytesConverter();
	@Override
	public List<TSenseSlider> to(BytesWapper buf) throws ConversionException {
		if(buf == null || buf.getBuffer() == null) {
			throw new ConversionException("null input.");
		}
		List<TSenseSlider> ssList = new ArrayList<TSenseSlider>();
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buf.getBuffer()));
		try {
			dis.skipBytes(buf.getPos());
		} catch (IOException e) {
			throw new ConversionException(e.getMessage(), e);
		}	
		try {
			int ssCount = dis.readInt();
			buf.movePos(4);
			logger.debug("Sense slider number: " + ssCount);
			for (int i = 0; i < ssCount; ++i) {
				ssList.add(SenseSliderBytesConverter.INSTANCE.to(buf));
			}
			if (buf.getPos() < buf.getBuffer().length) {
				logger.error("Error: more bytes are available.");
			}
		} catch (IOException e) {
			throw new ConversionException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(dis);
		}	
		return ssList;
	}

}
