package Elevator;

import core.ElevatorMessage;
import core.EventListener;

public class ElevatorController {

	Elevator[] elevators = null;
	int numElevators = 0;
	int numFloors;
	
	EventListener requestListener = null;
	EventListener occupancyListener = null;
	
	public ElevatorController(int numElevators, int numFloors) {
		this.numElevators = numElevators;
		this.numFloors = numFloors;
		elevators = new Elevator[numElevators];
		for(int i=0; i<numElevators; i++) {
			elevators[i] = new Elevator(i, numFloors);
		}
		
		requestListener = new EventListener(28, "ELEVATOR REQUEST LISTENER");
		occupancyListener= new EventListener(30, "ELEVATOR OCCUPANCY LISTENER");
	}
	
	public void startReqListen() {
		System.out.println("ELEVATOR: Starting request listener...");
		
		for(;;) {
			ElevatorMessage msg = requestListener.waitForNotification();
			System.out.println("\nELEVATOR: RECEIVED FLOOR REQUEST " + msg);
			elevators[0].pickUpPerson(msg.getCurrentFloor(), msg.getDirection(), msg.getMovingTo());
		}
	}
	
	public void startOccupancyListen() {
		System.out.println("ELEVATOR: Starting occupanct listener...");
		
		for(;;) {
			ElevatorMessage msg = occupancyListener.waitForNotification();
			System.out.println("\nELEVATOR: RECEIVED OCCUPANCY NOTIFICATION " + msg);
			elevators[0].rideToFloor(msg.getCurrentFloor(), msg.getDirection(), msg.getMovingTo());
		}
	}
	
	public void start() {
		ElevatorController s = this;
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startReqListen();
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startOccupancyListen();
			}
		});
		t1.start();
		t2.start();
	}
	
	public static void main(String[] args) {
		ElevatorController c = new ElevatorController(1, 5);
		c.start();
	}
	
}
