package library.archive.example;

import library.archive.v2.Archiver;


public class ArchiverMain {

	
	/**
	 * java -cp library.jar:log4j-1.2.17.jar:snappy-java-1.1.2.1.jar library.archive.example.ArchiverMain test.zar snappy-java-1.1.2.1.jar  /Users/Miller/scripts
	 * @param args
	 */
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: zar-name [file|directory]+ [options], option can be one of following: ");
			System.err.println("\t-c\t\t use Snappy to compress the file.");
			System.err.println("\t-t value\t the file size threshold for compressing, in bytes.");
			return;
		}
		String destFile = args[0];
		Archiver archiver = new Archiver();
		archiver.setWriteMode(0);
		for (int i = 1; i < args.length; ++i) {
			if (args[i].equals("-c")) {
				archiver.setWriteMode(1);
			} else if (args[i].equals("-t")) {
				archiver.setCompressThreshold(Integer.parseInt(args[++i]));
			} else {
				archiver.add(args[i]);
			}
		}
		archiver.archive(destFile);
	}

}
