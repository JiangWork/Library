package library.archive;

public class CapacityFormatter {

	private final static String[] UNITS = {"Bytes", "KB", "MB", "GB", "TB", "PB"};
	
	public static String format(long size) {
		if (size < 1024) {
			return size + "Bytes";
		}
		double formattedSize = getSize(size, 0);
		int unit = getUnit(size, 0);
		return String.format("%.3f%s", formattedSize, UNITS[unit]);
	}
	
    // B KB MB GB TB PB
	private static double getSize(double total, int unit) {
		if (unit >= UNITS.length || total < 1024) {
			return total;
		} else {
			return getSize(total/1024.0, ++unit);
		}
	}
	
	private static int getUnit(double total, int unit) {
		if(unit >= UNITS.length || total < 1024) {
			return unit;
		} else {
			return getUnit(total/1024.0, ++unit);
		}
	}
}
