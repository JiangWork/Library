package com.klatencor.klara.future.object.recipe;

public class SenseRegion {

	private String type;
	private Point origin;
	private Point regionCount;
	private Point size;
	private Point pitch;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Point getOrigin() {
		return origin;
	}
	public void setOrigin(Point origin) {
		this.origin = origin;
	}
	public Point getRegionCount() {
		return regionCount;
	}
	public void setRegionCount(Point regionCount) {
		this.regionCount = regionCount;
	}
	public Point getSize() {
		return size;
	}
	public void setSize(Point size) {
		this.size = size;
	}
	public Point getPitch() {
		return pitch;
	}
	public void setPitch(Point pitch) {
		this.pitch = pitch;
	}
}
