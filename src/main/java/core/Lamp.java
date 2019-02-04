package core;

public class Lamp {
	boolean on = false;
	String name = "";
	
	public Lamp(String s, boolean on) {
		this.on = on;
		this.name = s;
	}
	
	public void setOn(boolean on) {
		this.on = on;
		if (on) System.out.println(name+" turned ON.");
		else System.out.println(name+" turned OFF.");
	}
	
	public boolean isOn() {
		return on;
	}
}