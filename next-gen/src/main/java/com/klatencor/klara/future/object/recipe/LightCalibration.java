package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class LightCalibration {
	private String viewName;
	private String method;
	private SinglePointLightCalibration singlePointLightCalibration;
	private MultiPointLightCalibration multiPointLightCalibration;
	private ThreeBeamLightCalibration threeBeamLightCalibration;
	
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public SinglePointLightCalibration getSinglePointLightCalibration() {
		return singlePointLightCalibration;
	}

	public void setSinglePointLightCalibration(
			SinglePointLightCalibration singlePointLightCalibration) {
		this.singlePointLightCalibration = singlePointLightCalibration;
	}

	public MultiPointLightCalibration getMultiPointLightCalibration() {
		return multiPointLightCalibration;
	}

	public void setMultiPointLightCalibration(
			MultiPointLightCalibration multiPointLightCalibration) {
		this.multiPointLightCalibration = multiPointLightCalibration;
	}

	public ThreeBeamLightCalibration getThreeBeamLightCalibration() {
		return threeBeamLightCalibration;
	}

	public void setThreeBeamLightCalibration(
			ThreeBeamLightCalibration threeBeamLightCalibration) {
		this.threeBeamLightCalibration = threeBeamLightCalibration;
	}

	public static class SinglePointLightCalibration {
		private String measureTransmitted;
		private String measureReflected;
		private String transmittedBlackGoal;
		private String transmittedWhiteGoal;
		private String reflectedBlackGoal;
		private String reflectedWhiteGoal;
		private String actualBlack;
		private String actualWhite;
		private String blackWhiteRatio;
		private SPLightCalLightCalPoints lightCalPoints;
		private SPLightCalMaterials materials;
		private SPLightCalDigitizer digitizer;
		
		public String getMeasureTransmitted() {
			return measureTransmitted;
		}
		public void setMeasureTransmitted(String measureTransmitted) {
			this.measureTransmitted = measureTransmitted;
		}
		public String getMeasureReflected() {
			return measureReflected;
		}
		public void setMeasureReflected(String measureReflected) {
			this.measureReflected = measureReflected;
		}
		public String getTransmittedBlackGoal() {
			return transmittedBlackGoal;
		}
		public void setTransmittedBlackGoal(String transmittedBlackGoal) {
			this.transmittedBlackGoal = transmittedBlackGoal;
		}
		public String getTransmittedWhiteGoal() {
			return transmittedWhiteGoal;
		}
		public void setTransmittedWhiteGoal(String transmittedWhiteGoal) {
			this.transmittedWhiteGoal = transmittedWhiteGoal;
		}
		public String getReflectedBlackGoal() {
			return reflectedBlackGoal;
		}
		public void setReflectedBlackGoal(String reflectedBlackGoal) {
			this.reflectedBlackGoal = reflectedBlackGoal;
		}
		public String getReflectedWhiteGoal() {
			return reflectedWhiteGoal;
		}
		public void setReflectedWhiteGoal(String reflectedWhiteGoal) {
			this.reflectedWhiteGoal = reflectedWhiteGoal;
		}
		public String getActualBlack() {
			return actualBlack;
		}
		public void setActualBlack(String actualBlack) {
			this.actualBlack = actualBlack;
		}
		public String getActualWhite() {
			return actualWhite;
		}
		public void setActualWhite(String actualWhite) {
			this.actualWhite = actualWhite;
		}
		public String getBlackWhiteRatio() {
			return blackWhiteRatio;
		}
		public void setBlackWhiteRatio(String blackWhiteRatio) {
			this.blackWhiteRatio = blackWhiteRatio;
		}
		public SPLightCalLightCalPoints getLightCalPoints() {
			return lightCalPoints;
		}
		public void setLightCalPoints(SPLightCalLightCalPoints lightCalPoints) {
			this.lightCalPoints = lightCalPoints;
		}
		public SPLightCalMaterials getMaterials() {
			return materials;
		}
		public void setMaterials(SPLightCalMaterials materials) {
			this.materials = materials;
		}
		public SPLightCalDigitizer getDigitizer() {
			return digitizer;
		}
		public void setDigitizer(SPLightCalDigitizer digitizer) {
			this.digitizer = digitizer;
		}		
	}
	
	public static class SPLightCalLightCalPoints {
		private List<Point> lightCalPoints;

		public List<Point> getLightCalPoints() {
			return lightCalPoints;
		}

		public void setLightCalPoints(List<Point> lightCalPoints) {
			this.lightCalPoints = lightCalPoints;
		}		
	}
	
	public static class SPLightCalMaterials {
		private List<SPLightCalMaterial> materials;

		public List<SPLightCalMaterial> getMaterials() {
			return materials;
		}

		public void setMaterials(List<SPLightCalMaterial> materials) {
			this.materials = materials;
		}
		
	}
	public static class SPLightCalMaterial {
		private String name;
		private Point point;		
		private String transmitedGoal;
		private String reflectedGoal;
		private String transmitedGrayScale;		
		private String reflectedGrayScale;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Point getPoint() {
			return point;
		}
		public void setPoint(Point point) {
			this.point = point;
		}
		public String getTransmitedGoal() {
			return transmitedGoal;
		}
		public void setTransmitedGoal(String transmitedGoal) {
			this.transmitedGoal = transmitedGoal;
		}
		public String getReflectedGoal() {
			return reflectedGoal;
		}
		public void setReflectedGoal(String reflectedGoal) {
			this.reflectedGoal = reflectedGoal;
		}
		public String getTransmitedGrayScale() {
			return transmitedGrayScale;
		}
		public void setTransmitedGrayScale(String transmitedGrayScale) {
			this.transmitedGrayScale = transmitedGrayScale;
		}
		public String getReflectedGrayScale() {
			return reflectedGrayScale;
		}
		public void setReflectedGrayScale(String reflectedGrayScale) {
			this.reflectedGrayScale = reflectedGrayScale;
		}
		
	}
	
	public static class SPLightCalDigitizer {
		private String transmittedGain;
		private String transmittedOffset;		
		private String reflectedGain;
		private String reflectedOffset;
		public String getTransmittedGain() {
			return transmittedGain;
		}
		public void setTransmittedGain(String transmittedGain) {
			this.transmittedGain = transmittedGain;
		}
		public String getTransmittedOffset() {
			return transmittedOffset;
		}
		public void setTransmittedOffset(String transmittedOffset) {
			this.transmittedOffset = transmittedOffset;
		}
		public String getReflectedGain() {
			return reflectedGain;
		}
		public void setReflectedGain(String reflectedGain) {
			this.reflectedGain = reflectedGain;
		}
		public String getReflectedOffset() {
			return reflectedOffset;
		}
		public void setReflectedOffset(String reflectedOffset) {
			this.reflectedOffset = reflectedOffset;
		}
		
	}

	public static class MultiPointLightCalibration {
		private List<MPLightCalibration> calList;

		public List<MPLightCalibration> getCalList() {
			return calList;
		}

		public void setCalList(List<MPLightCalibration> calList) {
			this.calList = calList;
		}
		
	}
	
	public static class MPLightCalibration {
		private MPLightCalDigitizer  digitizer;
		private MPLightCalGoalValues goalValues;
		private MPLightCalMaterials materials;
		
		public MPLightCalDigitizer getDigitizer() {
			return digitizer;
		}
		public void setDigitizer(MPLightCalDigitizer digitizer) {
			this.digitizer = digitizer;
		}
		public MPLightCalGoalValues getGoalValues() {
			return goalValues;
		}
		public void setGoalValues(MPLightCalGoalValues goalValues) {
			this.goalValues = goalValues;
		}
		public MPLightCalMaterials getMaterials() {
			return materials;
		}
		public void setMaterials(MPLightCalMaterials materials) {
			this.materials = materials;
		}
		
	}
	
	public static class MPLightCalDigitizer {
		private LcLightMode lightMode;

		public LcLightMode getLightMode() {
			return lightMode;
		}

		public void setLightMode(LcLightMode lightMode) {
			this.lightMode = lightMode;
		}

		public static class LcLightMode {
			private String illum;
			private String lightLevel;
			private String splitRatio;
			private String gain;
			private String offset;
			private String actualBlack;

			public String getIllum() {
				return illum;
			}

			public void setIllum(String illum) {
				this.illum = illum;
			}

			public String getLightLevel() {
				return lightLevel;
			}

			public void setLightLevel(String lightLevel) {
				this.lightLevel = lightLevel;
			}

			public String getSplitRatio() {
				return splitRatio;
			}

			public void setSplitRatio(String splitRatio) {
				this.splitRatio = splitRatio;
			}

			public String getGain() {
				return gain;
			}

			public void setGain(String gain) {
				this.gain = gain;
			}

			public String getOffset() {
				return offset;
			}

			public void setOffset(String offset) {
				this.offset = offset;
			}

			public String getActualBlack() {
				return actualBlack;
			}

			public void setActualBlack(String actualBlack) {
				this.actualBlack = actualBlack;
			}
		}
	}

	public static class MPLightCalGoalValues {
		private LcLightMode lightMode;

		public LcLightMode getLightMode() {
			return lightMode;
		}

		public void setLightMode(LcLightMode lightMode) {
			this.lightMode = lightMode;
		}

		public static class LcLightMode {
			private String illum;
			private String blackGoal;
			private String whiteGoal;

			public String getIllum() {
				return illum;
			}

			public void setIllum(String illum) {
				this.illum = illum;
			}

			public String getBlackGoal() {
				return blackGoal;
			}

			public void setBlackGoal(String blackGoal) {
				this.blackGoal = blackGoal;
			}

			public String getWhiteGoal() {
				return whiteGoal;
			}

			public void setWhiteGoal(String whiteGoal) {
				this.whiteGoal = whiteGoal;
			}
		}
	}
	
	public static class MPLightCalMaterials {
		private List<MPLightCalMaterial> materials;

		public List<MPLightCalMaterial> getMaterials() {
			return materials;
		}

		public void setMaterials(List<MPLightCalMaterial> materials) {
			this.materials = materials;
		}
		
	}
	
	public static class MPLightCalMaterial {
		private String name;
		private Point point;
		private GrayScaleValue value;
	
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

		public GrayScaleValue getValue() {
			return value;
		}

		public void setValue(GrayScaleValue value) {
			this.value = value;
		}

		public static class GrayScaleValue {
			private String value;
			private String lightMode;
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getLightMode() {
				return lightMode;
			}
			public void setLightMode(String lightMode) {
				this.lightMode = lightMode;
			}
		}
		
	}

	public static class ThreeBeamLightCalibration {
		private ThreeBeamTransmited transmittedSingleBeam;
		private ThreeBeamReflected reflectedSingleBeam;
		private ThreeBeamTransmited transmittedLower;
		private ThreeBeamReflected reflectedLower;
		private ThreeBeamTransmited transmittedMiddle;
		private ThreeBeamReflected reflectedMiddle;
		private ThreeBeamTransmited transmittedUpper;
		private ThreeBeamReflected reflectedUpper;
		public ThreeBeamTransmited getTransmittedSingleBeam() {
			return transmittedSingleBeam;
		}
		public void setTransmittedSingleBeam(ThreeBeamTransmited transmittedSingleBeam) {
			this.transmittedSingleBeam = transmittedSingleBeam;
		}
		public ThreeBeamReflected getReflectedSingleBeam() {
			return reflectedSingleBeam;
		}
		public void setReflectedSingleBeam(ThreeBeamReflected reflectedSingleBeam) {
			this.reflectedSingleBeam = reflectedSingleBeam;
		}
		public ThreeBeamTransmited getTransmittedLower() {
			return transmittedLower;
		}
		public void setTransmittedLower(ThreeBeamTransmited transmittedLower) {
			this.transmittedLower = transmittedLower;
		}
		public ThreeBeamReflected getReflectedLower() {
			return reflectedLower;
		}
		public void setReflectedLower(ThreeBeamReflected reflectedLower) {
			this.reflectedLower = reflectedLower;
		}
		public ThreeBeamTransmited getTransmittedMiddle() {
			return transmittedMiddle;
		}
		public void setTransmittedMiddle(ThreeBeamTransmited transmittedMiddle) {
			this.transmittedMiddle = transmittedMiddle;
		}
		public ThreeBeamReflected getReflectedMiddle() {
			return reflectedMiddle;
		}
		public void setReflectedMiddle(ThreeBeamReflected reflectedMiddle) {
			this.reflectedMiddle = reflectedMiddle;
		}
		public ThreeBeamTransmited getTransmittedUpper() {
			return transmittedUpper;
		}
		public void setTransmittedUpper(ThreeBeamTransmited transmittedUpper) {
			this.transmittedUpper = transmittedUpper;
		}
		public ThreeBeamReflected getReflectedUpper() {
			return reflectedUpper;
		}
		public void setReflectedUpper(ThreeBeamReflected reflectedUpper) {
			this.reflectedUpper = reflectedUpper;
		}
		
	}
	
	public static class ThreeBeamReflected {
		private String reflectedBlack;
		private String reflectedWhite;
		private String reflectedGain;
		public String getReflectedBlack() {
			return reflectedBlack;
		}
		public void setReflectedBlack(String reflectedBlack) {
			this.reflectedBlack = reflectedBlack;
		}
		public String getReflectedWhite() {
			return reflectedWhite;
		}
		public void setReflectedWhite(String reflectedWhite) {
			this.reflectedWhite = reflectedWhite;
		}
		public String getReflectedGain() {
			return reflectedGain;
		}
		public void setReflectedGain(String reflectedGain) {
			this.reflectedGain = reflectedGain;
		}
		
	}
	
	public static class ThreeBeamTransmited {
		private String transmitedBlack;
		private String transmitedWhite;
		private String transmitedOffset;
		public String getTransmitedBlack() {
			return transmitedBlack;
		}
		public void setTransmitedBlack(String transmitedBlack) {
			this.transmitedBlack = transmitedBlack;
		}
		public String getTransmitedWhite() {
			return transmitedWhite;
		}
		public void setTransmitedWhite(String transmitedWhite) {
			this.transmitedWhite = transmitedWhite;
		}
		public String getTransmitedOffset() {
			return transmitedOffset;
		}
		public void setTransmitedOffset(String transmitedOffset) {
			this.transmitedOffset = transmitedOffset;
		}
		
		
	}
}
