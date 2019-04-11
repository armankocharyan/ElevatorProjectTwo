package Floor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Logger.Logger;
import Scheduler.Scheduler;
import core.Button;
import core.Constants;
import core.ElevatorMessage;
import core.EventNotifier;
import core.Lamp;

public class Floor {
	
	int floorNum;
	public static final String floorTestLogFileName= "TestLogs/floor.testing";
	
	Button reqBtnUp = null; 
	Button reqBtnDown = null;
	
	Lamp reqLampUp = null;
	Lamp reqLampDown = null;
	
	Lamp directionLampUp = null;
	Lamp directionLampDown = null;
	
	EventNotifier notifier = null; // notifies the scheduler that we requested an elevator

	int reqUp = -1;
	int reqDown = -1;
	

	Calendar cal;
	SimpleDateFormat time;
	String fileName = "Logs/floor.log";
	
	public Floor(int num) {
		floorNum = num;
		
		reqBtnUp = new Button("Floor " + floorNum + " Request UP btn", true);
		reqLampUp = new Lamp("Floor " + floorNum + " Request UP lamp",false);
		
		reqBtnDown = new Button("Floor " + floorNum + " Request DOWN btn", true);
		reqLampDown = new Lamp("Floor " + floorNum + " Request DOWN lamp",false);
		
		this.directionLampUp = new Lamp("Floor " + floorNum + " direction UP lamp", false);
		this.directionLampDown = new Lamp("Floor " + floorNum + " direction DOWN lamp", false);
		
		this.notifier = new EventNotifier(Constants.SCHED_PORT, "FLOOR NOTIFIER");
		this.time = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	public void reqUp(int num, int fault) {
		// num -> floor we want to go to
		// send request from floor (this.floorNum) to floor num
		System.out.println("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING UP");
		cal = Calendar.getInstance();
		Logger.write("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING UP " + time.format(cal.getTime()), fileName); 
		Logger.write("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING UP " + time.format(cal.getTime()), floorTestLogFileName); 
		reqLampUp.setOn(true);
		reqBtnUp.setPressed(true);
		this.notifier.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.REQ, this.floorNum, Constants.DIR.UP.getCode(), num, fault), Constants.SCHEDULER_ADDR);
		this.time = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	public void reqDown(int num, int fault) {
		// num -> floor we want to go to
		// send request from floor (this.floorNum) to floor num
		
		
		System.out.println("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING DOWN");
		cal = Calendar.getInstance();
		Logger.write("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING DOWN " + time.format(cal.getTime()), fileName); 
		Logger.write("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING DOWN " + time.format(cal.getTime()), floorTestLogFileName); 
		reqLampDown.setOn(true);
		reqBtnDown.setPressed(true);
		this.notifier.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.REQ, this.floorNum, Constants.DIR.DOWN.getCode(), num, fault), Constants.SCHEDULER_ADDR);
	}
	
	public void resetDownBtn() {
		// unpresses button and turns off button lamp
			reqLampDown.setOn(false);
			reqBtnDown.setPressed(false);
	}
	
	public void resetUpBtn() {
		// unpresses button and turns off button lamp
			reqLampUp.setOn(false);
			reqBtnUp.setPressed(false);
	}
	
	public void elevArrival(Constants.DIR direction, int carNum) {
		System.out.println("\nFLOOR " + floorNum + " ELEVATOR CAR "+ carNum + " ARRIVAL.");
		cal = Calendar.getInstance();
		Logger.write("\nFLOOR " + floorNum + " ELEVATOR CAR "+ carNum + " ARRIVAL. " + time.format(cal.getTime()), fileName); 
		Logger.write("\nFLOOR " + floorNum + " ELEVATOR CAR "+ carNum + " ARRIVAL. " + time.format(cal.getTime()), floorTestLogFileName); 
		if (direction == Constants.DIR.UP) {
			resetUpBtn();
			directionLampUp.setOn(true);
			directionLampDown.setOn(false);
		}
		else {
			resetDownBtn();
			directionLampUp.setOn(false);
			directionLampDown.setOn(true);
		}
	}
	
	public void passengerEnter(int carNum, Constants.DIR dir) {
		int floor = (dir == Constants.DIR.UP) ? reqUp : reqDown;
		
		if (floor == -1) return;
		
		System.out.println("\nPASSENGER ON FLOOR "+ floorNum + " ENTERED ELEVATOR CAR " + carNum + " TO FLOOR " + floor);
		cal = Calendar.getInstance();
		Logger.write("\nPASSENGER ON FLOOR "+ floorNum + " ENTERED ELEVATOR CAR " + carNum + " TO FLOOR " + floor + " " + time.format(cal.getTime()), fileName); 
		Logger.write("\nPASSENGER ON FLOOR "+ floorNum + " ENTERED ELEVATOR CAR " + carNum + " TO FLOOR " + floor + " " + time.format(cal.getTime()), floorTestLogFileName);
		if (dir == Constants.DIR.UP) {
			reqUp = -1;
		}else {
			reqDown = -1;
		}

	}

}
