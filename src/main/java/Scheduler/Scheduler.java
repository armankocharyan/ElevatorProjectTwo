package Scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	public static final String ADDRESS = "172.17.70.139"; //Change this to the address of the floor and elevator PC. Leave it blank ("") to run locally

	EventListener listener;
	public static final String schedulerTestLogFileName = "TestLogs/scheduler.testing";
	EventNotifier elevatorNotifier;
	EventNotifier floorNotifier;
	
	long[] startTimesReq;
	long[] startTimesTrip;

	private Calendar cal; 
	private SimpleDateFormat time;

	Queue<ElevatorMessage> queue = new LinkedList<ElevatorMessage>(); // our queue of requests
	int processing = 0; // if this is > 0, we have an elevator moving to a floor to respond to a
						// request. 1 = 1 car occupied, 2 = both cars occupied, etc.
	int numCars = 0;
	
	boolean[] requestNotServed;
	boolean[] passStuck;
	
	public Scheduler(int numCars, int numFloors) {
		listener = new EventListener(PORT, "SCHEDULER LISTENER");
		elevatorNotifier = new EventNotifier(ElevatorController.PORT, "SCHEDULER ELEVATOR NOTIFIER");
		floorNotifier = new EventNotifier(FloorController.PORT, "SCHEDULER FLOOR NOTIFIER");
		this.numCars = numCars;
		time = new SimpleDateFormat("HH:mm:ss.SSS");
		
		requestNotServed = new boolean[numFloors];
		passStuck = new boolean[numFloors];
		for(int i=0; i<numFloors; i++) {
			requestNotServed[i] = false;
			passStuck[i] = false;
		}

		startTimesReq = new long[numFloors];
		startTimesTrip = new long[numFloors];
	}

	public void startListen() throws InterruptedException {
		// THIS ACTS AS A PRODUCER
		cal = Calendar.getInstance();
		System.out.println("SCHEDULER: Starting listener...");
		Logger.write("SCHEDULER: Starting listener...", schedulerTestLogFileName);
		Logger.write("SCHEDULER: Starting listener at " + time.format(cal.getTime()), "Logs/scheduler.log");
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
				synchronized(this) {
					passStuck[msg.getRequestedFloor()] = true;
					startTimesTrip[msg.getRequestedFloor()] = System.nanoTime();

				}
				int nfloor = msg.getRequestedFloor();
				new java.util.Timer().schedule(
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				                if (passStuck[nfloor]) {
				                	throwRuntime("PASSENGER STUCK");
				                }
				            }
				        },
				        30000
				);
				
				// the target address ("") is empty for now
				this.elevatorNotifier.sendMessage(msg, ADDRESS);
				
				break;
			case ELEV_PICKUP:
				System.out.println("\nSCHEDULER: RECEIVED ELEVATOR PICKUP NOTIFICATION " + msg);
				synchronized(this) {
					long end = System.nanoTime();
					double timeElapsed = (end - startTimesReq[msg.getFloor()])/1000000.00;
					System.out.println("TIME TO PICK UP: " + timeElapsed + " ms");
					Logger.write(""+ timeElapsed, "timer/timePickup.log");
					requestNotServed[msg.getFloor()] = false;
				}
				// the target address ("") is empty for now
				this.floorNotifier.sendMessage(msg, ADDRESS);
				break;
			case ELEV_ARRIVAL:
				System.out.println("\nSCHEDULER: RECEIVED ELEVATOR ARRIVAL NOTIFICATION " + msg);
				synchronized(this) {
					passStuck[msg.getFloor()] = false;
					long end = System.nanoTime();
					double timeElapsed = (end - startTimesTrip[msg.getFloor()])/1000000.00;
					Logger.write(""+timeElapsed, "timer/timeRide.log");
				}
				
				// the target address ("") is empty for now
				this.floorNotifier.sendMessage(msg, ADDRESS);
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
			
			int nFloor = msg.getId();
			requestNotServed[nFloor] = true;
			startTimesReq[nFloor] = System.nanoTime();
			
			// send notification to elevatorController that someone has requested a car
			// the target address ("") is empty for now
			this.elevatorNotifier.sendMessage(msg, ADDRESS);
			
			new java.util.Timer().schedule(
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			                if (requestNotServed[nFloor]) {
			                	
			                	throwRuntime("ELEVATOR TIMEOUT");
			                	
			                }
			            }
			        },
			        30000
			);
			
		}
	}

	void throwRuntime(String s) {
		throw new RuntimeException(s);
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
		
		Logger.clearAllLogFiles();
		Scheduler s = new Scheduler(2,8);
		s.start();
	}

}
