package library.java.basics;

public class Employee {

	public String name;
	protected int id;
	private int age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	
	public int getAge() {
		return age;
	}	
}
