package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class Material {

	private String name;
	private String height;
	private List<LightMode> lightModes;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public List<LightMode> getLightModes() {
		return lightModes;
	}
	public void setLightModes(List<LightMode> lightModes) {
		this.lightModes = lightModes;
	}
	
	
}
