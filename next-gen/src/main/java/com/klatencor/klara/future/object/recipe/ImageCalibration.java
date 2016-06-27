package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class ImageCalibration {
	
	private String method;
	private String measured;
	private List<Triple> ponits;
	private ImageCalibrationOperatorValues opValues;
	private String lithoModel;

	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMeasured() {
		return measured;
	}
	public void setMeasured(String measured) {
		this.measured = measured;
	}
	public List<Triple> getPonits() {
		return ponits;
	}
	public void setPonits(List<Triple> ponits) {
		this.ponits = ponits;
	}
	public ImageCalibrationOperatorValues getOpValues() {
		return opValues;
	}
	public void setOpValues(ImageCalibrationOperatorValues opValues) {
		this.opValues = opValues;
	}
	public String getLithoModel() {
		return lithoModel;
	}
	public void setLithoModel(String lithoModel) {
		this.lithoModel = lithoModel;
	}



	public static class ImageCalibrationPoints {
		private List<Triple> points;

		public List<Triple> getPoints() {
			return points;
		}

		public void setPoints(List<Triple> points) {
			this.points = points;
		}		
	}
	
	public static class ImageCalibrationOperatorValues {
		private String rendering;
		private String xbias;
		private String insideCR;
		private String outsideCR;
		private String totalPoints;
		private String validPoints;
		private List<LightMode> lightMode;
		private String useWSpotCal;
		private String wpiRenderMode;
		private String imageCalibrationStatus;
		public String getRendering() {
			return rendering;
		}
		public void setRendering(String rendering) {
			this.rendering = rendering;
		}
		public String getXbias() {
			return xbias;
		}
		public void setXbias(String xbias) {
			this.xbias = xbias;
		}
		public String getInsideCR() {
			return insideCR;
		}
		public void setInsideCR(String insideCR) {
			this.insideCR = insideCR;
		}
		public String getOutsideCR() {
			return outsideCR;
		}
		public void setOutsideCR(String outsideCR) {
			this.outsideCR = outsideCR;
		}
		public String getTotalPoints() {
			return totalPoints;
		}
		public void setTotalPoints(String totalPoints) {
			this.totalPoints = totalPoints;
		}
		public String getValidPoints() {
			return validPoints;
		}
		public void setValidPoints(String validPoints) {
			this.validPoints = validPoints;
		}
		public List<LightMode> getLightMode() {
			return lightMode;
		}
		public void setLightMode(List<LightMode> lightMode) {
			this.lightMode = lightMode;
		}
		public String getUseWSpotCal() {
			return useWSpotCal;
		}
		public void setUseWSpotCal(String useWSpotCal) {
			this.useWSpotCal = useWSpotCal;
		}
		public String getWpiRenderMode() {
			return wpiRenderMode;
		}
		public void setWpiRenderMode(String wpiRenderMode) {
			this.wpiRenderMode = wpiRenderMode;
		}
		public String getImageCalibrationStatus() {
			return imageCalibrationStatus;
		}
		public void setImageCalibrationStatus(String imageCalibrationStatus) {
			this.imageCalibrationStatus = imageCalibrationStatus;
		}
	}
	
	public static class LightMode {
		private List<Triple> points;
		private ReturnCode returnCode;
		private Residues residues;
		public List<Triple> getPoints() {
			return points;
		}
		public void setPoints(List<Triple> points) {
			this.points = points;
		}
		public ReturnCode getReturnCode() {
			return returnCode;
		}
		public void setReturnCode(ReturnCode returnCode) {
			this.returnCode = returnCode;
		}
		public Residues getResidues() {
			return residues;
		}
		public void setResidues(Residues residues) {
			this.residues = residues;
		}
	}
	
	public static class ReturnCode {
		private String L1;
		private String L2;
		public String getL1() {
			return L1;
		}
		public void setL1(String l1) {
			L1 = l1;
		}
		public String getL2() {
			return L2;
		}
		public void setL2(String l2) {
			L2 = l2;
		}
	}
	
	public static class Residues {
		private List<Point> points;
		private List<AllPoint> allPoints;
		 		
		public List<Point> getPoints() {
			return points;
		}

		public void setPoints(List<Point> points) {
			this.points = points;
		}

		public List<AllPoint> getAllPoints() {
			return allPoints;
		}

		public void setAllPoints(List<AllPoint> allPoints) {
			this.allPoints = allPoints;
		}

		public static class Point {
			private String Id;
			private String hist11;
			private String hist22;
			public String getId() {
				return Id;
			}
			public void setId(String id) {
				Id = id;
			}
			public String getHist11() {
				return hist11;
			}
			public void setHist11(String hist11) {
				this.hist11 = hist11;
			}
			public String getHist22() {
				return hist22;
			}
			public void setHist22(String hist22) {
				this.hist22 = hist22;
			}
		}
		
		public static class AllPoint {
			private String type;
			private String max;
			private String mean;
			private String std;
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
			public String getMax() {
				return max;
			}
			public void setMax(String max) {
				this.max = max;
			}
			public String getMean() {
				return mean;
			}
			public void setMean(String mean) {
				this.mean = mean;
			}
			public String getStd() {
				return std;
			}
			public void setStd(String std) {
				this.std = std;
			}
		}
	}
}
