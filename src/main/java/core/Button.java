package core;

import Logger.Logger;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Button {

	private Calendar cal; 
	private SimpleDateFormat time;
	boolean pressed = false;
	boolean active = true;
	String name = "";
	
	public Button(String s, boolean active) {
		time = new SimpleDateFormat("HH:mm:ss.SSS");
		this.active = active;
		this.pressed = false;
		this.name = s;
	}
	
	public void setPressed(boolean pressed) {
		
		cal = Calendar.getInstance();
<<<<<<< HEAD
<<<<<<< HEAD
		Logger.write("Button Pressed at " + time.format(cal.getTime()) , "Logs/button.log");
		
=======
		Logger.write("BUTTON PRESSED AT " + time.format(cal.getTime()) + "\n", "Logs/button.log");
>>>>>>> e9036da6520ecfa0b6db4ccdd8371b2de8e855d6
=======
		Logger.write("BUTTON PRESSED AT " + time.format(cal.getTime()) + "\n", "Logs/button.log");
>>>>>>> 03b03e858e6371579de5641b52a27d38b8c55db3
		this.pressed = pressed;
		if (pressed) System.out.println(name+" PRESSED.");
		else System.out.println(name+" UNPRESSED.");
	}
	
	public void setActive(boolean active) {
		if(active)
		Logger.write("Button Activated at " + time.format(cal.getTime()), "Logs/button.log");
		else
			Logger.write("Button is not active at " + time.format(cal.getTime()), "Logs/button.log");
		
		this.active = active;
		if (pressed) System.out.println(name+" active.");
		else System.out.println(name+" inactive.");
	}
	
	public boolean isPressed() {
		
		return this.pressed;
	}
	
	public boolean isActive() {
		
		return this.active;
	}
//public static void main(String[] args) {
//	
//	Button btn = new Button ("Button", false);
//	btn.setPressed(true);
//
//	}
	
	
}
