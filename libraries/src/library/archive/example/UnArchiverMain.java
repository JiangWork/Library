package library.archive.example;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import library.archive.CapacityFormatter;
import library.archive.v2.MetaData.FileInfo;
import library.archive.v2.UnArchiver;


public class UnArchiverMain {

	private final static Logger logger = Logger.getLogger(UnArchiverMain.class);
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		if (args.length < 2) {
			System.err.println("Usage: zar-file [option], where option can be following:"
					+ "\n\tlist -list all files in this zar file."
					+ "\n\tunarchive dest-dir [files]* - unarchive specific files. If not specific, all will be extracted.");
			return;
		}
		UnArchiver unArchiver = new UnArchiver(args[0]);
		try {
			unArchiver.init();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			logger.error(e.getMessage(), e);
			return;
		}
		if("list".equals(args[1])) {
			List<FileInfo> files = unArchiver.list();
			int count = files.size();
			for (int i = 0; i < files.size(); ++i) {
				System.out.println(String.format("[%d of %d] %s %s.", (i+1), count, files.get(i).filePath, 
						CapacityFormatter.format(files.get(i).ofs)));
			}
		} else if("unarchive".equals(args[1])) {
			if (args.length == 3) {
				unArchiver.unArchive(args[2]);
			} else {
				for (int i = 3; i < args.length; ++i) {
					unArchiver.unArchive(args[2], args[i]);
				}
			}
		}
	}

}
