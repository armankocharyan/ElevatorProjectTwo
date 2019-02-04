package Scheduler;


import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;

public class Scheduler {
	
	EventListener floorListener;
	EventNotifier floorReqNotifier;
	
	EventListener elevListener;
	EventNotifier floorNotifier;
	
	EventListener elevEnteredListener;
	EventNotifier elevEnteredNotifier;

	
	
	public Scheduler() {
		floorListener = new EventListener(23,"FLOOR REQUEST LISTENER");
		floorReqNotifier = new EventNotifier(28, "ELEVATOR REQUEST NOTIFIER");
		
		elevListener = new EventListener(24, "ELEVATOR LISTENER");
		floorNotifier = new EventNotifier(42424, "SCHEDULER ELEVATOR NOTIFIER");
		
		elevEnteredListener = new EventListener(25, "ELEVATOR OCCUPANCY LISTENER");
		elevEnteredNotifier = new EventNotifier(30, "ELEVATOR OCCUPANCY NOTIFIER");
		
		
	}
	
	public void startFloorListen() {
		System.out.println("SCHEDULER: Starting floor listener...");
		
		for(;;) {
			ElevatorMessage msg = floorListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED FLOOR REQUEST " + msg);
			this.floorReqNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
		}
	}
	
	public void startElevatorListen() {
		System.out.println("SCHEDULER: Starting elevator listener...");
		
		for(;;) {
			ElevatorMessage msg = elevListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED ELEVATOR NOTIFICATION " + msg);
			this.floorNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			
		}
	}
	
	public void startElevEnterListen() {
		System.out.println("SCHEDULER: Starting elevator occupancy listener...");
		
		for(;;) {
			ElevatorMessage msg = elevEnteredListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED OCCUPANCT NOTIFICATION " + msg);
			this.elevEnteredNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			
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
		
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevEnterListen();
			}
		});
		
		
		t1.start();
		t2.start();
		t3.start();
	}
	
	public static void main(String[] args) {
		Scheduler s = new Scheduler();
		s.start();
	}

}
