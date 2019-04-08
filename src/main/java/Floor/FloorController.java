package Floor;

import java.util.ArrayList;

import File.ReadFile;
import core.Constants;
import core.ElevatorMessage;
import core.EventListener;
import core.RequestData;

public class FloorController {

	Floor[] floors;

	EventListener listener;

	public FloorController() {
		floors = new Floor[Constants.NUM_FLOORS];
		for (int i = 0; i < Constants.NUM_FLOORS; i++) {
			floors[i] = new Floor(i);
		}
		listener = new EventListener(Constants.FLOOR_PORT, "FLOOR ELEVATOR LISTENER");

	}

	public void startListen() {
		// listens for notifications from the scheduler that an elevator has arrived

		System.out.println("FLOOR CONTROLLER: Starting elevator listener...");

		for (;;) {
			ElevatorMessage msg = listener.waitForNotification();

			switch (msg.getType()) {
			default:
				break;
			}

		}
	}

	public void requestFloor(int from, int to) {

		if (to > from)
			floors[from].reqUp(to);
		else
			floors[from].reqDown(to);
	}

	public void readInput() {
		ArrayList<RequestData> inputData = ReadFile.getData("inputFile.txt");
		// everything below this is just demo runs
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(inputData.get(0).goingUp()) {
			floors[inputData.get(0).getFloorNumber()].reqUp(inputData.get(0).getfloorToGo());
		}
		else {
			floors[inputData.get(0).getFloorNumber()].reqDown(inputData.get(0).getfloorToGo());
		}
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(inputData.get(1).goingUp()) {
			floors[inputData.get(1).getFloorNumber()].reqUp(inputData.get(1).getfloorToGo());
		}
		else {
			floors[inputData.get(1).getFloorNumber()].reqDown(inputData.get(1).getfloorToGo());
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(inputData.get(2).goingUp()) {
			floors[inputData.get(2).getFloorNumber()].reqUp(inputData.get(2).getfloorToGo());
		}
		else {
			floors[inputData.get(2).getFloorNumber()].reqDown(inputData.get(2).getfloorToGo());
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(inputData.get(3).goingUp()) {
			floors[inputData.get(3).getFloorNumber()].reqUp(inputData.get(3).getfloorToGo());
		}
		else {
			floors[inputData.get(3).getFloorNumber()].reqDown(inputData.get(3).getfloorToGo());
		}
	}

	public void start() {
		FloorController s = this;

		// start the thread that listens for notifications from the scheduler
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startListen();
			}
		});

		t1.start();

	}
	
	public static void main(String[] args) {

		FloorController c = new FloorController();
		c.start();
		c.readInput();
	}

}
