package Elevator;

import core.ElevatorMessage;
import core.EventListener;

public class ElevatorController {
	// Controls elevators and receives messages from the scheduler
	
	public static final int PORT = 69;
	
	Elevator[] elevators = null;
	int numElevators = 0; // number of elevator cars
	int numFloors; 
	
	EventListener messageListener = null;
	
	public ElevatorController(int numElevators, int numFloors) {
		
		this.numElevators = numElevators;
		this.numFloors = numFloors;
		
		// initialize all your elevators
		elevators = new Elevator[numElevators];
		for(int i=0; i<numElevators; i++) {
			elevators[i] = new Elevator(i, numFloors);
		}
		
		messageListener = new EventListener(PORT, "ELEVATOR CONTROLLER");
	}
	
	public void startListen() {
		// starts a new thread/daemon that has a BLOCKING WAIT call, waits for a floor request from the scheduler
		System.out.println("ELEVATOR CONTROLLER: Starting message listener...");
		
		for(;;) {
			ElevatorMessage msg = messageListener.waitForNotification();
			
			switch(msg.getType()) {
			case ELEV_REQUEST:
				System.out.println("\nELEVATOR CONTROLLER: RECEIVED ELEVATOR REQUEST NOTIFICATION" + msg);
				/* THIS IS WHERE WE WOULD RESPOND TO A REQUEST BY CHOOSING THE ELEVATOR,
				 * RIGHT NOW WE JUST RESPOND WITH THE FIRST ONE */
				
				// pick up the person who requested the elevator
				
				for(Elevator elevator : elevators) {
					if (elevator.isOccupied()) continue;
					elevator.pickUpPerson(msg.getId(),msg.getDirection());
					break;
				}
				
				break;
			case PASSENGER_ENTER:
				System.out.println("\nELEVATOR CONTROLLER: RECEIVED PASSENGER ENTERED NOTIFICATION" + msg);
				
				int car = msg.getCarNum();
				// ride to the destination
				elevators[car].rideToFloor(msg.getRequestedFloor());
				break;
			default:
				System.out.println("\nELEVATOR CONTROLLER: RECEIVED UNKNOWN NOTIFICATION " + msg);
				break;
			}
		}
	}
	

	
	public void start() {
		ElevatorController s = this;
		
		// start listening for floor requests
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startListen();
			}
		});
		
		t1.start();
	}
	
	public static void runElevator() {
		ElevatorController c = new ElevatorController(2, 8);
		c.start();
	}
	
	public static void main(String[] args) {
		ElevatorController c = new ElevatorController(2, 8);
		c.start();
	}
	
}
