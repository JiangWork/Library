package library.archive;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The zar header format
 * 
 *
 * @author jiangzhao
 * @date Aug 26, 2016
 * @version V1.0
 */
public class ZarHeader {
	
	public final static String MAGICCOOKIE = "zar0";
	
	private String magicCookie;
	private String zarFileName;
	private long timestamp;
	private int fileNum;
	private long totalFileSize;  // size of files
	private List<FileInfo> fileList;
	
	private int headerSize;
	
	public ZarHeader() {
		fileList = new ArrayList<FileInfo>();
	}
	
	public String getZarFileName() {
		return zarFileName;
	}

	public void setZarFileName(String zarFileName) {
		this.zarFileName = zarFileName;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public long getTotalFileSize() {
		return totalFileSize;
	}

	public void setTotalFileSize(long totalFileSize) {
		this.totalFileSize = totalFileSize;
	}

	public List<FileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileInfo> fileList) {
		this.fileList = fileList;
	}

	public void addFile(String fileName, long fileSize, long posWithoutHeader) {
		fileList.add(new FileInfo(fileName, fileSize, posWithoutHeader));
	}
	
	/**
	 * Count the Header size and modify file's {@code posInFile} value.
	 * @throws IOException
	 */
	public void gather() throws IOException {
		timestamp = System.currentTimeMillis();
		if (fileNum != fileList.size()) {
			throw new IllegalStateException(String.format("file size found: %d, actual: %d.", 
					fileList.size(), fileNum));
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		writeToStream(dos);
		headerSize = bos.size();
		for(FileInfo fileInfo: fileList) {
			fileInfo.posInFile += headerSize;
		}
	}
	
	/**
	 * Write ZarHeader information to stream.
	 * <b>Caution:</b> call {@link #gather} method first to correct the file pos.
	 * @param dos
	 * @throws IOException
	 */
	public void writeToStream(DataOutputStream dos) throws IOException {
		dos.write(MAGICCOOKIE.getBytes());
		writeString(dos, zarFileName);
		dos.writeLong(timestamp);
		dos.writeInt(fileNum);
		dos.writeLong(totalFileSize);
		for (FileInfo fileInfo: fileList) {
			fileInfo.writeToStream(dos);
		}
	}
	
	public void readFromStream(DataInputStream dis) throws IOException {
		byte[] buffer = new byte[4];
		dis.read(buffer);
		magicCookie = new String(buffer);
		if (!magicCookie.equals(MAGICCOOKIE)) {
			throw new IllegalStateException("Not a valid zar file");
		}
		zarFileName = readString(dis);
		timestamp = dis.readLong();
		fileNum = dis.readInt();
		totalFileSize = dis.readLong();
		headerSize = 4 + 4 + zarFileName.getBytes().length
				+ 8 + 4 + 8;
		for (int i = 0; i < fileNum; ++i) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.readFromStream(dis);
			fileList.add(fileInfo);
			headerSize += fileInfo.getClassSize();
		}
		
	}
	
	public FileInfo findFile(String fileName) {
		if (fileName == null) return new FileInfo();
		for (FileInfo fileInfo: fileList) {
			if (fileName.equals(fileInfo.fileName)) {
				return fileInfo.copy();
			}
		}
		return new FileInfo();
	}
	
	private void writeString(DataOutputStream os, String outStr) throws IOException {
		if(outStr == null) {
			outStr = "";
		}
		os.writeInt(outStr.length());
		os.write(outStr.getBytes());
	}
	
	private String readString(DataInputStream is) throws IOException {
		int length = is.readInt();
		byte[] buffer = new byte[length];
		int read = is.read(buffer);
		if (read != length) {
			throw new IOException(String.format("Unexpected end of stream: desired %d, read %d bytes.",
					length, read));
		}
		return new String(buffer);
	}
	
	public int headerSize() {
		return this.headerSize;
	}
	
	public class FileInfo {
		String fileName = "";
		long fileSize;
		/**start pos in zar file**/
		long posInFile;
		
		public FileInfo() {
			
		}
		
		public FileInfo(String fileName, long fileSize, long posInFile) {
			this.fileName = fileName;
			this.fileSize = fileSize;
			this.posInFile = posInFile;
		}
		
		public void writeToStream(DataOutputStream os) throws IOException {
			writeString(os, fileName);
			os.writeLong(fileSize);
			os.writeLong(posInFile);
		}
		
		public void readFromStream(DataInputStream is) throws IOException {
			fileName = readString(is);
			fileSize = is.readLong();
			posInFile = is.readLong();
		}
		
		public int getClassSize() {
			return 4 + fileName.getBytes().length + 8 + 8;
		}
		
		public FileInfo copy() {
			return new FileInfo(this.fileName, this.fileSize, this.posInFile);
		}
	}
}
