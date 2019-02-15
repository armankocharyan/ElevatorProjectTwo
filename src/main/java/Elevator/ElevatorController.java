package Elevator;

import core.ElevatorMessage;
import core.EventListener;

public class ElevatorController {
	// Controls elevators and receives messages from the scheduler
	
	Elevator[] elevators = null;
	int numElevators = 0; // number of elevator cars
	int numFloors; 
	
	
	EventListener requestListener = null; // listens for a person pushing the elevator request button
	EventListener occupancyListener = null; // waits for the person to get into the elevator before moving to the destination
	
	public ElevatorController(int numElevators, int numFloors) {
		
		this.numElevators = numElevators;
		this.numFloors = numFloors;
		
		// initialize all your elevators
		elevators = new Elevator[numElevators];
		for(int i=0; i<numElevators; i++) {
			elevators[i] = new Elevator(i, numFloors);
		}
		
		
		requestListener = new EventListener(28, "ELEVATOR REQUEST LISTENER");
		occupancyListener= new EventListener(30, "ELEVATOR OCCUPANCY LISTENER");
	}
	
	public void startReqListen() {
		// starts a new thread/daemon that has a BLOCKING WAIT call, waits for a floor request from the scheduler
		System.out.println("ELEVATOR: Starting request listener...");
		
		for(;;) {
			ElevatorMessage msg = requestListener.waitForNotification();
			System.out.println("\nELEVATOR: RECEIVED FLOOR REQUEST " + msg);
			/* THIS IS WHERE WE WOULD RESPOND TO A REQUEST BY CHOOSING THE ELEVATOR,
			 * RIGHT NOW WE JUST RESPOND WITH THE FIRST ONE */
			
			// pick up the person who requested the elevator
			elevators[0].pickUpPerson(msg.getCurrentFloor(), msg.getDirection(), msg.getMovingTo());
		}
	}
	
	public void startOccupancyListen() {
		// starts a new thread/daemon that has a BLOCKING WAIT call, a person to get in an elevator before moving to the floor they want
		System.out.println("ELEVATOR: Starting occupanct listener...");
		
		for(;;) {
			ElevatorMessage msg = occupancyListener.waitForNotification();
			System.out.println("\nELEVATOR: RECEIVED OCCUPANCY NOTIFICATION " + msg);
			
			// ride to the destination
			elevators[0].rideToFloor(msg.getDirection(), msg.getMovingTo());
		}
	}
	
	public void start() {
		ElevatorController s = this;
		
		// start listening for floor requests
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startReqListen();
			}
		});
		
		
		// start listening for people entering the elevator
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startOccupancyListen();
			}
		});
		
		// both run in an infinite loop
		t1.start();
		t2.start();
	}
	
	public static void main(String[] args) {
		ElevatorController c = new ElevatorController(1, 5);
		c.start();
	}
	
}
