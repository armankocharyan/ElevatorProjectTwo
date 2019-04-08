package Elevator;

import core.Button;
import Logger.Logger;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JRadioButton;

public class Door {
	
	
	private Calendar cal; 
	private SimpleDateFormat time;
	JRadioButton open;
	JRadioButton closed;
	
	boolean isOpen = false;
	
	public Door(boolean isOpen) {
		cal = Calendar.getInstance();
		time = new SimpleDateFormat("HH:mm:ss.SSS");
		
		this.isOpen = isOpen;
	}
	
	public Door(boolean isOpen, JRadioButton open, JRadioButton closed) {
		cal = Calendar.getInstance();
		time = new SimpleDateFormat("HH:mm:ss.SSS");
		
		this.open = open;
		this.closed = closed;
		
		this.isOpen = isOpen;
	}
	
	public void open() {
		cal = Calendar.getInstance();
		Logger.write("DOOR IS OPEN AT " + time.format(cal.getTime()) + "\n", "Logs/door.log");
		open.setSelected(true);
		closed.setSelected(false);
		
		this.isOpen = true;
	}
	
	public void close() {
		cal = Calendar.getInstance();
		Logger.write("DOOR IS CLOSED AT " + time.format(cal.getTime()) + "\n", "Logs/door.log");
		this.isOpen = false;
		
		open.setSelected(false);
		closed.setSelected(true);
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
