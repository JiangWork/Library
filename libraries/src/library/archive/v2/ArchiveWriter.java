/**
 * 
 */
package library.archive.v2;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A ArchiveWriter writes content of a file to output destination.
 * 
 * A normal writer writes identical bytes to destination.
 * 
 * A compressed writer can write compressed the bytes to destination in order to save storage space.
 * 
 * @author Jiang
 *
 */
public interface ArchiveWriter {
	
	public static final int BUFFER_SIZE = 5 * 1024 * 1024; // 5 MB

	/**
	 * Write the content of file to OutputStream.
	 * Return the number of written bytes.
	 * 
	 * <p>The OutputStream should be not closed after being invoked.
	 * 
	 * @param file a file.
	 * @param os OutputStream.
	 * @return 
	 * @throws IOException
	 */
	public long write(File file, OutputStream os) throws IOException;
}
