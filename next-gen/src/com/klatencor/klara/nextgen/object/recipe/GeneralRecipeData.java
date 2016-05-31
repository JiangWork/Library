package com.klatencor.klara.nextgen.object.recipe;

public class GeneralRecipeData {
	
	private String recipeName;
	private String recipeVersion;
	private String systemType;
	private String minimumRecpeVersion;
	private String opaqueSubstrateMode;
	private String note;
	private String databaseName;
	private String plateMirror;
	private String dbImageRotation;
	private String platePolarity;
	private String plateTone;
	private PlateInfo plateInfo;
	private PlateOpticalAttributes plateOpticalAttributes;
	private RteParameters rteParameters;
	
	
	public String getRecipeName() {
		return recipeName;
	}
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	public String getRecipeVersion() {
		return recipeVersion;
	}
	public void setRecipeVersion(String recipeVersion) {
		this.recipeVersion = recipeVersion;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getMinimumRecpeVersion() {
		return minimumRecpeVersion;
	}
	public void setMinimumRecpeVersion(String minimumRecpeVersion) {
		this.minimumRecpeVersion = minimumRecpeVersion;
	}
	public String getOpaqueSubstrateMode() {
		return opaqueSubstrateMode;
	}
	public void setOpaqueSubstrateMode(String opaqueSubstrateMode) {
		this.opaqueSubstrateMode = opaqueSubstrateMode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getPlateMirror() {
		return plateMirror;
	}
	public void setPlateMirror(String plateMirror) {
		this.plateMirror = plateMirror;
	}
	public String getDbImageRotation() {
		return dbImageRotation;
	}
	public void setDbImageRotation(String dbImageRotation) {
		this.dbImageRotation = dbImageRotation;
	}
	public String getPlatePolarity() {
		return platePolarity;
	}
	public void setPlatePolarity(String platePolarity) {
		this.platePolarity = platePolarity;
	}
	public String getPlateTone() {
		return plateTone;
	}
	public void setPlateTone(String plateTone) {
		this.plateTone = plateTone;
	}
	public PlateInfo getPlateInfo() {
		return plateInfo;
	}
	public void setPlateInfo(PlateInfo plateInfo) {
		this.plateInfo = plateInfo;
	}
	public PlateOpticalAttributes getPlateOpticalAttributes() {
		return plateOpticalAttributes;
	}
	public void setPlateOpticalAttributes(
			PlateOpticalAttributes plateOpticalAttributes) {
		this.plateOpticalAttributes = plateOpticalAttributes;
	}
	public RteParameters getRteParameters() {
		return rteParameters;
	}
	public void setRteParameters(RteParameters rteParameters) {
		this.rteParameters = rteParameters;
	}
}
