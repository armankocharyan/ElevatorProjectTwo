package core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Logger.Logger;

public class Lamp {
	boolean on = false;
	String name = "";
	
	private Calendar cal; 
	private SimpleDateFormat time;
	
	public Lamp(String s, boolean on) {
		this.on = on;
		this.name = s;
		time = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	public void setOn(boolean on) {
		this.on = on;
		cal = Calendar.getInstance();
		if (on) {
			Logger.write("LAMP TURNED ON AT " + time.format(cal.getTime()) + "\n", "Logs/lamp.log");
			System.out.println(name+" turned ON.");
		}
		else {
			Logger.write("LAMP TURNED OFF AT " + time.format(cal.getTime()) + "\n", "Logs/lamp.log");
			System.out.println(name+" turned OFF.");
		}
	}
	
	public boolean isOn() {
		return on;
	}
	
	public static void main(String[] args) {
		Lamp lamp = new Lamp("Lamp", false);
		lamp.setOn(true);
	}
	
}