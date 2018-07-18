package library.play;


public abstract class AbstractTask implements Runnable {
	public static final int NOT_INITIALIED = -1;
	protected int id = NOT_INITIALIED;
	protected int pos = NOT_INITIALIED;  // position in the queue
	protected Throwable err;
	protected long startTime;
	protected long endTime;
	
	public AbstractTask(int id) {
		this.id = id;
	}		
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractTask) {
			return this.id == ((AbstractTask)obj).id;
		}
		return false;
	}
	
	public synchronized int getPos() {
		return pos;
	}
	
	public synchronized void setPos(int pos) {
		this.pos = pos;
	}		
	
}