package library.common;

public class PIDExperiment {

	public static void main(String[] args) {
		while(true) {
		    String pid = System.getProperty("PID");
		    System.out.println(pid);
		    try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
