package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class StarLightCalibration {

	private String method;
	private Point pixelSize;
	private List<Point> starCalPoints;
	private String result;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Point getPixelSize() {
		return pixelSize;
	}
	public void setPixelSize(Point pixelSize) {
		this.pixelSize = pixelSize;
	}
	public List<Point> getStarCalPoints() {
		return starCalPoints;
	}
	public void setStarCalPoints(List<Point> starCalPoints) {
		this.starCalPoints = starCalPoints;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
