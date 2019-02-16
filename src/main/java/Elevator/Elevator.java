package Elevator;

import core.Button;
import core.EventNotifier;
import core.ElevatorMessage;
import core.Lamp;
import Scheduler.Scheduler;

/**
 * Represents a single elevator car connected to our ElevatorController. This class
 * tracks where our car is at any point in time and notifies our scheduler when it has 
 * arrived at a floor. 
 */
public class Elevator {
	// -- STATIC VARIABLES -- //
	public static final String elevatorTestLogFileName = "TestLogs/elevator.testing";
	
	// -- INSTANCE VARIABLES -- //
	int numFloors;
	int onFloor = 0;
	int[] destinationFloors = null;
	int direction = 1;
	int carNum = -1;
	boolean occupied = false;
	EventNotifier notif;
	
	// TODO : populate and implement our doors/motors/buttons/lamps in our Elevators
	Door door;
	Motor motor;
	Lamp[] floorLamps;
	Button[] floorBtns;
	
	// -- CONSTRUCTOR -- //
	public Elevator(int carNum, int numFloors) {
		this.numFloors = numFloors;
		this.onFloor = 0;
		this.direction = 1;
		this.carNum = carNum;
		this.destinationFloors = new int[numFloors];
		this.notif = new EventNotifier(Scheduler.PORT, "ELEVATOR");
	}
	
	// -- GETTERS -- //
	public int getCurrentFloor() {
		return onFloor;
	}
	
	/**
     * Getter for the elevator car's direction.
     * 
	 * @return 1 if direction is UP, 2 if direction is DOWN 
	 */
	public int getDirection() {
		return direction; // 1->UP, 2->DOWN
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	/**
	 * This is called when a floor opens a request for an elevator
	 * and this car is chosen to service the request. It will 
	 * move the elevator to the floor over a period of time and 
	 * send a notification to the scheduler when it has arrived.
	 * 
	 * @param floor the floor that has called the elevator.
	 * @param dir the direction requested.
	 */
	public void pickUpPerson(int floor, int dir) {
		System.out.println("\nPICKING UP PERSON ON FLOOR " + floor);
		Logger.Logger.write("\nPICKING UP PERSON ON FLOOR " + floor, elevatorTestLogFileName );
		occupied = true;
		
		try {
			// THIS IS WHERE WE WOULD IMPLEMENT TIMING FROM FLOOR TO FLOOR
			
			int time = Math.abs(onFloor - floor);
			if(dir == 1) System.out.println("Going up to " + floor + " floor to pick up someone");
			if(dir == 2) System.out.println("Going down to " + floor + " floor to pick up someone");
			
			
			for(int i = 0; i < time; i ++) {
				if(dir == 1) System.out.println("Current Floor " + onFloor ++);
				if(dir == 2) System.out.println("Current FLoor " + onFloor --);
				Thread.sleep(1000);
			}
			
			System.out.println("We have arrived at floor " + floor);
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// set our new current floor and direction
		this.onFloor = floor;
		this.direction = dir;
		// send notification to scheduler saying that we have arrived and which floor we are going to next
		this.notif.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.ELEV_PICKUP, this.carNum, this.direction, this.onFloor));
		
		
		// this does nothing yet, eventually when we have the GUI it should show the doors opening
		openDoors();
	}
	
	/**
	 * This is called when a passenger has entered the elevator and chosen 
	 * a floor to ride to. This will move our elevator to the floor requested
	 * over a period of time and notify the scheduler when it has arrived.
	 * 
	 * @param destination the floor we need to ride to.
	 */
	public void rideToFloor(int destination) {
		System.out.println("\nTAKING PERSON TO FLOOR " + destination);
		Logger.Logger.write("\nTAKING PERSON TO FLOOR " + destination, elevatorTestLogFileName );
		if (onFloor > destination) this.direction = 2;
		else this.direction = 1;
		try {
			Thread.sleep(1000);
			// TODO : Elevator timing
			// THIS IS WHERE WE WOULD IMPLEMENT TIMING FROM FLOOR TO FLOOR
			int time = Math.abs(onFloor - destination);
			int floor = onFloor;
			
			if(direction == 1) System.out.println("Going up to requested floor " + destination);
			if(direction == 2) System.out.println("Going down to requested floor " + destination);
			
			
			for(int i = 0; i < time; i ++) {
				if(direction == 1) System.out.println("Current Floor " + floor ++);
				if(direction == 2) System.out.println("Current FLoor " + floor --);
				Thread.sleep(1000);
			}
			
			System.out.println("We have arrived at floor " + destination); 
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// set our new current floor to the floor we want to arrive at and our direction
		this.onFloor = destination;
		
		// send notification to scheduler saying that we have arrived and that we have no pending destination
		this.notif.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.ELEV_ARRIVAL, this.carNum, this.direction, this.onFloor));
		occupied = false;
		
		// this does nothing yet, eventually when we have the GUI it should show the doors opening
		openDoors();
	}
	
	void openDoors() {
		// open the doors
	}
	

	
	
}
