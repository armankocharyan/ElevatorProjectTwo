package Elevator;

import java.util.ArrayList;

import core.Constants;
import core.Constants.DIR;
import core.ElevatorMessage;
import core.EventListener;

public class ElevatorController {
	
	Elevator[] elevators = null;
	EventListener m_messageListener = null;
	
	
	public ElevatorController() {
		elevators = new Elevator[Constants.NUM_CARS];
	    for(int i=0; i<Constants.NUM_CARS; i++) {
	    	elevators[i] =  new Elevator(i);
	    }

	    m_messageListener = new EventListener(Constants.ELEV_PORT, "ELEVATOR CONTROLLER");
	}
	
	public void startListen() {
			
			// starts a new thread/daemon that has a BLOCKING WAIT call, waits for a floor request from the scheduler
			System.out.println("ELEVATOR CONTROLLER: Starting message listener...");
			for(;;) {
				ElevatorMessage msg = m_messageListener.waitForNotification();
				switch(msg.getType()) {
				case REQ:
					System.out.println("\nELEVATOR CONTROLLER: RECEIVED ELEVATOR REQUEST NOTIFICATION" + msg);
					for(Elevator elev : elevators) {
						if (elev.dir == DIR.NONE) {
							elev.receiveRequest(msg.getId(), Constants.DIR.fromCode(msg.getData().get(0)));
							break;
						}
					}
					break;
				default:
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
		ElevatorController c = new ElevatorController();
		c.start();
	}
}
		
