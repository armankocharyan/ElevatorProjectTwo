package Elevator;

import core.Constants;
import core.ElevatorMessage;
import core.EventListener;

// TODO : ElevatorController documentation
public class ElevatorController {
	// Controls elevators and receives messages from the scheduler
	
	Elevator[] m_elevators = null;
	EventListener m_messageListener = null;
	
	public ElevatorController() {
		
		m_elevators = new Elevator[Constants.NUM_CARS];
		for(int i=0; i<Constants.NUM_CARS; i++) {
			m_elevators[i] = new Elevator(i);
		}
		
		m_messageListener = new EventListener(Constants.ELEV_PORT, "ELEVATOR CONTROLLER");
	}
	
	public void startListen() {
		
		// starts a new thread/daemon that has a BLOCKING WAIT call, waits for a floor request from the scheduler
		System.out.println("ELEVATOR CONTROLLER: Starting message listener...");
		
		for(;;) {
			ElevatorMessage msg = m_messageListener.waitForNotification();
			System.out.println(m_elevators[0]);
			switch(msg.getType()) {
			case ELEV_REQUEST:
				System.out.println("\nELEVATOR CONTROLLER: RECEIVED ELEVATOR REQUEST NOTIFICATION" + msg);
				 //THIS IS WHERE WE WOULD RESPOND TO A REQUEST BY CHOOSING THE ELEVATOR,
				 //RIGHT NOW WE JUST RESPOND WITH THE FIRST ONE 
				
				// pick up the person who requested the elevator
				/*
				for(Elevator elevator : elevators) {
					if (elevator.isOccupied()) continue;
					elevator.pickUpPerson(msg.getId(),msg.getDirection());
					break;
				}*/
				
				break;
			case PASSENGER_ENTER:
				System.out.println("\nELEVATOR CONTROLLER: RECEIVED PASSENGER ENTERED NOTIFICATION" + msg);
				/*
				int car = msg.getCarNum();
				// ride to the destination
				elevators[car].rideToFloor(msg.getRequestedFloor());*/
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
		ElevatorController c = new ElevatorController();
		c.start();
	}
	
	public static void main(String[] args) {
		runElevator();
	}
	
}
