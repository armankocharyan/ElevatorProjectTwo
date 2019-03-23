package Floor;

import core.Button;
import core.ElevatorMessage;
import core.EventNotifier;
import core.Lamp;
import Scheduler.Scheduler;
 
// TODO : Floor documentation
public class Floor{
	// -- STATIC VARIABLES -- //
	public static final String floorTestLogFileName = "TestLogs/floor.testing";
	public static final String ADDRESS = ""; //Change this to the address of the scheduler PC
	
	
	int floorNum = -1;	
	boolean highestFloor = false;  // highest floors have no up requests
	boolean lowestFloor = false;  // lowest floors have no down requests
	
	EventNotifier notifier = null; // notifies the scheduler that we requested an elevator
	
	Button reqBtnUp = null; 
	Button reqBtnDown = null;
	
	Lamp reqLampUp = null;
	Lamp reqLampDown = null;
	
	Lamp directionLampUp = null;
	Lamp directionLampDown = null;
	
	int requestFloor = -1;
	
	public Floor(int num, boolean highestFloor, boolean lowestFloor) {
		this.floorNum = num;
		this.highestFloor = highestFloor;
		this.lowestFloor = lowestFloor;
		
		// buttons, button lamps, and direction lamps
		if(!highestFloor) {
			reqBtnUp = new Button("Floor " + floorNum + " Request UP btn", true);
			reqLampUp = new Lamp("Floor " + floorNum + " Request UP lamp",false);
		}
		if(!lowestFloor) {
			reqBtnDown = new Button("Floor " + floorNum + " Request DOWN btn", true);
			reqLampDown = new Lamp("Floor " + floorNum + " Request DOWN lamp",false);
		}
		this.directionLampUp = new Lamp("Floor " + floorNum + " direction UP lamp", false);
		this.directionLampDown = new Lamp("Floor " + floorNum + " direction DOWN lamp", false);
		
		
		this.notifier = new EventNotifier(Scheduler.PORT, "FLOOR NOTIFIER");
	}
	
	public void reqUp(int num) {
		// num -> floor we want to go to
		// send request from floor (this.floorNum) to floor num
		
		System.out.println("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING UP");
		Logger.Logger.write("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING UP", floorTestLogFileName);
		if (highestFloor) {
			// highest floors can't go up
			throw new IllegalStateException();
		}
		
		requestFloor = num;
		// turn on button lamp and press the button
		reqLampUp.setOn(true);
		reqBtnUp.setPressed(true);
		
		// send notification to scheduler
		// the target address ("") is empty for now
		this.notifier.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.ELEV_REQUEST, this.floorNum, 1), ADDRESS);
	}
	
	public void reqDown(int num) {
		// num -> floor we want to go to
		// send request from floor (this.floorNum) to floor num
		
		System.out.println("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING DOWN");
		Logger.Logger.write("\nFLOOR "+ floorNum + " REQUESTED AN ELEVATOR GOING DOWN", floorTestLogFileName);
		if (lowestFloor) {
			// lowest floors can't go down
			throw new IllegalStateException();
		}
		
		requestFloor = num;
		// turn on button lamp and press the button
		reqLampDown.setOn(true);
		reqBtnDown.setPressed(true);
		
		// the target address ("") is empty for now
		this.notifier.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.ELEV_REQUEST, this.floorNum, 2), ADDRESS);
	}
	
	public void resetDownBtn() {
		// unpresses button and turns off button lamp
		if(!lowestFloor) {
			reqLampDown.setOn(false);
			reqBtnDown.setPressed(false);
		}
	}
	
	public void resetUpBtn() {
		// unpresses button and turns off button lamp
		if(!highestFloor) {
			reqLampUp.setOn(false);
			reqBtnUp.setPressed(false);
		}
	}
	
	public void elevArrival(int direction, int carNum) {
		System.out.println("\nFLOOR " + floorNum + " ELEVATOR CAR "+ carNum + " ARRIVAL.");
		Logger.Logger.write("\nFLOOR " + floorNum + " ELEVATOR CAR "+ carNum + " ARRIVAL.", floorTestLogFileName);
		if (direction == 1) {
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
	
	public void passengerEnter(int carNum) {
		if(requestFloor == -1) {
			return;
		}
		System.out.println("\nPASSENGER ON FLOOR "+ floorNum + " ENTERED ELEVATOR CAR " + carNum + " TO FLOOR " + requestFloor);
		Logger.Logger.write("\nPASSENGER ON FLOOR "+ floorNum + " ENTERED ELEVATOR CAR " + carNum + " TO FLOOR " + requestFloor, floorTestLogFileName);
		// the target address ("") is empty for now
		this.notifier.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.PASSENGER_ENTER, this.floorNum, carNum, this.requestFloor), ADDRESS);
		this.requestFloor = -1;
	}
}
