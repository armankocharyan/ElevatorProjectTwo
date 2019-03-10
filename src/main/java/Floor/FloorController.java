package Floor;

import core.ElevatorMessage;
import core.EventListener;
import core.RequestData;
import Logger.Logger;
import java.util.ArrayList;

import File.ReadFile;

// TODO : FloorController documentation
public class FloorController {
	public static final int PORT = 62442;
	
	
	int numFloors;
	Floor[] floors;
	
	EventListener listener;
	
	int isWaiting = -1;
	
	public FloorController(int numFloors) {
		this.numFloors = numFloors;
		
		// initialize <numfloors> floors
		floors = new Floor[numFloors];
		for(int i=0; i<numFloors; i++) {
			floors[i] = new Floor(i, (i == numFloors-1), (i==0));
		}
		
		// listens for notifications from the scheduler that an elevator has arrived
		listener = new EventListener(PORT, "FLOOR ELEVATOR LISTENER");
	}
	
	public boolean isWaiting() {
		return isWaiting > 0;
	}
	
	public void startListen() {
		// listens for notifications from the scheduler that an elevator has arrived
		
		System.out.println("FLOOR CONTROLLER: Starting elevator listener...");
		
		for(;;) {
			ElevatorMessage msg = listener.waitForNotification();
			
			switch(msg.getType()) {
			case ELEV_PICKUP:
				System.out.println("\nFLOOR CONTROLLER: RECEIVED FLOOR " + msg.getFloor() + " ELEVATOR PICKUP NOTIFICATION" + msg);
				floors[msg.getFloor()].elevArrival(msg.getDirection(), msg.getId());
				floors[msg.getFloor()].passengerEnter(msg.getId());
				isWaiting = -1;
				break;
			case ELEV_ARRIVAL:
				System.out.println("\nFLOOR CONTROLLER: RECEIVED FLOOR " + msg.getFloor() + " ELEVATOR ARRIVAL NOTIFICATION" + msg);
				floors[msg.getFloor()].elevArrival(msg.getDirection(), msg.getId());
				break;
			default:
					break;
			}
			
		}
	}
	
	public void start() {
		FloorController s = this;
		
		// start the thread that listens for notifications from the scheduler
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startListen();
			}
		});
		
		
		t1.start();
		
	}
	
	public void requestFloor(int from, int to) {
		
		if (to > from) floors[from].reqUp(to);
		else floors[from].reqDown(to);
		isWaiting = from;
	}
	
	public void readInput() {
		//Input file
				//TODO : make input file repeatable
				
				ArrayList<RequestData> inputData = ReadFile.getData("inputFile.txt");
				// everything below this is just demo runs
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(inputData.get(0).goingUp()) {
					floors[inputData.get(0).getFloorNumber()].reqUp(inputData.get(0).getfloorToGo());
				}
				else {
					floors[inputData.get(0).getFloorNumber()].reqDown(inputData.get(0).getfloorToGo());
				}
				
				
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
				
				if(inputData.get(3).goingUp()) {
					floors[inputData.get(3).getFloorNumber()].reqUp(inputData.get(3).getfloorToGo());
				}
				else {
					floors[inputData.get(3).getFloorNumber()].reqDown(inputData.get(3).getfloorToGo());
				}
	}
	
	public static void main(String[] args) {
		
		
		
		FloorController c = new FloorController(8);
		c.start();
		
		c.readInput();
	}
	
}
