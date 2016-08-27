package library.archive;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * Like tar command, this class provide functions to 
 * package multiple files into a single zar file.
 * 
 * 
 * @author jiangzhao
 * @date Aug 26, 2016
 * @version V1.0
 */
public class Archiver {
	
	private final static Logger logger = Logger.getLogger(Archiver.class);
	private final static int BUFFERSIZE = 1024 * 1024 * 4;
	
	private List<String> fileNames;
	private List<FileInfo> fileInfos;
	private long totalFilesSize = 0;
	
	public Archiver() {
		fileNames = new ArrayList<String>();
		fileInfos = new ArrayList<FileInfo>();
	}
	
	/**
	 * Add a file to archive.
	 * @param fileName
	 */
	public void addFile(String fileName) {
		if (!fileNames.contains(fileName) && fileName != null) {
			fileNames.add(fileName);
		}
	}
	
	/**
	 * Add files which are directly under this directory. 
	 * @param directoryName
	 */
	public void addDirectory(String directoryName) {
		File directory = new File(directoryName);
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file: files) {
				if (file.isFile()) {
					addFile(directoryName + File.separator + file.getName());
				}
			}
		}
	}
	
	private boolean checkFiles() {
		fileInfos.clear();
		totalFilesSize = 0;
		int success = 0, fail = 0;
		int index = 0;
		for (String fileName: fileNames) {
			File file = new File(fileName);
			if (file.exists()) {
				fileInfos.add(new FileInfo(file, index ++, totalFilesSize));
				totalFilesSize += file.length();
				++success;
			} else {
				++fail;
				logger.warn(String.format("File %s is unaccessable.", fileName));
			}
		}
		logger.info(String.format("Processing %d files.", success));
		return success == 0? fail == 0 : true;
	}
	
	
	public boolean archive(String archiveName) {
		if (!archiveName.endsWith(".zar")) {
			archiveName += ".zar";
		}
		boolean ret = true;
		long start = System.currentTimeMillis();
		try {
			doArchive(archiveName);
		} catch (Exception e) {
			System.err.println("Error was found: " + e.getMessage());
			logger.error(e.getMessage(), e);
			// clean up
			File file = new File(archiveName);
			if (file.exists()) {
				file.delete();
			}
			ret  = false;
		}
		long end = System.currentTimeMillis();
		if(ret) {
			File file = new File(archiveName);
			String msg = String.format("Generated archvie file: %s, elapsed time: %.3f s, size: %s.", 
					file.getAbsolutePath(), (end - start)*1.0/1000, CapacityFormatter.format(file.length()));
			logger.info(msg);
			System.out.println(msg);
		}
		return ret;
	}
	
	private void doArchive(String archiveName) throws IOException {
		if (!checkFiles()) {
			throw new IllegalStateException("No valid files were found.");
		}
		System.out.println(String.format("Archiving files to %s, %d files, %s in total.", 
				archiveName, fileInfos.size(), CapacityFormatter.format(totalFilesSize)));
		ZarHeader header = new ZarHeader();
		header.setFileNum(fileInfos.size());
		header.setTotalFileSize(totalFilesSize);
		header.setZarFileName(archiveName);
		for (FileInfo fileInfo: fileInfos) {
			header.addFile(fileInfo.file.getName(), 
					fileInfo.file.length(), fileInfo.cSize);
		}
		header.gather();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(archiveName));
		header.writeToStream(dos);
		logger.info(String.format("Writing header done [%d bytes written]", header.headerSize()));
		System.out.println(String.format("Writing header done [%d bytes written]", header.headerSize()));
		int count = 0;
		int num = fileInfos.size();
		for (FileInfo fileInfo: fileInfos) {
			long start = System.currentTimeMillis();
			System.out.print(String.format("[%d of %d] Archiving file {%s} ", 
					++count, num, fileInfo.file.getName()));
			long copiedSize = copy(fileInfo.file, dos);
			if (copiedSize != fileInfo.fileSize) {
				throw new IllegalStateException("File " + fileInfo.file.getName() + " is staled.");
			}
			long end = System.currentTimeMillis();
			System.out.println(String.format(" elapsed time: %.3f s.", (end - start)*1.0/1000));
			logger.info(String.format("Archiving file {%s} [%d of %d] elapsed time: %.3f s.", 
					fileInfo.file.getName(), count, num, (end - start)*1.0/1000));
		}
		dos.close();
	}
	
	private long copy(File from, DataOutputStream dos) throws IOException {
		long copiedSize = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(from);
			byte[] buffer = new byte[BUFFERSIZE];
			int read = 0;
			while((read = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, read);
				copiedSize += read;
			}
		} catch(IOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return copiedSize;
	}
	
	public static class FileInfo {
		File file;
		/** the position in the archive **/
		int index;
		/** the cumulative size of preceding files **/
		long cSize;
		
		long fileSize;
		
		public FileInfo(File file, int index, long size) {
			this.file = file;
			this.index = index;
			this.cSize = size;
			this.fileSize = file.length();
		}
	}
}
