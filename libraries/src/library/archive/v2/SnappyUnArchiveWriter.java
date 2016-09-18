package library.archive.v2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;
import org.xerial.snappy.SnappyInputStream;

public class SnappyUnArchiveWriter implements UnArchiveWriter {

	private static final Logger logger = Logger.getLogger(SnappyUnArchiveWriter.class);
	
    private static final int DEFAULT_COMPRESS_THRESHOLD = 20 * 1024 * 1024; // 20 MB
	
	private int cThreshold = DEFAULT_COMPRESS_THRESHOLD;
	
	private ArchiveInputStream ais;
	
	private DirectUnArchiveWriter iWriter;
	
	public SnappyUnArchiveWriter(RandomAccessFile raf) {
		this(DEFAULT_COMPRESS_THRESHOLD, raf);
	}
	
	public SnappyUnArchiveWriter(int cThr, RandomAccessFile raf) {
		this.cThreshold = cThr;
		this.ais = new ArchiveInputStream(raf, 0);
		iWriter = new DirectUnArchiveWriter(raf);
	}
	
	@Override
	public long write(OutputStream os, long size) throws IOException {
		if (cThreshold >= size) {
			logger.debug(String.format("Using IdenticalUnArchiveWriter due to file size %d less than %d", size, cThreshold));
			return iWriter.write(os, size);
		}
		ais.resetSize(size);
		SnappyInputStream sis = new SnappyInputStream(ais);
		byte[] buffer = new byte[BUFFER_SIZE];
		long bytesReadCount = 0;
        int readCount = 0;
        while((readCount = sis.read(buffer, 0, BUFFER_SIZE)) != -1) {
        	os.write(buffer, 0, readCount);
        	bytesReadCount += readCount;
        }
        sis.close();
        os.close();
		return bytesReadCount;
	}

	@Override
	public void setPos(long newPos) throws IOException {
		ais.setPos(newPos);
	}

}
