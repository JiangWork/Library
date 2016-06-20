/**
 * 
 */
package com.klatencor.klara.future.object.recipe;

import java.util.List;

/**
 * A recipe java bean which is identical to XML.
 *
 * @author jiangzhao
 * @date May 28, 2016
 * @version V1.0
 */
public class Recipe {

	private GeneralRecipeData recipeGeneral;
	private Behavior behavior;
	private PlateAlignment plateAlignment;
	private ZCalibration zCalibration;
	private List<InspectionArea> inspectionAreaList;
	
	/**Additional fields**/
	private String recipeStoragePath;
	private String recipeType; //S or N
	private String systemName;
	private int fmId;
	private int ppId;
	
	public GeneralRecipeData getRecipeGeneral() {
		return recipeGeneral;
	}
	public void setRecipeGeneral(GeneralRecipeData recipeGeneral) {
		this.recipeGeneral = recipeGeneral;
	}
	public Behavior getBehavior() {
		return behavior;
	}
	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}
	public PlateAlignment getPlateAlignment() {
		return plateAlignment;
	}
	public void setPlateAlignment(PlateAlignment plateAlignment) {
		this.plateAlignment = plateAlignment;
	}
	public ZCalibration getZCalibration() {
		return zCalibration;
	}
	public void setZCalibration(ZCalibration zCalibration) {
		this.zCalibration = zCalibration;
	}
	public List<InspectionArea> getInspectionAreaList() {
		return inspectionAreaList;
	}
	public void setInspectionAreaList(List<InspectionArea> inspectionAreaList) {
		this.inspectionAreaList = inspectionAreaList;
	}
	public String getRecipeStoragePath() {
		return recipeStoragePath;
	}
	public void setRecipeStoragePath(String recipeStoragePath) {
		this.recipeStoragePath = recipeStoragePath;
	}
	public String getRecipeType() {
		return recipeType;
	}
	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public int getFmId() {
		return fmId;
	}
	public void setFmId(int fmId) {
		this.fmId = fmId;
	}
	public int getPpId() {
		return ppId;
	}
	public void setPpId(int ppId) {
		this.ppId = ppId;
	}
	
	
}
