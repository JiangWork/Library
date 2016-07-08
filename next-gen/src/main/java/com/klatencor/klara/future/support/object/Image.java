package com.klatencor.klara.future.support.object;

/**
 * Image information from IR/Recipe.
 * 
 * @author jiangzhao
 * @date  Jul 1, 2016
 * @version V1.0
 */
public class Image {
	
	private int width;
	private int height;
	private String description;
	private byte[] imageData;
	
	// following information is from description
	private String type;
	private String feImageName;
	private int feType;
	private String viewAlias;
	private int detectionIdx;
	private int planeId;
	private String klarfImageName;
	
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public String getFeImageName() {
		return feImageName;
	}
	public void setFeImageName(String feImageName) {
		this.feImageName = feImageName;
	}
	public int getFeType() {
		return feType;
	}
	public void setFeType(int feType) {
		this.feType = feType;
	}
	public String getViewAlias() {
		return viewAlias;
	}
	public void setViewAlias(String viewAlias) {
		this.viewAlias = viewAlias;
	}
	public int getDetectionIdx() {
		return detectionIdx;
	}
	public void setDetectionIdx(int detectionIdx) {
		this.detectionIdx = detectionIdx;
	}
	public int getPlaneId() {
		return planeId;
	}
	public void setPlaneId(int planeId) {
		this.planeId = planeId;
	}
	public String getKlarfImageName() {
		return klarfImageName;
	}
	public void setKlarfImageName(String klarfImageName) {
		this.klarfImageName = klarfImageName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * Extract informtion like feType, viewAlias from {@code description}.
	 */
	public void populateDesc() {
		if (this.description != null) {
			String lightMode = "";
			String[] comps = description.split(" ");
			for (String comp: comps) {
				if (comp.startsWith("Type=")) {
					type = comp.substring(6, comp.length() - 1);
				} else if (comp.startsWith("Name=")) {
					feImageName = comp.substring(6, comp.length() - 1);
				} else if (comp.startsWith("feType=")) {
					feType = Integer.parseInt(comp.substring(8, comp.length() - 1));
				} else if (comp.startsWith("DetectionIdx=")) {
					detectionIdx = Integer.parseInt(comp.substring(14, comp.length() - 1));
					detectionIdx = detectionIdx == -1 ? 0: detectionIdx;
				} else if (comp.startsWith("ViewAlias=")) {
					viewAlias = comp.substring(11, comp.length() - 1);
				} else if (comp.startsWith("PlaneId=")) {
					planeId = Integer.parseInt(comp.substring(9, comp.length() - 1));
				} else if (comp.startsWith("LightMode=")) {
					lightMode = comp.substring(11, comp.length() - 1);
					if (lightMode.equalsIgnoreCase("ReflectedPrime"))
			        {
			        	lightMode = "Reflected";
			        }
			        if (lightMode.equalsIgnoreCase("TransmittedPrime"))
			        {
			            lightMode = "Transmitted";
			        }
				} else {
					// nothing
				}
			}
			klarfImageName = feImageName;
			if(type != null && feType >= 200) {
				klarfImageName += lightMode+"Plane"+planeId+"Pass"+detectionIdx;
			}
		}
	}
	
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("width=" + width);
		sb.append(" height=" + height);
		sb.append(" desc=[" + description + "]");
		sb.append(" imageData=" + (imageData == null? 0: imageData.length));
		sb.append(" type=" + type);
		sb.append(" feImageName=" + feImageName);
		sb.append(" feType=" + feType);
		sb.append(" viewAlias=" + viewAlias);
		sb.append(" detectionIdx=" + detectionIdx);
		sb.append(" planeId=" + planeId);
		sb.append(" klarfImageName=" + klarfImageName);
		return sb.toString();
	}
	
	public String toString() {
		return debug();
	}
	
}
