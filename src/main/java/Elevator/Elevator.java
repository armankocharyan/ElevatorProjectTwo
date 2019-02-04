package Elevator;


import core.Button;
import core.EventNotifier;
import core.Lamp;

public class Elevator {
	
	int numFloors;
	int currentFloor = 0;
	int movingTo = 0;
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
		this.notif = new EventNotifier(24, "ELEVATOR NOTIFIER");
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public int getDirection() {
		return direction;
	}
	
	void announceFloor(int movingTo){
		this.notif.sendNotif(direction, currentFloor, movingTo);
	}
	
	public void rideToFloor(int floor, int dir, int movingTo) {
		this.currentFloor = movingTo;
		this.direction = dir;
		this.movingTo = -1;
		announceFloor(-1);
	}
	
	public void pickUpPerson(int floor, int dir, int movingTo) {
		this.currentFloor = floor;
		this.direction = dir;
		this.movingTo = movingTo;
		announceFloor(movingTo);
	}
	
	
}
