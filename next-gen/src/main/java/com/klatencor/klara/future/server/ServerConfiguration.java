package com.klatencor.klara.future.server;

public final class ServerConfiguration {
	
	public static final String APPLICATION_NAME = Server.getContext().getServerName();
	
	public static final String WORKING_DIRECTORY = "/kla/klaS/future";
	
	public static final String STORAGE_DIRECTORY = "/kla/klaS/future";
	
	public static final String LOG_DIRECTORY = "klaL/logs/future";
	
	public static final boolean SHOULD_REPORT = true;
	
	public static final int KEEP_MESSAGE_SIZE = Integer.MAX_VALUE;
	
	public static final String APPLICATION_LOCATION = System.getProperty("APPLOCATION", "/opt/KLA90serv/java/future");
}
