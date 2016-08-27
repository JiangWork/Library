package library.processinvocation;

public class ProcExecutionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String procName;
	private long exceptionTime;
	
	public ProcExecutionException(String msg, String processName) {
		super(msg);
		this.procName = processName;
		this.exceptionTime = System.currentTimeMillis();
	}
	
	public ProcExecutionException(String msg, String processName, Throwable cause) {
		super(msg, cause);
		this.procName = processName;
		this.exceptionTime = System.currentTimeMillis();
	}
	

	public String getProcessName() {
		return procName;
	}

	public void setProcessName(String processName) {
		this.procName = processName;
	}

	public long getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(long exceptionTime) {
		this.exceptionTime = exceptionTime;
	}
}
