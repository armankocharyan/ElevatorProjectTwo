package Floor;

import Elevator.ElevatorMessage;
import core.Button;
import core.Lamp;

public class Floor{
	
	int port = -1;
	
	int floorNum = -1;	
	boolean highestFloor = false;
	boolean lowestFloor = false;
	
	ArrivalSensor arrivalSensor = null;
	Dispatcher dispatcher = null;
	
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
		
		this.arrivalSensor = new ArrivalSensor();
		this.port = arrivalSensor.getPort();
		this.dispatcher = new Dispatcher();
	}
	
	public void reqUp(int num) {
		//send request
		if (highestFloor) {
			throw new IllegalStateException();
		}
		dispatcher.sendUpRequest(floorNum, num);
		reqLampUp.setOn(true);
		reqBtnUp.setPressed(true);
	}
	
	public void reqDown(int num) {
		//send request
		if (lowestFloor) {
			throw new IllegalStateException();
		}
		dispatcher.sendDownRequest(floorNum, num);
		reqLampDown.setOn(true);
		reqBtnDown.setPressed(true);
	}
	
	public void listen() {
		
		System.out.println("FLOOR "+floorNum+": Starting arrival sensor...");
		
		for(;;) {
			ElevatorMessage msg = arrivalSensor.waitForElevator();
			System.out.println("\nFLOOR " + floorNum + ": ELEVATOR ARRIVED. " + msg);
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
