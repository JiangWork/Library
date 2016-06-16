package com.klatencor.klara.future.object.recipe;

public class AlignmentPointInfo {
	private String lightMode;
	private String magnification;
	private String alignmentType;
	
	public String getLightMode(){
		return this.lightMode;
	}
	
	public void setLightMode(String lightMode){
		this.lightMode = lightMode;
	}
	
	public String getMagnification(){
		return this.magnification;
	}
	
	public void setMagnification(String magnification){
		this.magnification = magnification;
	}
	
	public String getAlignmentType(){
		return this.alignmentType;
	}
	
	public void setAlignmentType(String alignmentType){
		this.alignmentType = alignmentType;
	}
}
