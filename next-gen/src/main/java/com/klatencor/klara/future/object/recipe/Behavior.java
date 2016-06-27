package com.klatencor.klara.future.object.recipe;

/**
 * 
 * TODO
 *
 * @author jiangzhao
 * @date May 28, 2016
 * @version V1.0
 */
public class Behavior {
	
	private String autoSetup;
	private String autoInspect;
	private String autoReview;
	private String autoUnload;
	private String abortJobOnError;
	private String printAfterInspection;
	private String printAfterReview;
	private String printFullReport;
	private String storeAfterInspection;
	private String storeAfterReview;
	private String storeImages;
	private String displayNewDefects;
	private String iadDefectConcat;
	private String vDLEnabled;
	private String vDLArrayX;
	private String vDLFilename;
	private String inspectionCount;
	private String abortFailCount; 
	private String abortWarnCount;
	private String zCalForP90;
	private String zCalForP125;
	private String autoDnir;
	private String diagCalType;
	private String vdlParameters;
	private String spotCal;
	public String getAutoSetup() {
		return autoSetup;
	}
	public void setAutoSetup(String autoSetup) {
		this.autoSetup = autoSetup;
	}
	public String getAutoInspect() {
		return autoInspect;
	}
	public void setAutoInspect(String autoInspect) {
		this.autoInspect = autoInspect;
	}
	public String getAutoReview() {
		return autoReview;
	}
	public void setAutoReview(String autoReview) {
		this.autoReview = autoReview;
	}
	public String getAutoUnload() {
		return autoUnload;
	}
	public void setAutoUnload(String autoUnload) {
		this.autoUnload = autoUnload;
	}
	public String getAbortJobOnError() {
		return abortJobOnError;
	}
	public void setAbortJobOnError(String abortJobOnError) {
		this.abortJobOnError = abortJobOnError;
	}
	public String getPrintAfterInspection() {
		return printAfterInspection;
	}
	public void setPrintAfterInspection(String printAfterInspection) {
		this.printAfterInspection = printAfterInspection;
	}
	public String getPrintAfterReview() {
		return printAfterReview;
	}
	public void setPrintAfterReview(String printAfterReview) {
		this.printAfterReview = printAfterReview;
	}
	public String getPrintFullReport() {
		return printFullReport;
	}
	public void setPrintFullReport(String printFullReport) {
		this.printFullReport = printFullReport;
	}
	public String getStoreAfterInspection() {
		return storeAfterInspection;
	}
	public void setStoreAfterInspection(String storeAfterInspection) {
		this.storeAfterInspection = storeAfterInspection;
	}
	public String getStoreAfterReview() {
		return storeAfterReview;
	}
	public void setStoreAfterReview(String storeAfterReview) {
		this.storeAfterReview = storeAfterReview;
	}
	public String getStoreImages() {
		if(storeImages == null)
			storeImages = "";
		return storeImages;
	}
	public void setStoreImages(String storeImages) {
		this.storeImages = storeImages;
	}
	public String getDisplayNewDefects() {
		return displayNewDefects;
	}
	public void setDisplayNewDefects(String displayNewDefects) {
		this.displayNewDefects = displayNewDefects;
	}
	public String getIadDefectConcat() {
		return iadDefectConcat;
	}
	public void setIadDefectConcat(String iadDefectConcat) {
		this.iadDefectConcat = iadDefectConcat;
	}
	public String getVDLEnabled() {
		return vDLEnabled;
	}
	public void setVDLEnabled(String vDLEnabled) {
		this.vDLEnabled = vDLEnabled;
	}
	public String getVDLArrayX() {
		return vDLArrayX;
	}
	public void setVDLArrayX(String vDLArrayX) {
		this.vDLArrayX = vDLArrayX;
	}
	public String getVDLFilename() {
		return vDLFilename;
	}
	public void setVDLFilename(String vDLFilename) {
		this.vDLFilename = vDLFilename;
	}
	public String getInspectionCount() {
		return inspectionCount;
	}
	public void setInspectionCount(String inspectionCount) {
		this.inspectionCount = inspectionCount;
	}
	public String getAbortFailCount() {
		return abortFailCount;
	}
	public void setAbortFailCount(String abortFailCount) {
		this.abortFailCount = abortFailCount;
	}
	public String getAbortWarnCount() {
		return abortWarnCount;
	}
	public void setAbortWarnCount(String abortWarnCount) {
		this.abortWarnCount = abortWarnCount;
	}
	public String getZCalForP90() {
		return zCalForP90;
	}
	public void setZCalForP90(String zCalForP90) {
		this.zCalForP90 = zCalForP90;
	}
	public String getZCalForP125() {
		return zCalForP125;
	}
	public void setZCalForP125(String zCalForP125) {
		this.zCalForP125 = zCalForP125;
	}
	public String getAutoDnir() {
		return autoDnir;
	}
	public void setAutoDnir(String autoDnir) {
		this.autoDnir = autoDnir;
	}
	public String getDiagCalType() {
		return diagCalType;
	}
	public void setDiagCalType(String diagCalType) {
		this.diagCalType = diagCalType;
	}
	public String getVdlParameters() {
		return vdlParameters;
	}
	public void setVdlParameters(String vdlParameters) {
		this.vdlParameters = vdlParameters;
	}
	public String getSpotCal() {
		return spotCal;
	}
	public void setSpotCal(String spotCal) {
		this.spotCal = spotCal;
	}
}
