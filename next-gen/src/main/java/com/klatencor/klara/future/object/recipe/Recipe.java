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
}
