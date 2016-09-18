package library.archive.v2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class DirectUnArchiveWriter implements UnArchiveWriter {

	private ArchiveInputStream ais;
	
	
	public DirectUnArchiveWriter(RandomAccessFile raf) {
		this.ais = new ArchiveInputStream(raf, 0);
	}
	
	@Override
	public long write(OutputStream os, long size) throws IOException {
		ais.resetSize(size);
		byte[] buffer = new byte[BUFFER_SIZE];
		long bytesReadCount = 0;
		int bytesNum = 0;
		while((bytesNum = ais.read(buffer, 0, BUFFER_SIZE)) != -1) {
			os.write(buffer, 0, bytesNum);
			bytesReadCount += bytesNum;
		}
		os.close();
		return bytesReadCount;
	}

	@Override
	public void setPos(long newPos) throws IOException {
		// TODO Auto-generated method stub
		 ais.setPos(newPos);
	}

}
