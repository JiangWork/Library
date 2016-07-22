package library.generator.comparator.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import library.common.StringUtils;
import library.generator.comparator.ComparatorGenerator;
import library.generator.comparator.Order;

public class Main {
	
	public static void main(String[] args) {
		ComparatorGenerator<Computer> generator = new ComparatorGenerator<Computer>();
		generator.add("model", 2, Order.ASCENDING)
		.add("revenue", 3, Order.ASCENDING);
		List<Computer> computers = new ArrayList<Computer>();
		populateList(computers);
		Collections.sort(computers, generator.generate(Computer.class));
		for (Computer comp: computers) {
			System.out.println(StringUtils.propertiesInfo(comp));
		}
	}
	
	
	public static void populateList(List<Computer> computers) {
		computers.add(newComputer("HP", "X23", 100, 20, "China", "HP's Computer."));
		computers.add(newComputer("HP", "S78", 58, 23, "EU", "HP's Computer."));
		computers.add(newComputer("HP", "N90", 120, 87, "UK", "HP's Computer."));
		computers.add(newComputer("Lenovo", "TN3", 60, 20, "UN", "Lenovo's Computer."));
		computers.add(newComputer("Lenovo", "TN3", 61, 20, "UN", "Lenovo's Computer."));
		computers.add(newComputer("Lenovo", "TS4", 100, 20, "UN", "Lenovo's Computer."));
		computers.add(newComputer("Dell", "X46", 60, 20, "UN", "Dell's Computer."));
	}
	
	public static Computer newComputer(String branch, String model, 
			int sold, int price, String area, String desc) {
		Computer comp = new Computer();
		comp.setBranch(branch);
		comp.setModel(model);
		comp.setPrice(price);
		comp.setSold(sold);
		comp.setDescription(desc);
		comp.setArea(area);
		return comp;
	}
}


