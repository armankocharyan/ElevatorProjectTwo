package Floor;

import java.util.ArrayList;

import File.ReadFile;
import Logger.Logger;
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

	public void requestFloor(int from, int to, int fault) {

		if (to > from)
			floors[from].reqUp(to, fault);
		else
			floors[from].reqDown(to, fault);
	}

	public void readInput() {
		ArrayList<RequestData> inputData = ReadFile.getData("inputFile.txt");
		// everything below this is just demo runs
		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(RequestData line : inputData) {
			if (line.goingUp()) {
				floors[line.getFloorNumber()].reqUp(line.getfloorToGo(), line.getFault());
			}
			else {
				floors[line.getFloorNumber()].reqDown(line.getfloorToGo(), line.getFault());
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void start() {
		Logger.clearAllLogFiles();
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
