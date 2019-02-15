package Floor;

import core.Button;
import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;
import core.Lamp;

public class Floor{
	
	public static int NEXT_PORT = 62442;  // ignore this im getting rid of it tmo
	
	int port = -1; // ignore this, same thing
	
	int floorNum = -1;	
	boolean highestFloor = false;  // highest floors have no up requests
	boolean lowestFloor = false;  // lowest floors have no down requests
	
	EventListener arrivalSensor = null; // our arrival sensor is just a receiving socket
	EventNotifier notifier = null; // notifies the scheduler that we requested an elevator
	EventNotifier occupancyNotifier = null; // notifies the scheduler that we entered an elevator and are ready to go to the floor we want
	
	Button reqBtnUp = null; 
	Button reqBtnDown = null;
	
	Lamp reqLampUp = null;
	Lamp reqLampDown = null;
	
	Lamp directionLampUp = null;
	Lamp directionLampDown = null;
	
	
	public Floor(int num, boolean highestFloor, boolean lowestFloor) {
		this.floorNum = num;
		this.highestFloor = highestFloor;
		this.lowestFloor = lowestFloor;
		
		// buttons, button lamps, and direction lamps
		if(!highestFloor) {
			String btnString = "Floor " + floorNum + " Request UP btn";
			String lampString = "Floor " + floorNum + " Request UP lamp";
			reqBtnUp = new Button(btnString, true);
			reqLampUp = new Lamp(lampString,false);
		}
		if(!lowestFloor) {
			String btnString = "Floor " + floorNum + " Request DOWN btn";
			String lampString = "Floor " + floorNum + " Request DOWN lamp";
			reqBtnDown = new Button(btnString, true);
			reqLampDown = new Lamp(lampString,false);
		}
		String btnString = "Floor " + floorNum + " direction UP lamp";
		this.directionLampUp = new Lamp(btnString, false);
		btnString = "Floor " + floorNum + " direction DOWN lamp";
		this.directionLampDown = new Lamp(btnString, false);
		
		
		// ignore this
		this.port = NEXT_PORT++;
		
		// event listeners/notifiers initialization
		this.arrivalSensor = new EventListener(this.port, "ARRIVAL SENSOR");
		this.notifier = new EventNotifier(23, "FLOOR NOTIFIER");
		this.occupancyNotifier = new EventNotifier(24, "OCCUPANCY NOTIFIER");
	}
	
	public void reqUp(int num) {
		// num -> floor we want to go to
		// send request from floor (this.floorNum) to floor num
		
		if (highestFloor) {
			// highest floors can't go up
			throw new IllegalStateException();
		}
		
		// turn on button lamp and press the button
		reqLampUp.setOn(true);
		reqBtnUp.setPressed(true);
		
		// send notification to scheduler
		this.notifier.sendNotif(1, floorNum, num);
		
	
	}
	
	public void reqDown(int num) {
		// num -> floor we want to go to
		// send request from floor (this.floorNum) to floor num
		if (lowestFloor) {
			// lowest floors can't go down
			throw new IllegalStateException();
		}
		
		// turn on button lamp and press the button
		reqLampDown.setOn(true);
		reqBtnDown.setPressed(true);
		
		
		this.notifier.sendNotif(2, floorNum, num);
		
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
	
	public void listen() {
		// IGNORE THIS, CHANGING THIS SOON
		System.out.println("FLOOR "+floorNum+": Starting arrival sensor...");
		
		for(;;) {
			ElevatorMessage msg = arrivalSensor.waitForNotification();
			System.out.println("\nFLOOR " + floorNum + ": ELEVATOR ARRIVED. " + msg);
			if (msg.getDirection() == 1) {
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
	}
	
	public void start() {
		Floor floor = this;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				floor.listen();
			}
		});
		t.start();
	}
	
	
	int getPort() {
		return this.port;
	}
	
	
}
