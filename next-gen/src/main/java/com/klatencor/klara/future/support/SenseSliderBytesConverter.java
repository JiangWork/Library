package com.klatencor.klara.future.support;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.thrift.common.TSenseSlider;
import com.klatencor.klara.future.utils.IOUtils;
import com.klatencor.klara.future.utils.StringUtils;

public class SenseSliderBytesConverter implements BytesConverter<TSenseSlider> {

	private final static Logger logger = Logger.getLogger(SenseSliderBytesConverter.class);
	public final static SenseSliderBytesConverter INSTANCE = new SenseSliderBytesConverter();
	
	@Override
	public TSenseSlider to(BytesWapper buf) throws ConversionException {
		if(buf == null || buf.getBuffer() == null) {
			throw new ConversionException("null input.");
		}
		TSenseSlider ss = new TSenseSlider();
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buf.getBuffer()));
		try {
			dis.skipBytes(buf.getPos());
		} catch (IOException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		int bytesRead = 0;
		try {			
			byte[] nameBytes = new byte[dis.readInt()];
			bytesRead += 4;
			dis.read(nameBytes);
			bytesRead += nameBytes.length;
			ss.setName(StringBytesConverter.INSTANCE.to(new BytesWapper(nameBytes)));
			ss.setType(dis.readInt());
			bytesRead += 4;
			ss.setFValue(dis.readFloat());
			bytesRead += 4;
			ss.setFMin(dis.readFloat());
			bytesRead += 4;
			ss.setFMax(dis.readFloat());
			bytesRead += 4;
			ss.setValue(dis.readInt());
			bytesRead += 4;
			ss.setMin(dis.readInt());
			bytesRead += 4;
			ss.setMax(dis.readInt());
			bytesRead += 4;
			logger.debug("Read senseslider: " + StringUtils.printProperties(ss));
		} catch (IOException e) {
			throw new ConversionException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(dis);
			buf.movePos(bytesRead);
		}
		return ss;
	}

}
