package library.archive;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import library.archive.ZarHeader.FileInfo;

/**
 * 
 * It provides quick search/extract specific file.
 *
 * @author jiangzhao
 * @date Aug 26, 2016
 * @version V1.0
 */
public class UnArchiver {

	private final static Logger logger = Logger.getLogger(UnArchiver.class);
	private final static int BUFFERSIZE = 1024 * 1024 * 4;
	private byte[] buffer;
	
	private File archFile;
	private ZarHeader header;
	
	public UnArchiver(String file) {
		archFile = new File(file);
	}
	
	/**
	 * Read the header information.
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		if (header != null) return;
		if (!archFile.exists()) {
			throw new IllegalArgumentException("No such zar file: " + archFile.getAbsolutePath());
		}
		header = new ZarHeader();
		DataInputStream dis = new DataInputStream(new FileInputStream(archFile));
		header.readFromStream(dis);
		dis.close();
		buffer = new byte[BUFFERSIZE];
		logger.info(String.format("Read header from %s, %d bytes read.", archFile.getAbsolutePath(), header.headerSize()));
	}
	
	/**
	 * Test if zar file contains specific file.
	 * @param fileName
	 * @return
	 */
	public boolean conatins(String fileName) {
		if (fileName == null || fileName.trim().length() == 0) {
			return false;
		}
		fileName = fileName.trim();
		FileInfo fileInfo = header.findFile(fileName);
		if (fileName.equals(fileInfo.fileName)) {
			return true;
		}
		return false;
	}
	
	/**
	 * List all files in this zar file.
	 * @return
	 */
	public List<String> list() {
		List<String> files = new ArrayList<String>();
		List<FileInfo> fileInfos = header.getFileList();
		for (FileInfo fileInfo: fileInfos) {
			files.add(String.format("%s: %s", fileInfo.fileName, 
					CapacityFormatter.format(fileInfo.fileSize)));
		}
		return files;
	}
	
	/**
	 * Unarchive all files in this zar to destDirectory.
	 * @param destDirectory
	 * @throws IOException 
	 */
	public boolean unArchive(String destDirectory) {
		boolean ret = true;
		try {
			File file = new File(destDirectory);
			if (!file.exists()) {
				if(!file.mkdirs()) {
					throw new IllegalArgumentException("Can make directory: " + destDirectory);
				}
			}
			doUnArchive(destDirectory);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			System.out.println("Error: " + e.getMessage());
			ret = false;
		}
		return ret;
	}
	
	/**
	 * Unarchive specific file to destDirectory.
	 * 
	 * @param destDirectory
	 * @param desiredFile
	 * @throws IOException 
	 */
	public boolean unArchive(String destDirectory, String desiredFile) {
		boolean ret = true;
		try {
			File file = new File(destDirectory);
			if (!file.exists()) {
				if(!file.mkdirs()) {
					throw new IllegalArgumentException("Can make directory: " + destDirectory);
				}
			}
			desiredFile = desiredFile.trim();
			if (!conatins(desiredFile)) {
				throw new IllegalArgumentException("Can't find file in this zar: " + desiredFile);
			}
			doUnArchive(destDirectory, desiredFile);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			System.out.println("Error: " + e.getMessage());
			ret = false;
		}
		return ret;
	}
	
	/**
	 * Unarchive all files in this zar to destDirectory.
	 * @param destDirectory
	 * @throws IOException 
	 */
	private void doUnArchive(String destDirectory) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(archFile, "r");
		for(FileInfo fileInfo: header.getFileList()) {
			doUnArchive(destDirectory, fileInfo.fileName);
		}
		raf.close();
	}
	
	/**
	 * Unarchive specific file to destDirectory.
	 * 
	 * @param destDirectory
	 * @param desiredFile
	 * @throws IOException 
	 */
	private void doUnArchive(String destDirectory, String desiredFile) throws IOException {
		FileInfo fileInfo = header.findFile(desiredFile);
		String msg = "Extracting file: " + desiredFile + ", size " + CapacityFormatter.format(fileInfo.fileSize);
		System.out.print(msg);
		long start = System.currentTimeMillis();
		RandomAccessFile raf = new RandomAccessFile(archFile, "r");
		FileOutputStream fos = new FileOutputStream(new File(desiredFile));
		raf.seek(fileInfo.posInFile);
		long copiedSize = copy(raf, fos, fileInfo.fileSize);
		if (copiedSize != fileInfo.fileSize) {
			throw new IOException(String.format("expected %d, read %d", fileInfo.fileSize, copiedSize));
		}
		raf.close();
		long end = System.currentTimeMillis();
		System.out.println(String.format(" elapsed time: %.3f s.", (end - start)*1.0/1000));
		logger.info(msg + String.format(" elapsed time: %.3f s.", (end - start)*1.0/1000));
	}
	
	public long copy(RandomAccessFile raf, FileOutputStream fos, long size) throws IOException {
		long copiedSize = 0;
		long left = size;
		int shouldRead = (int) (left > BUFFERSIZE ? BUFFERSIZE: left);
		int read = 0;
		while((read = raf.read(buffer, 0, shouldRead)) != -1) {
			fos.write(buffer, 0, read);
			left -= read;
			shouldRead = (int) (left > BUFFERSIZE ? BUFFERSIZE: left);
			copiedSize += read;
			if (shouldRead == 0)	break;
		}
		fos.close();
		return copiedSize;
	}
}
