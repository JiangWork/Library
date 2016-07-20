package library.oxm;

import java.util.List;

public class SimpleBean {
	
	private boolean isNative;
	private String name;
	private List<String> values;
	private int number;
	
	public boolean getIsNative() {
		return isNative;
	}
	public void setNative(boolean isNative) {
		this.isNative = isNative;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String toString() {
		return isNative + ":" + name + ":" + number + ":" + values;
	}
}
