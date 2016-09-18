package library.archive.v2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.log4j.Logger;

import library.archive.CapacityFormatter;
import library.archive.v2.MetaData.FileInfo;


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

	private File archFile;
	private MetaData metaData;
	private UnArchiveWriter unArchiveWriter;
	
	public UnArchiver(String file) {
		archFile = new File(file);
	}
	
	/**
	 * Read the header information.
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		if (metaData != null) return;
		if (!archFile.exists()) {
			throw new IllegalArgumentException("No such zar file: " + archFile.getAbsolutePath());
		}
		metaData = new MetaData();
		RandomAccessFile raf = new RandomAccessFile(archFile, "r");
		metaData.readHeader(raf);
		metaData.readFileInfos(raf);
		if (metaData.getWriteMode() == 0) {
			unArchiveWriter = new DirectUnArchiveWriter(raf);
		} else if (metaData.getWriteMode() == 1) {
			unArchiveWriter = new SnappyUnArchiveWriter(metaData.getCompressThreshold(), raf);
		} else {
			throw new IOException("Invalid write mode, actual " + metaData.getWriteMode() +" expect 0 or 1");
		}
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
		return metaData.findFiles(fileName).size() != 0;
	}
	
	/**
	 * List all files in this zar file.
	 * @return
	 */
	public List<FileInfo> list() {
		List<FileInfo> fileInfos = metaData.getFileList();
		return fileInfos;
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
			List<FileInfo> fileInfos = metaData.findFiles(desiredFile);
			if (fileInfos.isEmpty()) {
				throw new IllegalArgumentException("Can't find file in this zar: " + desiredFile);
			}
			for (FileInfo fileInfo: fileInfos) {
				System.out.println("Found: " + fileInfo.filePath);
			}
			System.out.println("Hint: " + fileInfos.get(0).filePath + " will be extracted.");
			doUnArchive(destDirectory, fileInfos.get(0));
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
		for(FileInfo fileInfo: metaData.getFileList()) {
			doUnArchive(destDirectory, fileInfo);
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
	private void doUnArchive(String destDirectory, FileInfo desiredFile) throws IOException {
		long start = System.currentTimeMillis();
		String msg = "Extracting to " + destDirectory + File.separator + desiredFile.filePath + ", size " + desiredFile.ofs;
		System.out.print(msg);
		String fileDest  = destDirectory + File.separator + desiredFile.filePath;
		File destFile = new File(fileDest);
		File parentFile = destFile.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		unArchiveWriter.setPos(desiredFile.posInFile);
		long byteWrittenCount = unArchiveWriter.write(new DataOutputStream(new FileOutputStream(fileDest)), desiredFile.wfs);
		if (byteWrittenCount != desiredFile.ofs) {
			throw new IOException(String.format("file is corrputted, bytes written: %d, expected: %d", byteWrittenCount, desiredFile.ofs));
		}
		long end = System.currentTimeMillis();
		System.out.println(String.format(" elapsed time: %.3f s.", (end - start)*1.0/1000));
		logger.info(msg + String.format(" elapsed time: %.3f s.", (end - start)*1.0/1000));
	}
}
