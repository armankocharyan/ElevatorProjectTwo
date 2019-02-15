package Elevator;


import core.Button;
import core.EventNotifier;
import core.Lamp;

public class Elevator {
	/* Elevator class -- ONE ELEVATOR CAR */
	
	int numFloors;
	int currentFloor = 0;
	int movingTo = 0; // our current destination floor / where we want to go. -1 if we have no destination
	int elevatorNum;
	int direction = 1; 
	
	Door door;
	Motor motor;
	Lamp[] floorLamps;
	Button[] floorBtns;
	
	EventNotifier notif;
	
	public Elevator(int elevatorNum, int numFloors) {
		this.numFloors = numFloors;
		this.currentFloor = 0;
		this.direction = 1;
		this.elevatorNum = elevatorNum;
		
		// notifies the scheduler listening on port 24 when the elevator has arrived on the floor
		this.notif = new EventNotifier(24, "ELEVATOR NOTIFIER");
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public int getDirection() {
		return direction; // 1->UP, 2->DOWN
	}
	
	void announceFloor(int movingTo){
		// sends a datagram to the scheduler saying we have arrived on a floor
		this.notif.sendNotif(direction, currentFloor, movingTo);
	}
	
	public void pickUpPerson(int floor, int dir, int movingTo) {
		// floor -> the floor the person is requesting from
		// dir -> the direction they need to go, 1-> UP, 2-> DOWN
		// movingTo -> the floor the person wants to go to
		// Called when a person requests an elevator
		// this function sends the elevator to the floor the person is on to pick them up
		
		try {
			// TODO : Elevator timing
			// THIS IS WHERE WE WOULD IMPLEMENT TIMING FROM FLOOR TO FLOOR
			
			int time = Math.abs(currentFloor - movingTo);
			if(dir == 1) System.out.println("Going up to " + movingTo + " floor to pick up someone");
			if(dir == 2) System.out.println("Going down to " + movingTo + " floor to pick up someone");
			
			
			for(int i = 0; i < time; i ++) {
				if(dir == 1) System.out.println("Current Floor " + floor ++);
				if(dir == 2) System.out.println("Current FLoor " + floor --);
				Thread.sleep(1000);
			}
			
			System.out.println("We have arrived to floor " + movingTo);
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// set our new current floor and direction
		this.currentFloor = floor;
		this.direction = dir;
		// set our new destination floor
		this.movingTo = movingTo;
		// send notification to scheduler saying that we have arrived and which floor we are going to next
		announceFloor(movingTo);
		
		
		// this does nothing yet, eventually when we have the GUI it should show the doors opening
		openDoors();
	}
	
	public void rideToFloor(int dir, int movingTo) {
		// Called AFTER we have picked up the person at floor requesting the elevator
		// this function moves the elevator to the floor they want to go to
		
		try {
			Thread.sleep(1000);
			// TODO : Elevator timing
			// THIS IS WHERE WE WOULD IMPLEMENT TIMING FROM FLOOR TO FLOOR
			int time = Math.abs(currentFloor - movingTo);
			int floor = currentFloor;
			
			if(dir == 1) System.out.println("Going up to requested floor " + movingTo);
			if(dir == 2) System.out.println("Going down to requested floor " + movingTo);
			
			
			for(int i = 0; i < time; i ++) {
				if(dir == 1) System.out.println("Current Floor " + floor ++);
				if(dir == 2) System.out.println("Current FLoor " + floor --);
				Thread.sleep(1000);
			}
			
			System.out.println("We have arrived to floor " + movingTo); 
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// set our new current floor to the floor we want to arrive at and our direction
		this.currentFloor = movingTo;
		this.direction = dir;
		
		// set our movingTo to -1 to signal that we have no pending movements
		this.movingTo = -1;
		
		// send notification to scheduler saying that we have arrived and that we have no pending destination
		announceFloor(-1);
		
		// this does nothing yet, eventually when we have the GUI it should show the doors opening
		openDoors();
	}
	
	void openDoors() {
		// open the doors
	}
	
	
}
