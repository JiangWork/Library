package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class InspectionArea {
	private String id;
	private String compositeView;
	private String inspectionType;
	private String algo;
	private String algoVersion;
	private String surface;
	private String shape;
	private Point center;
	private String radius;
	private Point bottomLeftCorner;
	private Point topRightCorner;
	private Point dieCount;
	private Point dieSize;
	private Point diePitch;
	private Point dieFirstLeft;
	private Point dieFirstRight;
	private Point dieNextLeft;
	private Point dieLastRight;
	private Point dieFirstBottom;
	private Point dieFirstTop;
	private Point dieNextBottom;
	private Point dieLastTop;
	private List<String> row;  // Dropout
	private Sensitivity sensitivity;
	private LightCalibration lightCalibration;
	private ImageCalibration imageCalibration;
	private StarLightCalibration starLightCalibration;
	private List<SenseRegion> senseRegionList;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompositeView() {
		return compositeView;
	}
	public void setCompositeView(String compositeView) {
		this.compositeView = compositeView;
	}
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	public String getAlgo() {
		return algo;
	}
	public void setAlgo(String algo) {
		this.algo = algo;
	}
	public String getAlgoVersion() {
		return algoVersion;
	}
	public void setAlgoVersion(String algoVersion) {
		this.algoVersion = algoVersion;
	}
	public String getSurface() {
		return surface;
	}
	public void setSurface(String surface) {
		this.surface = surface;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public Point getCenter() {
		return center;
	}
	public void setCenter(Point center) {
		this.center = center;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public Point getBottomLeftCorner() {
		return bottomLeftCorner;
	}
	public void setBottomLeftCorner(Point bottomLeftCorner) {
		this.bottomLeftCorner = bottomLeftCorner;
	}
	public Point getTopRightCorner() {
		return topRightCorner;
	}
	public void setTopRightCorner(Point topRightCorner) {
		this.topRightCorner = topRightCorner;
	}
	public Point getDieCount() {
		return dieCount;
	}
	public void setDieCount(Point dieCount) {
		this.dieCount = dieCount;
	}
	public Point getDieSize() {
		return dieSize;
	}
	public void setDieSize(Point dieSize) {
		this.dieSize = dieSize;
	}
	public Point getDiePitch() {
		return diePitch;
	}
	public void setDiePitch(Point diePitch) {
		this.diePitch = diePitch;
	}
	public Point getDieFirstLeft() {
		return dieFirstLeft;
	}
	public void setDieFirstLeft(Point dieFirstLeft) {
		this.dieFirstLeft = dieFirstLeft;
	}
	public Point getDieFirstRight() {
		return dieFirstRight;
	}
	public void setDieFirstRight(Point dieFirstRight) {
		this.dieFirstRight = dieFirstRight;
	}
	public Point getDieNextLeft() {
		return dieNextLeft;
	}
	public void setDieNextLeft(Point dieNextLeft) {
		this.dieNextLeft = dieNextLeft;
	}
	public Point getDieLastRight() {
		return dieLastRight;
	}
	public void setDieLastRight(Point dieLastRight) {
		this.dieLastRight = dieLastRight;
	}
	public Point getDieFirstBottom() {
		return dieFirstBottom;
	}
	public void setDieFirstBottom(Point dieFirstBottom) {
		this.dieFirstBottom = dieFirstBottom;
	}
	public Point getDieFirstTop() {
		return dieFirstTop;
	}
	public void setDieFirstTop(Point dieFirstTop) {
		this.dieFirstTop = dieFirstTop;
	}
	public Point getDieNextBottom() {
		return dieNextBottom;
	}
	public void setDieNextBottom(Point dieNextBottom) {
		this.dieNextBottom = dieNextBottom;
	}
	public Point getDieLastTop() {
		return dieLastTop;
	}
	public void setDieLastTop(Point dieLastTop) {
		this.dieLastTop = dieLastTop;
	}
	public Sensitivity getSensitivity() {
		return sensitivity;
	}
	public void setSensitivity(Sensitivity sensitivity) {
		this.sensitivity = sensitivity;
	}
	public LightCalibration getLightCalibration() {
		return lightCalibration;
	}
	public void setLightCalibration(LightCalibration lightCalibration) {
		this.lightCalibration = lightCalibration;
	}
	public ImageCalibration getImageCalibration() {
		return imageCalibration;
	}
	public void setImageCalibration(ImageCalibration imageCalibration) {
		this.imageCalibration = imageCalibration;
	}
	public StarLightCalibration getStarLightCalibration() {
		return starLightCalibration;
	}
	public void setStarLightCalibration(StarLightCalibration starLightCalibration) {
		this.starLightCalibration = starLightCalibration;
	}
	public List<SenseRegion> getSenseRegionList() {
		return senseRegionList;
	}
	public void setSenseRegionList(List<SenseRegion> senseRegionList) {
		this.senseRegionList = senseRegionList;
	}
	public List<String> getRow() {
		return row;
	}
	public void setRow(List<String> row) {
		this.row = row;
	}
	
}
