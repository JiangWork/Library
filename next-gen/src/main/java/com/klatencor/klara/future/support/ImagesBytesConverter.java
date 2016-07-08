package com.klatencor.klara.future.support;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.klatencor.klara.future.support.object.Image;
import com.klatencor.klara.future.utils.IOUtils;

/**
 * Convert byte array returned by JNI to a list of {@link com.klatencor.klara.future.support.object.Image}.
 * 
 * @author jiangzhao
 * @date  Jul 1, 2016
 * @version V1.0
 */
public class ImagesBytesConverter implements BytesConverter<List<Image>>{
	private final static Logger logger = Logger.getLogger(ImagesBytesConverter.class);
	
	public final static ImagesBytesConverter INSTANCE = new ImagesBytesConverter();
	
	/**Not used for now**/
	private int version = 1;
	
	private boolean needImageData = true;
	

	
	@Override
	public List<Image> to(BytesWapper buf) throws ConversionException {
		if(buf == null || buf.getBuffer() == null) {
			throw new ConversionException("null input.");
		}
		List<Image> images = to(buf.getBuffer(), buf.getPos());
		// no need to update Position now
		return images;
	}
	

	public List<Image> to(byte[] buffer, int fromIndex) throws ConversionException {
		if(buffer == null) {
			throw new ConversionException("null input.");
		}
		// TODO Auto-generated method stub
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer));
		try {
			dis.skipBytes(fromIndex);
		} catch (IOException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		List<Image> images = new ArrayList<Image>();
		try {
			int imageCount = dis.readInt();
			logger.debug("Image count:" + imageCount);
			for (int  i =0; i < imageCount; ++i) {
				Image image = new Image();
				image.setHeight(dis.readInt());
				image.setWidth(dis.readInt());
				byte[] descBytes = new byte[dis.readInt()];
				dis.read(descBytes);
				image.setDescription(StringBytesConverter.INSTANCE.to(new BytesWapper(descBytes)));
				int imageDataBytes = image.getHeight() * image.getWidth();
				if (needImageData) {
					byte[] imageData = new byte[imageDataBytes];
					dis.read(imageData);
					image.setImageData(imageData);
				} else {  // skip
					dis.skipBytes(imageDataBytes);
				}
				image.populateDesc();
				images.add(image);
				logger.debug("Read image " + i + ": " + image.toString());
			}
			if(dis.available() != 0) {
				logger.error("Error: more bytes are available.");
			}
		} catch (IOException e) {
			throw new ConversionException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(dis);
		}
		return images;
	}
	
	

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isNeedImageData() {
		return needImageData;
	}

	public void setNeedImageData(boolean needImageData) {
		this.needImageData = needImageData;
	}





}
