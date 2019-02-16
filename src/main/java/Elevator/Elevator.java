package Elevator;


import core.Button;
import core.EventNotifier;
import core.ElevatorMessage;
import core.Lamp;
import Scheduler.Scheduler;

public class Elevator {
	/* Elevator class -- ONE ELEVATOR CAR */
	
	int numFloors;
	int onFloor = 0;
	int[] destinationFloors = null;
	int direction = 1;
	
	int carNum = -1;
	
	Door door;
	Motor motor;
	Lamp[] floorLamps;
	Button[] floorBtns;
	
	EventNotifier notif;
	
	boolean occupied = false;
	
	public Elevator(int carNum, int numFloors) {
		this.numFloors = numFloors;
		this.onFloor = 0;
		this.direction = 1;
		this.carNum = carNum;
		
		this.destinationFloors = new int[numFloors];
		
		// notifies the scheduler listening on port 24 when the elevator has arrived on the floor
		this.notif = new EventNotifier(Scheduler.PORT, "ELEVATOR");
	}
	
	public int getCurrentFloor() {
		return onFloor;
	}
	
	public int getDirection() {
		return direction; // 1->UP, 2->DOWN
	}
	
	public void pickUpPerson(int floor, int dir) {
		// floor -> the floor the person is requesting from
		// dir -> the direction they need to go, 1-> UP, 2-> DOWN
		// movingTo -> the floor the person wants to go to
		// Called when a person requests an elevator
		// this function sends the elevator to the floor the person is on to pick them up
		System.out.println("\nPICKING UP PERSON ON FLOOR " + floor);
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
	
	public void rideToFloor(int destination) {
		// Called AFTER we have picked up the person at floor requesting the elevator
		// this function moves the elevator to the floor they want to go to
		
		System.out.println("\nTAKING PERSON TO FLOOR " + destination);
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
	
	public boolean isOccupied() {
		return occupied;
	}
	
	
}
