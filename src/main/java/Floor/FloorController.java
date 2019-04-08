package Floor;

import core.Constants;
import core.ElevatorMessage;
import core.EventListener;

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
	}

}
