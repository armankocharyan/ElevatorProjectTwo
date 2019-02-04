package Floor;

import core.Button;
import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;
import core.Lamp;

public class Floor{
	
	public static int NEXT_PORT = 62442;
	
	int port = -1;
	
	int floorNum = -1;	
	boolean highestFloor = false;
	boolean lowestFloor = false;
	
	EventListener arrivalSensor = null;
	EventNotifier notifier = null;
	
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
		
		this.port = NEXT_PORT++;
		this.arrivalSensor = new EventListener(this.port, "ARRIVAL SENSOR");
		this.notifier = new EventNotifier(23, "FLOOR NOTIFIER");
	}
	
	public void reqUp(int num) {
		//send request
		if (highestFloor) {
			throw new IllegalStateException();
		}
		this.notifier.sendNotif(1, floorNum, num);
		reqLampUp.setOn(true);
		reqBtnUp.setPressed(true);
	}
	
	public void reqDown(int num) {
		//send request
		if (lowestFloor) {
			throw new IllegalStateException();
		}
		this.notifier.sendNotif(2, floorNum, num);
		reqLampDown.setOn(true);
		reqBtnDown.setPressed(true);
	}
	
	public void listen() {
		
		System.out.println("FLOOR "+floorNum+": Starting arrival sensor...");
		
		for(;;) {
			ElevatorMessage msg = arrivalSensor.waitForNotification();
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
