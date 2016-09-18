package library.archive.v2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * A UnArchiveWriter extracts parts of data from archive file 
 * and writes them to output destination.
 * 
 * @author Jiang
 *
 */
public interface UnArchiveWriter {

	public static final int BUFFER_SIZE = 4 * 1024 * 1024; //4 MB
	
	/**
	 * Read a size number of data from  {@link RandomAccessFile} and write them to {@link OutputStream}.
	 * <p><b>Notice</b> that {@link #setPos} should be called before.</p>
	 * 
	 * <p><b>Notice</b> that {@link OutputStream} is closed after this call.</p>
	 * 
	 * @param os
	 * @param size
	 * @return the bytes actually written to OutputStream.
	 */
	public long write(OutputStream os, long size) throws IOException;
	
	/**
	 * Set the position of {@link RandomAccessFile} as {@code newPos}.
	 * 
	 * 
	 * @param newPos
	 */
	public void setPos(long newPos) throws IOException;
}
