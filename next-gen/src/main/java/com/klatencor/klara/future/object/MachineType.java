package com.klatencor.klara.future.object;


public class MachineType {
	// The various machine types supported
	public static final int STORE_STATUS_ENUM = 1;
	public final static short ALL = 0;
	public final static short TWO_XX = 2;
	public final static short THREE_XX = 3;
	public final static short SLX = 4;
	public final static short FIVE_XX = 5;
	public final static short WAFER = 6;
	public final static short SIX_XX = 7;
	
	public static short getMachineType(int recipeVersion){
		if (recipeVersion >= Version.V210
				&& recipeVersion < Version.V600) {
			return FIVE_XX;
		}
		if (recipeVersion >= Version.V600) {
			return SIX_XX;
		}
		return ALL;
	}
}
