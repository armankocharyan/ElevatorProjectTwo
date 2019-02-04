package Scheduler;

import Elevator.ElevatorMessage;
import Floor.FloorMessage;

public class Scheduler {
	
	FloorRequestListener floorListener;
	ElevatorListener elevListener;
	ElevatorDispatcher dispatcher;
	
	
	public Scheduler() {
		floorListener = new FloorRequestListener();
		elevListener = new ElevatorListener();
		dispatcher = new ElevatorDispatcher();
	}
	
	public void startFloorListen() {
		System.out.println("SCHEDULER: Starting floor listener...");
		
		for(;;) {
			FloorMessage msg = floorListener.waitForFloorRequest();
			System.out.println("\nSCHEDULER: RECEIVED FLOOR REQUEST " + msg);
		}
	}
	
	public void startElevatorListen() {
		System.out.println("SCHEDULER: Starting elevator listener...");
		
		for(;;) {
			ElevatorMessage msg = elevListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED ELEVATOR NOTIFICATION " + msg);
			this.dispatcher.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			
		}
	}
	
	public void start() {
		Scheduler s = this;
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startFloorListen();
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevatorListen();
			}
		});
		
		
		t1.start();
		t2.start();
	}
	
	public static void main(String[] args) {
		Scheduler s = new Scheduler();
		s.start();
	}

}
