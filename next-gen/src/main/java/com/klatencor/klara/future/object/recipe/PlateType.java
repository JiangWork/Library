package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class PlateType {

	private String type;
	private String description;
	private String algPlateType;
	private String mOPF;
	private String zCalModel;	
	private List<Material> materials;	
	private List<FocusOffset> focusOffsets;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAlgPlateType() {
		return algPlateType;
	}
	public void setAlgPlateType(String algPlateType) {
		this.algPlateType = algPlateType;
	}
	public String getMOPF() {
		return mOPF;
	}
	public void setMOPF(String mOPF) {
		this.mOPF = mOPF;
	}
	public String getZCalModel() {
		return zCalModel;
	}
	public void setZCalModel(String zCalModel) {
		this.zCalModel = zCalModel;
	}
	public List<Material> getMaterials() {
		return materials;
	}
	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}
	public List<FocusOffset> getFocusOffsets() {
		return focusOffsets;
	}
	public void setFocusOffsets(List<FocusOffset> focusOffsets) {
		this.focusOffsets = focusOffsets;
	}

	
}
