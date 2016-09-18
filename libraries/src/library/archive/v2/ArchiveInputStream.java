package library.archive.v2;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * A wrapper of {@link RandomAccessFile}, provides function to read certain size of data.
 * 
 * 
 * @author Jiang
 *
 */
public class ArchiveInputStream extends InputStream {

	private RandomAccessFile raf;
	private long size;
	private long readBytesCount = 0;


	public ArchiveInputStream(RandomAccessFile raf, long size) {
		this.raf = raf;
		this.size = size;
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		throw new IOException("Oops, this should never happed.");
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		if (readBytesCount >= size) {
			return -1;
		}
		int byteSlots = Math.min(len, b.length - off);
		int shouldRead = (int)Math.min((long)byteSlots, (size - readBytesCount));
		int bytesNum = raf.read(b, off, shouldRead);
		readBytesCount += bytesNum;
		return bytesNum;
	}
	
	public void setPos(long newPos) throws IOException {
		raf.seek(newPos);
		readBytesCount = 0;
	}
	
	public void resetSize(long size) {
		this.size = size;
		readBytesCount = 0;
	}
	
	/**
	 * Do nothing.
	 */
	public void close() {
		
	}

	
}
