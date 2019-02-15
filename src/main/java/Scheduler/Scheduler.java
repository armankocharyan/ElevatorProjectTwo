package Scheduler;

import java.util.LinkedList;
import java.util.Queue;

import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;

public class Scheduler {

	// all of this will change
	EventListener floorListener;  
	EventNotifier floorReqNotifier;

	EventListener elevListener;
	EventNotifier floorNotifier;

	EventListener elevEnteredListener;
	EventNotifier elevEnteredNotifier;

	// everything below this wont
	Queue<ElevatorMessage> queue = new LinkedList<ElevatorMessage>(); // our queue of requests
	int processing = 0; // if this is > 0, we have an elevator moving to a floor to respond to a request. 1 = 1 car occupied, 2 = both cars occupied, etc.

	public Scheduler() {
		floorListener = new EventListener(23, "FLOOR REQUEST LISTENER");
		floorReqNotifier = new EventNotifier(28, "ELEVATOR REQUEST NOTIFIER");

		elevListener = new EventListener(24, "ELEVATOR LISTENER");
		floorNotifier = new EventNotifier(42424, "SCHEDULER ELEVATOR NOTIFIER");

		elevEnteredListener = new EventListener(25, "ELEVATOR OCCUPANCY LISTENER");
		elevEnteredNotifier = new EventNotifier(30, "ELEVATOR OCCUPANCY NOTIFIER");

	}

	
	public void startFloorListen() throws InterruptedException {
		// THIS ACTS AS A PRODUCER
		System.out.println("SCHEDULER: Starting floor listener...");

		for (;;) {
				// listens for a person requesting an elevator
				ElevatorMessage msg = floorListener.waitForNotification();
				System.out.println("\nSCHEDULER: RECEIVED FLOOR REQUEST " + msg);
				
				synchronized(this) {
					// when a person requests an elevator, add that request to our queue of requests
					queue.add(msg);
					// and let all waiting threads know there is a new request
					notifyAll();
				}
				
				
			}

		
	}

	
	public synchronized void dequeue() throws InterruptedException {
		// THIS ACTS AS A CONSUMER
		for (;;) {
				while (queue.isEmpty() || processing >= 1) {
					// WAIT while our queue is empty OR while all cars are occupied (processing >= numCars)
					wait();
				}
				
				// now we are processing one request, so one car is occupied
				processing += 1;
				
				// get the request and remove it from the queue
				ElevatorMessage msg = queue.remove();
				
				// send notification to elevatorController that someone has requested a car
				this.floorReqNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
				
			}
	}

	public void startElevatorListen() {
		// this receives notifications from the elevatorController saying a car has arrived on a floor
		System.out.println("SCHEDULER: Starting elevator listener...");

		for (;;) {
			ElevatorMessage msg = elevListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED ELEVATOR NOTIFICATION " + msg);
			
			// send the notification to the floorController
			this.floorNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			synchronized(this) {
				if (msg.getMovingTo() == -1) {
					// if we have no destination (movingTo == -1) then our elevator car is UNOCCUPIED, so one car frees up for processing
					processing -= 1;
					
					// let the waiting threads know that we are free to process another thread
					notifyAll();
				}
			}
			

		}
	}

	public  void startElevEnterListen() {
		// waits for notifications from the floor controller saying a person has entered an elevator and is waiting to go to their floor
		System.out.println("SCHEDULER: Starting elevator occupancy listener...");

		for (;;) {
			ElevatorMessage msg = elevEnteredListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED OCCUPANCT NOTIFICATION " + msg);
			
			// sends the notification to the elevator controller
			this.elevEnteredNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());

		}
	}

	public void start() {
		Scheduler s = this;
		
		// producer
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s.startFloorListen();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// consumer
		Thread t4 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s.dequeue();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// elevator arrival notifications 
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevatorListen();
			}
		});

		// elevator occupied notification
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevEnterListen();
			}
		});

		t2.start();
		t3.start();
		t4.start();
		t1.start();
	}

	public static void main(String[] args) {
		Scheduler s = new Scheduler();
		s.start();
	}

}
