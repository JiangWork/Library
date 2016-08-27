package library.memorymonitor.example;

import library.memorymonitor.MemoryMonitor;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MemoryMonitor monitor = new MemoryMonitor();
		monitor.add(14501);
		monitor.setAutoPurge(true);
		monitor.start();
		
		//
		monitor.getPidStatus(14501);
	}

}
