package library.archive.v2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MetaData {	
	
	public final static String MAGICCOOKIE = "zar0";
	
	private String magicCookie;
	private int version;
	private long timestamp;
	private int writeMode; // Direct or Snappy, 0 for direct, 1 for snappy
	private int compressThreshold; // COMPRESS_THRESHOLD for Snappy
	private int fileNum;
	private long totalFileSize;  // size of files
	private long fileInfoPos;  // the position of List<FileInfo>
	
	private List<FileInfo> fileList;
	
	public final static int HEADER_SIZE = MAGICCOOKIE.getBytes().length + 3 * 8 + 4 * 4;
	
	public MetaData() {
		fileList = new ArrayList<FileInfo>();
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

	public void addFile(String filePath, long orgSize, long compressedSize, long posInFile) {
		fileList.add(new FileInfo(filePath, orgSize, compressedSize, posInFile));
	}
	
	public void addFileInfo(FileInfo fileInfo) {
		fileList.add(fileInfo);
	}
	
	/**
	 * Write ZarHeader information to stream.
	 * 
	 * @param dos
	 * @throws IOException
	 */
	public void writeHeader(DataOutputStream dos) throws IOException {	
		dos.write(MAGICCOOKIE.getBytes());
		dos.writeInt(version);
		dos.writeLong(timestamp);
		dos.writeInt(writeMode);
		dos.writeInt(compressThreshold);
		dos.writeInt(fileNum);
		dos.writeLong(totalFileSize);
		dos.writeLong(fileInfoPos);
	}
	
	/**
	 * Write FileInfo information to stream.
	 * Return written bytes size.
	 * 
	 * @param dos
	 * @throws IOException
	 */
	public int writeFileInfos(DataOutputStream dos) throws IOException {
		int size = 0;
		for (FileInfo fileInfo: fileList) {
			size += fileInfo.writeToStream(dos);
		}
		return size;
	}
	
	public void readHeader(RandomAccessFile raf) throws IOException {
		byte[] buffer = new byte[4];
		raf.read(buffer);
		magicCookie = new String(buffer);
		if (!magicCookie.equals(MAGICCOOKIE)) {
			throw new IllegalStateException("Not a valid zar file.");
		}
		version = raf.readInt();
		timestamp = raf.readLong();
		writeMode = raf.readInt();
		compressThreshold = raf.readInt();
		fileNum = raf.readInt();
		totalFileSize = raf.readLong();
		fileInfoPos = raf.readLong();
	}
	
	public void readFileInfos(RandomAccessFile raf) throws IOException {
		fileList.clear();
		raf.seek(fileInfoPos);
		for (int i = 0; i < fileNum; ++i) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.readFromStream(raf);
			fileList.add(fileInfo);
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public FileInfo findFile(String filePath) {
		if (filePath == null) return null;
		for (FileInfo fileInfo: fileList) {
			if (filePath.equals(fileInfo.filePath)) {
				return fileInfo.copy();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public List<FileInfo> findFiles(String fileName) {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		if (fileName == null) return fileInfos;
		for (FileInfo fileInfo: fileList) {
			if (fileInfo.filePath.endsWith(fileName)) {
				fileInfos.add(fileInfo.copy());
			}
		}
		return fileInfos;
	}
	
	
	/**
	 * Using UTF-8 encoding
	 * @param os
	 * @param outStr
	 * @throws IOException
	 */
	private static void writeString(DataOutputStream os, String outStr) throws IOException {
		if(outStr == null) {
			outStr = "";
		}
		byte[] bytes = outStr.getBytes("UTF-8");
		os.writeInt(bytes.length);
		os.write(bytes);
	}
	
//	private String readString(DataInputStream is) throws IOException {
//		int length = is.readInt();
//		byte[] buffer = new byte[length];
//		int read = is.read(buffer);
//		if (read != length) {
//			throw new IOException(String.format("Unexpected end of stream in reading String: desired %d, read %d bytes.",
//					length, read));
//		}
//		return new String(buffer, Charset.forName("UTF-8"));
//	}
	
	private static String readString(RandomAccessFile raf) throws IOException {
		int length = raf.readInt();
		byte[] buffer = new byte[length];
		int read = raf.read(buffer);
		if (read != length) {
			throw new IOException(String.format("Unexpected end of stream in reading String: desired %d, read %d bytes.",
					length, read));
		}
		return new String(buffer, Charset.forName("UTF-8"));
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getWriteMode() {
		return writeMode;
	}

	public void setWriteMode(int writeMode) {
		this.writeMode = writeMode;
	}

	public long getFileInfoPos() {
		return fileInfoPos;
	}
	
	public void setFileInfoPos(long fileInfoPos) {
		this.fileInfoPos = fileInfoPos;
	}
	

	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	public int getCompressThreshold() {
		return compressThreshold;
	}


	public void setCompressThreshold(int compressThreshold) {
		this.compressThreshold = compressThreshold;
	}


	public static class FileInfo {
		public String filePath = "";  // the relative file path
		public long ofs; // original file size
		public long wfs; // written file size, maybe Snappyed.
		
		/**start pos in zar file**/
		long posInFile;
		
		public FileInfo() {
			
		}
		
		public FileInfo(String filePath, long fileSize) {
			this.filePath = filePath;
			this.ofs = fileSize;
		}
		
		public FileInfo(String filePath, long ofs, long wfs, long posInFile) {
			this.filePath = filePath;
			this.ofs = ofs;
			this.wfs = wfs;
			this.posInFile = posInFile;
		}
		
		public int writeToStream(DataOutputStream os) throws IOException {
			writeString(os, filePath);
			os.writeLong(ofs);
			os.writeLong(wfs);
			os.writeLong(posInFile);
			return getOccupiedSize();
		}
		
		public void readFromStream(RandomAccessFile raf) throws IOException {
			filePath = readString(raf);
			ofs = raf.readLong();
			wfs = raf.readLong();
			posInFile = raf.readLong();
		}
		
		public int getOccupiedSize() {
			try {
				return 4 + filePath.getBytes("UTF-8").length + 3*8;
			} catch (UnsupportedEncodingException e) {
				return 0;
			}
		}
		
		public FileInfo copy() {
			return new FileInfo(this.filePath, this.ofs, this.wfs, this.posInFile);
		}
	}


}
