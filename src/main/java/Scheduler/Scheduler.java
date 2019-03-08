package Scheduler;

import java.util.LinkedList;
import java.util.Queue;

import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;

import Floor.FloorController;
import Elevator.ElevatorController;
import Logger.Logger;

// TODO : Scheduler documentation
public class Scheduler {

	public static final int PORT = 24;

	EventListener listener;
	public static final String schedulerTestLogFileName = "TestLogs/scheduler.testing";
	EventNotifier elevatorNotifier;
	EventNotifier floorNotifier;


	Queue<ElevatorMessage> queue = new LinkedList<ElevatorMessage>(); // our queue of requests
	int processing = 0; // if this is > 0, we have an elevator moving to a floor to respond to a
						// request. 1 = 1 car occupied, 2 = both cars occupied, etc.
	int numCars = 0;

	public Scheduler(int numCars) {
		listener = new EventListener(PORT, "SCHEDULER LISTENER");
		elevatorNotifier = new EventNotifier(ElevatorController.PORT, "SCHEDULER ELEVATOR NOTIFIER");
		floorNotifier = new EventNotifier(FloorController.PORT, "SCHEDULER FLOOR NOTIFIER");
		this.numCars = numCars;
	}

	public void startListen() throws InterruptedException {
		// THIS ACTS AS A PRODUCER
		System.out.println("SCHEDULER: Starting listener...");
		Logger.write("SCHEDULER: Starting listener...", schedulerTestLogFileName);
		for (;;) {
			ElevatorMessage msg = listener.waitForNotification();
			switch (msg.getType()) {
			case ELEV_REQUEST:
				System.out.println("\nSCHEDULER: RECEIVED ELEVATOR REQUEST " + msg);
				synchronized (this) {
					// when a person requests an elevator, add that request to our queue of requests
					queue.add(msg);
					// and let all waiting threads know there is a new request
					notifyAll();
				}
				break;
			case PASSENGER_ENTER:
				System.out.println("\nSCHEDULER: RECEIVED PASSENGER ENTER NOTIFICATION " + msg);
				this.elevatorNotifier.sendMessage(msg);
				break;
			case ELEV_PICKUP:
				System.out.println("\nSCHEDULER: RECEIVED ELEVATOR PICKUP NOTIFICATION " + msg);
				this.floorNotifier.sendMessage(msg);
				break;
			case ELEV_ARRIVAL:
				System.out.println("\nSCHEDULER: RECEIVED ELEVATOR ARRIVAL NOTIFICATION " + msg);
				this.floorNotifier.sendMessage(msg);
				synchronized (this) {
					processing -= 1;

					// let the waiting threads know that we are free to process another thread
					notifyAll();
				}
				break;
			}

		}

	}

	public synchronized void dequeue() throws InterruptedException {
		// THIS ACTS AS A CONSUMER
		for (;;) {
			while (queue.isEmpty() || processing >= numCars) {
				// WAIT while our queue is empty OR while all cars are occupied (processing >=
				// numCars)
				wait();
			}

			// now we are processing one request, so one car is occupied
			processing += 1;

			// get the request and remove it from the queue
			ElevatorMessage msg = queue.remove();
			
			
			// send notification to elevatorController that someone has requested a car
			this.elevatorNotifier.sendMessage(msg);
		}
	}

	public void start() {
		Scheduler s = this;

		// producer
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s.startListen();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// consumer
		Thread t2 = new Thread(new Runnable() {
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


		t2.start();
		t1.start();
	}

	public static void main(String[] args) {
		Scheduler s = new Scheduler(2);
		s.start();
	}

}
