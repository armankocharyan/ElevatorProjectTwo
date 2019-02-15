package Floor;

import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;
import core.RequestData;
import File.ReadFile;
public class FloorController {
	
	public static final int PORT = 62442; // IGNORE THIS, CHANGING SOON
	
	int numFloors;
	Floor[] floors;
	
	EventListener listener;
	
	public FloorController(int numFloors) {
		this.numFloors = numFloors;
		
		// initialize <numfloors> floors
		floors = new Floor[numFloors];
		for(int i=0; i<numFloors; i++) {
			floors[i] = new Floor(i, (i == numFloors-1), (i==0));
		}
		
		// ignore this, changing soon
		for(Floor f : floors) {
			f.start();
		}
		
		// listens for notifications from the scheduler that an elevator has arrived
		listener = new EventListener(42424, "FLOOR ELEVATOR LISTENER");
	}
	
	
	public void startElevatorListen() {
		// listens for notifications from the scheduler that an elevator has arrived
		
		System.out.println("FLOOR CONTROLLER: Starting elevator listener...");
		
		for(;;) {
			ElevatorMessage msg = listener.waitForNotification();
			System.out.println("\nFLOOR CONTROLLER: RECEIVED ELEVATOR NOTIFICATION " + msg);
			
			// ELEVATOR ARRIVED ON FLOOR (msg.getCurrentFloor())
			EventNotifier notif = new EventNotifier(floors[msg.getCurrentFloor()].getPort(), "ARRIVAL NOTIFIER");
			
			// ignore this, changing soon
			notif.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			
			// if we have a destination (ie. movingTo isn't -1), then we know we have just requested the elevator
			// so we send a message to the scheduler saying we have boarded the elevator so it can move to the destination
			if(msg.getMovingTo() != -1) {
				notif = new EventNotifier(25, "OCCUPANCY NOTIFIER");
				notif.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			}
			
			
		}
	}
	
	public void start() throws InterruptedException {
		FloorController s = this;
		
		// start the thread that listens for notifications from the scheduler
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevatorListen();
			}
		});
		
		
		t2.start();
		
		/* THIS IS WHERE YOU WOULD CALL THE INPUT FILE METHOD */
		//Input file
		
		ArrayList<RequestData> inputData = ReadFile.getData("inputFile.txt");
		// everything below this is just demo runs
		Thread.sleep(5000);
		
		if(inputData.get(0).goingUp()) {
			floors[inputData.get(0).getFloorNumber()].reqUp(inputData.get(0).getfloorToGo());
		}
		else {
			floors[inputData.get(0).getFloorNumber()].reqDown(inputData.get(0).getfloorToGo());
		}
		
		
		Thread.sleep(30);
		
		if(inputData.get(1).goingUp()) {
			floors[inputData.get(1).getFloorNumber()].reqUp(inputData.get(1).getfloorToGo());
		}
		else {
			floors[inputData.get(1).getFloorNumber()].reqDown(inputData.get(1).getfloorToGo());
		}
		
		if(inputData.get(2).goingUp()) {
			floors[inputData.get(2).getFloorNumber()].reqUp(inputData.get(2).getfloorToGo());
		}
		else {
			floors[inputData.get(2).getFloorNumber()].reqDown(inputData.get(2).getfloorToGo());
		}
		
	}
	
	public static void main(String[] args) {
		FloorController c = new FloorController(5);
		try {
			c.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
