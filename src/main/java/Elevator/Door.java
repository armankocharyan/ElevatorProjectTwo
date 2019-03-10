package Elevator;

import core.Button;
import Logger.Logger;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Door {
	
	
	private Calendar cal; 
	private SimpleDateFormat time;

	boolean isOpen = false;
	
	public Door(boolean isOpen) {
		cal = Calendar.getInstance();
		time = new SimpleDateFormat("HH:mm:ss.SSS");
		
		this.isOpen = isOpen;
	}
	
	public void open() {
			Logger.write("Door is Open at " + time.format(cal.getTime()), "Logs/door.log");
		
		this.isOpen = true;
	}
	
	public void close() {
		Logger.write("Door is Closed at " + time.format(cal.getTime()), "Logs/door.log");
		this.isOpen = false;
	}
	
	public boolean isOpen() {
		return this.isOpen();
	}
	
//	public static void main(String[] args) {
//		
//		Door doorOpn = new Door (false);
//		doorOpn.open();
//		doorOpn.close();
//		
//
//		}
}
