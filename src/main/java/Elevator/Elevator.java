package Elevator;

import Logger.Logger;
import core.Button;
import core.EventNotifier;
import core.ElevatorMessage;
import core.Lamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	int direction = 1;
	int carNum = -1;
	int[] destinationFloors = null;
	boolean occupied = false;
	EventNotifier notif;
	Calendar cal;
	SimpleDateFormat time;

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
		this.time = new SimpleDateFormat("HH:mm:ss.SSS");
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
		this.occupied = true;

		if (dir == 2) {
			this.direction = 2;
			cal = Calendar.getInstance();
			System.out.println("Elevator" + Integer.toString(this.carNum) + " Going to PICK UP to floor " +
			floor + " from floor " + Integer.toString(onFloor) + " at " + time.format(cal.getTime()));
			Logger.write("Elevator" + Integer.toString(this.carNum) + " Going to PICK UP to floor " +
			floor + " from floor " + Integer.toString(onFloor) + " at " + time.format(cal.getTime()), "Logs/elevator.log");
		}
		else {
			this.direction = 1;
			cal = Calendar.getInstance();
			System.out.println("Elevator" + Integer.toString(this.carNum) + " Going to PICK UP to floor " +
			floor + " from floor " + Integer.toString(onFloor) + " at "  + time.format(cal.getTime()));
			Logger.write("Elevator" + Integer.toString(this.carNum) + " Going to PICK UP to floor " +
			floor + " from floor " + Integer.toString(onFloor) + " at "  + time.format(cal.getTime()), "Logs/elevator.log");
		}

		int time = Math.abs(onFloor - floor) * 3000;


		new java.util.Timer().schedule(
	        new java.util.TimerTask() {
	            @Override
	            public void run() {
	                arrivePickUp(floor, dir);
	            }
	        },
	        time
		);
	}

	/**
	 * This is called when a passenger has entered the elevator and chosen
	 * a floor to ride to. This will move our elevator to the floor requested
	 * over a period of time and notify the scheduler when it has arrived.
	 *
	 * @param destination the floor we need to ride to.
	 */
	public void rideToFloor(int destination) {

		//setting the destination and printing when the elevator departs
		if (onFloor > destination) {
			this.direction = 2;
			cal = Calendar.getInstance();
			System.out.println("Elevator" + Integer.toString(this.carNum) + " Going to requested floor " +
			destination + " from floor" + Integer.toString(onFloor) + " at " + time.format(cal.getTime()));
			Logger.write("Elevator" + Integer.toString(this.carNum) + " Going to requested floor " +
					destination + " from floor" + Integer.toString(onFloor) + " at " + time.format(cal.getTime()), "Logs/elevator.log");
		}
		else {
			this.direction = 1;
			cal = Calendar.getInstance();
			System.out.println("Elevator" + Integer.toString(this.carNum) + " Going to requested floor " +
			destination + " from floor " + Integer.toString(onFloor) + " at "  + time.format(cal.getTime()));
			Logger.write("Elevator" + Integer.toString(this.carNum) + " Going to requested floor " +
			destination + " from floor " + Integer.toString(onFloor) + " at "  + time.format(cal.getTime()), "Logs/elevator.log");
		}

		int time = Math.abs(onFloor - destination) * 3000;
		new java.util.Timer().schedule(
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		                arriveRequest(destination);
		            }
		        },
		        time

		);


	}


	public void arriveRequest(int destination) {

		cal = Calendar.getInstance();
		System.out.println("Elevator" + Integer.toString(this.carNum) + " Has arrived to floor" + destination + " at " + time.format(cal.getTime()));
		Logger.write("Elevator" + Integer.toString(this.carNum) + " Has arrived to floor" + destination + " at " + time.format(cal.getTime()), "Logs/elevator.log");
		// set our new current floor to the floor we want to arrive at and our direction
		this.onFloor = destination;
		// send notification to scheduler saying that we have arrived and that we have no pending destination
		this.notif.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.ELEV_ARRIVAL, this.carNum, this.direction, this.onFloor));
		occupied = false;
		// this does nothing yet, eventually when we have the GUI it should show the doors opening
		openDoors();

	}


	public void arrivePickUp(int floor, int dir) {

		cal = Calendar.getInstance();
		System.out.println("Elevator" + Integer.toString(this.carNum) + " Has arrived to floor " + floor + " at " + time.format(cal.getTime()));
		Logger.write("Elevator" + Integer.toString(this.carNum) + " Has arrived to floor " + floor + " at " + time.format(cal.getTime()), "Logs/elevator.log");
		// set our new current floor and direction
		this.onFloor = floor;
		this.direction = dir;
		// send notification to scheduler saying that we have arrived and which floor we are going to next
		this.notif.sendMessage(new ElevatorMessage(ElevatorMessage.MessageType.ELEV_PICKUP, this.carNum, this.direction, this.onFloor));


		// this does nothing yet, eventually when we have the GUI it should show the doors opening
		openDoors();

	}



	void openDoors() {
		// open the doors
	}




}
