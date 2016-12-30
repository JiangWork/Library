package library.archive.v2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import library.archive.CapacityFormatter;

/**
 * 
 * Like tar command, this class provides functionalities to 
 * package multiple files into a single zar file. 
 * Notice that the user can choose to perform compression or not.
 * 
 * @author jiangzhao
 * @date Aug 26, 2016
 * @version V1.0
 */
public class Archiver {
	
	private final static Logger logger = Logger.getLogger(Archiver.class);
	private final String currentDirectory = new File("").getAbsolutePath();
	
	private List<File> files;
	
	private MetaData metaData;
	private long totalOFS = 0;  // the original size of files
	private long totalCFS = 0; // the size of files written to file
	private long bytesWrittenCount = 0;
	private int writeMode = 0; // 0 for direct, 1 for snappy
	private int compressThreshold = SnappyArchiveWriter.DEFAULT_COMPRESS_THRESHOLD;
	
	private ArchiveWriter writer;
	
	public Archiver() {
		files = new ArrayList<File>();
		metaData = new MetaData();
	}
	
	/**
	 * Add a file to archive.
	 * @param fileName
	 */
	public void addFile(String fileName) {
		if (fileName != null) {
			files.add(new File(fileName));
		}
	}
	/**
	 * Add files recursively.
	 * 
	 * @param directoryOrFile
	 */
	public void add(File directoryOrFile) {
		//File file = new File(directoryOrFile);
		if (directoryOrFile.isFile()) {
			files.add(directoryOrFile);
		} else if (directoryOrFile.isDirectory()) {
			File[] files = directoryOrFile.listFiles();
			for (File file: files) {
				add(file);
			}
		} else {
			logger.info("No such file path: " + directoryOrFile.getAbsolutePath());
		}
	}
	
	/**
	 * Add files recursively.
	 * 
	 * @param directoryOrFile
	 */
	public void add(String directoryOrFile) {
		this.add(new File(directoryOrFile));
	}
	
	
	private boolean checkFiles() {
		List<File> filteredFiles = new ArrayList<File>();
		totalOFS = 0;
		totalCFS = 0;
		int success = 0, fail = 0;
		for (File file: files) {
			if (file.exists()) {
				totalOFS += file.length();
				++success;
				filteredFiles.add(file);
			} else {
				++fail;
				System.out.println(String.format("File %s is unaccessable, ignored.", file.getAbsolutePath()));
				logger.warn(String.format("File %s is unaccessable, ignored.", file.getAbsolutePath()));
			}
		}
		logger.info(String.format("Processing %d files.", success));
		files.clear();
		files.addAll(filteredFiles);
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
			String msg = String.format("Generated archvie file: %s, elapsed time: %.3fs, size: (in:%s,out:%s), cratio: %.2f, speed: %s/s.", 
					file.getAbsolutePath(), (end - start)*1.0/1000, CapacityFormatter.format(totalOFS),
					CapacityFormatter.format(bytesWrittenCount), totalCFS*1.0/totalOFS, 
					CapacityFormatter.format((long)(totalOFS * 1000.0/(end - start))));
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
				archiveName, files.size(), CapacityFormatter.format(totalOFS)));
		metaData.setCompressThreshold(this.compressThreshold);
		metaData.setFileNum(files.size());
		metaData.setTimestamp(System.currentTimeMillis());
		metaData.setVersion(1);
		metaData.setWriteMode(this.writeMode);
		metaData.setTotalFileSize(this.totalOFS);
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(archiveName)); 
		metaData.writeHeader(dos);
		logger.info(String.format("Writing header done [%d bytes written]", MetaData.HEADER_SIZE));
		System.out.println(String.format("Writing header done [%d bytes written]", MetaData.HEADER_SIZE));
		bytesWrittenCount += MetaData.HEADER_SIZE;
		if (writeMode == 0 ) {
			writer = new DirectArchiveWriter();
		} else if (writeMode == 1) {
			writer = new SnappyArchiveWriter(this.compressThreshold);
		}
		int count = 0;
		int num = files.size();
		for (File file: files) {
			MetaData.FileInfo fileInfo = archiveFile(file, dos, ++count, num);
			metaData.addFileInfo(fileInfo);
		}
		long fileInfosStartPos = bytesWrittenCount;
		bytesWrittenCount += metaData.writeFileInfos(dos);
		dos.close();
		// rewrite the fileInfosStartPos
		RandomAccessFile raf = new RandomAccessFile(archiveName, "rw");
		raf.seek(MetaData.HEADER_SIZE - 8);
		raf.writeLong(fileInfosStartPos);
		raf.close();
	}
	
	private MetaData.FileInfo archiveFile(File file, DataOutputStream dos, int count, int num) throws IOException {
		long start = System.currentTimeMillis();
		String filePath = file.getAbsolutePath();
		if (filePath.startsWith(currentDirectory)) {
			filePath = filePath.substring(currentDirectory.length());
		}
		if (filePath.startsWith(File.separator)) {
			filePath = filePath.substring(1);
		}
		System.out.print(String.format("[%d of %d] Archiving file {%s} ... ", 
				count, num, filePath));
		long ofs = file.length();
		long wfs = writer.write(file, dos);
		
		MetaData.FileInfo fileInfo = new MetaData.FileInfo(filePath, ofs, wfs, bytesWrittenCount);
		bytesWrittenCount += wfs;
		totalCFS += wfs;
		long end = System.currentTimeMillis();
		System.out.println(String.format(" elapsed time: %.3fs, (%d, %d, %.3f)", (end - start)*1.0/1000, ofs, wfs, 1.0*wfs/ofs));
		logger.info(String.format("[%d of %d] Archiving file {%s},  elapsed time: %.3fs,  (%d, %d, %.1f) ", 
				count, num, filePath, (end - start)*1.0/1000,
				ofs, wfs, 1.0*ofs/wfs));
		return fileInfo;
	}
	
	public int getWriteMode() {
		return writeMode;
	}

	/**
	 * 0 for directly writing, 1 for Snappy writing.
	 * @param writeMode
	 */
	public void setWriteMode(int writeMode) {
		this.writeMode = writeMode;
	}

	public int getCompressThreshold() {
		return compressThreshold;
	}

	public void setCompressThreshold(int compressThreshold) {
		this.compressThreshold = compressThreshold;
	}
	
}
