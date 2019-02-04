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
	
	public void arriveAtFloor(int num) {
		if ((num - currentFloor) > 0) direction = 1;
		else direction = 2;
		
		currentFloor = num;
	}
	
	public void moveToFloor(int num) {
		if ((num - currentFloor) > 0) direction = 1;
		else direction = 2;
		
	}
	
	
}
