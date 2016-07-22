package library.generator.comparator.example;

import library.generator.comparator.Order;
import library.generator.comparator.Sorted;

public class Computer {
	
	@Sorted(priority=1, order=Order.DESCENDING)
	private String branch;
	@Sorted(priority=8, order=Order.DESCENDING)
	private String model;
	private int sold;
	@Sorted(priority=3, order=Order.ASCENDING)
	private int price;
	
	private String description;
	private String area;
	
	@Sorted(priority=4, order=Order.DESCENDING)
	public long revenue() {
		return 1L*sold*price;
	}
	
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
}
