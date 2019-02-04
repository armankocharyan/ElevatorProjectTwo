package Scheduler;


import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;

public class Scheduler {
	
	EventListener floorListener;
	EventListener elevListener;
	EventNotifier floorNotifier;
	
	
	public Scheduler() {
		floorListener = new EventListener(23,"FLOOR REQUEST LISTENER");
		elevListener = new EventListener(24, "ELEVATOR LISTENER");
		floorNotifier = new EventNotifier(42424, "SCHEDULER ELEVATOR NOTIFIER");
	}
	
	public void startFloorListen() {
		System.out.println("SCHEDULER: Starting floor listener...");
		
		for(;;) {
			ElevatorMessage msg = floorListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED FLOOR REQUEST " + msg);
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
