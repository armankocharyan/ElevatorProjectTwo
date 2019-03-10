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

		Logger.write("BUTTON PRESSED AT " + time.format(cal.getTime()) + "\n", "Logs/button.log");

		this.pressed = pressed;
		if (pressed) System.out.println(name+" PRESSED.");
		else System.out.println(name+" UNPRESSED.");
	}
	
	public void setActive(boolean active) {
		if(active)
		Logger.write("BUTTON ACTIVATED AT " + time.format(cal.getTime()), "Logs/button.log");
		else
			Logger.write("BUTTON IS DEACTIVATED AT " + time.format(cal.getTime()), "Logs/button.log");
		
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
