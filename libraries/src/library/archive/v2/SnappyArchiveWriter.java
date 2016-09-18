package library.archive.v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * Use Snappy to compress the content of file and write them
 * to output stream.
 * 
 * @author Jiang
 *
 */
public class SnappyArchiveWriter implements ArchiveWriter {
	private static final Logger logger = Logger.getLogger(SnappyArchiveWriter.class);
	
	public static final int DEFAULT_COMPRESS_THRESHOLD = 20 * 1024 * 1024; // 20 MB
	
	private int cThreshold = DEFAULT_COMPRESS_THRESHOLD;
	
	public SnappyArchiveWriter() {
		this(DEFAULT_COMPRESS_THRESHOLD);
	}
	
	public SnappyArchiveWriter(int cThr) {
		this.cThreshold = cThr;
	}
	
	@Override
	public long write(File file, OutputStream os) throws IOException {
		// TODO Auto-generated method stub
		long fileSize = file.length();
		if (fileSize <= cThreshold) {
			logger.debug(String.format("Using IdenticalWriter due to file size %d less than %d", fileSize, cThreshold));
			return new DirectArchiveWriter().write(file, os);
		}
        CountableSnappyOutputStream sos = new CountableSnappyOutputStream(os, BUFFER_SIZE);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int readCount = 0;
        while((readCount = fis.read(buffer)) != -1) {
        	sos.write(buffer, 0, readCount);
        }
        sos.flush();
        //sos.close();  SHOULDN'T close it
        fis.close();
		return sos.getWrittenBytesCount();
	}

}
