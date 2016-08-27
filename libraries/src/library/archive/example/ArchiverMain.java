package library.archive.example;

import java.io.File;

import library.archive.Archiver;

public class ArchiverMain {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: dest-zar file, [file|directory]+");
			return;
		}
		String destFile = args[0];
		Archiver archiver = new Archiver();
		for (int i = 1; i < args.length; ++i) {
			String file = args[i];
			File f = new File(file);
			if (f.isFile()) {
				archiver.addFile(file);
			} else if (f.isDirectory()) {
				archiver.addDirectory(file);
			} else {
				// add although shouldn't
				archiver.addFile(file);
			}
		}
		archiver.archive(destFile);
	}

}
