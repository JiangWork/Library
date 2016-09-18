package library.archive.v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Directly write the content of file to destination output stream.
 * @author Jiang
 *
 */
public class DirectArchiveWriter implements ArchiveWriter {
	
	@Override
	public long write(File file, OutputStream os) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		long totalReadCnt = 0;
		int readCnt = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		while((readCnt = fis.read(buffer)) != -1) {
			totalReadCnt += readCnt;
			os.write(buffer, 0, readCnt);
		}
		fis.close();
		return totalReadCnt;
	}

}
