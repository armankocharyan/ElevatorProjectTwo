package core;

public class Lamp {
	boolean on = false;
	
	public Lamp(boolean on) {
		this.on = on;
	}
	
	public void setOn(boolean on) {
		this.on = on;
	}
	
	public boolean isOn() {
		return on;
	}
}