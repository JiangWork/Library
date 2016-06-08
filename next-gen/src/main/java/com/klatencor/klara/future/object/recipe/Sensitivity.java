package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class Sensitivity {
	private String acquisitionView;
	private String edgeSpeedRatio;
	private String concatRadius;
	private String opcEnable;
	private String transLossEnable;
	private String highSensitivityEnable;
	private Point pixelSize; 
	private DefectList defectList;
	private String upaLevel;

	public String getAcquisitionView() {
		return acquisitionView;
	}

	public void setAcquisitionView(String acquisitionView) {
		this.acquisitionView = acquisitionView;
	}

	public String getEdgeSpeedRatio() {
		return edgeSpeedRatio;
	}

	public void setEdgeSpeedRatio(String edgeSpeedRatio) {
		this.edgeSpeedRatio = edgeSpeedRatio;
	}

	public String getConcatRadius() {
		return concatRadius;
	}

	public void setConcatRadius(String concatRadius) {
		this.concatRadius = concatRadius;
	}

	public String getOpcEnable() {
		return opcEnable;
	}

	public void setOpcEnable(String opcEnable) {
		this.opcEnable = opcEnable;
	}

	public String getTransLossEnable() {
		return transLossEnable;
	}

	public void setTransLossEnable(String transLossEnable) {
		this.transLossEnable = transLossEnable;
	}

	public String getHighSensitivityEnable() {
		return highSensitivityEnable;
	}

	public void setHighSensitivityEnable(String highSensitivityEnable) {
		this.highSensitivityEnable = highSensitivityEnable;
	}

	public Point getPixelSize() {
		return pixelSize;
	}

	public void setPixelSize(Point pixelSize) {
		this.pixelSize = pixelSize;
	}
	
	public DefectList getDefectList() {
		return defectList;
	}

	public void setDefectList(DefectList defectList) {
		this.defectList = defectList;
	}

	public String getUpaLevel() {
		return upaLevel;
	}

	public void setUpaLevel(String upaLevel) {
		this.upaLevel = upaLevel;
	}

	public static class Defect {
		private String defectType;
		private String value;
		private String type;
		private String min;
		private String max;
		private String displayMode;
		private String defectID;
		private String maxSmallBndry;
		private String maxMedBndry;
		private String maxLargeBndry;
		private String small;
		private String medium;
		private String large;
		private String xLarge;
		private String extension;
		private String directory;
		private String edit;
		private String optionValue;
		private String rankValue;
		private String precision;
		
		public String getDefectType() {
			return defectType;
		}
		public void setDefectType(String defectType) {
			this.defectType = defectType;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getMin() {
			return min;
		}
		public void setMin(String min) {
			this.min = min;
		}
		public String getMax() {
			return max;
		}
		public void setMax(String max) {
			this.max = max;
		}
		public String getDisplayMode() {
			return displayMode;
		}
		public void setDisplayMode(String displayMode) {
			this.displayMode = displayMode;
		}
		public String getDefectID() {
			return defectID;
		}
		public void setDefectID(String defectID) {
			this.defectID = defectID;
		}
		public String getMaxSmallBndry() {
			return maxSmallBndry;
		}
		public void setMaxSmallBndry(String maxSmallBndry) {
			this.maxSmallBndry = maxSmallBndry;
		}
		public String getMaxMedBndry() {
			return maxMedBndry;
		}
		public void setMaxMedBndry(String maxMedBndry) {
			this.maxMedBndry = maxMedBndry;
		}
		public String getMaxLargeBndry() {
			return maxLargeBndry;
		}
		public void setMaxLargeBndry(String maxLargeBndry) {
			this.maxLargeBndry = maxLargeBndry;
		}
		public String getSmall() {
			return small;
		}
		public void setSmall(String small) {
			this.small = small;
		}
		public String getMedium() {
			return medium;
		}
		public void setMedium(String medium) {
			this.medium = medium;
		}
		public String getLarge() {
			return large;
		}
		public void setLarge(String large) {
			this.large = large;
		}
		public String getXLarge() {
			return xLarge;
		}
		public void setXLarge(String xLarge) {
			this.xLarge = xLarge;
		}
		public String getExtension() {
			return extension;
		}
		public void setExtension(String extension) {
			this.extension = extension;
		}
		public String getDirectory() {
			return directory;
		}
		public void setDirectory(String directory) {
			this.directory = directory;
		}
		public String getEdit() {
			return edit;
		}
		public void setEdit(String edit) {
			this.edit = edit;
		}
		public String getOptionValue() {
			return optionValue;
		}
		public void setOptionValue(String optionValue) {
			this.optionValue = optionValue;
		}
		public String getRankValue() {
			return rankValue;
		}
		public void setRankValue(String rankValue) {
			this.rankValue = rankValue;
		}
		public String getPrecision() {
			return precision;
		}
		public void setPrecision(String precision) {
			this.precision = precision;
		}
		
		
	}
	
	public static class DefectList {
		private List<Defect> defectList;

		public List<Defect> getDefectList() {
			return defectList;
		}

		public void setDefectList(List<Defect> defectList) {
			this.defectList = defectList;
		}
		
	}
}
