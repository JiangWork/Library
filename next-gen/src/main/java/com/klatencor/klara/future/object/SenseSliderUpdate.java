package com.klatencor.klara.future.object;

/**
 * This class is used to update the sense values in recipes.
 * 
 * @author jiangzhao
 * @date  Jun 30, 2016
 * @version V1.0
 */
public class SenseSliderUpdate implements Comparable<SenseSliderUpdate> {
	
	private int iaIndex;
	private int senseIndex;
	private String newValue;
	
	public int getIaIndex() {
		return iaIndex;
	}
	public void setIaIndex(int iaIndex) {
		this.iaIndex = iaIndex;
	}
	public int getSenseIndex() {
		return senseIndex;
	}
	public void setSenseIndex(int senseIndex) {
		this.senseIndex = senseIndex;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	@Override
	public int compareTo(SenseSliderUpdate another) {
		// TODO Auto-generated method stub
		return iaIndex == another.iaIndex ? (senseIndex - another.senseIndex) 
				: (iaIndex - another.iaIndex);
	}
	
}
