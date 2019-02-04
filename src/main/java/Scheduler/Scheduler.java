package Scheduler;

import java.util.LinkedList;
import java.util.Queue;

import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;

public class Scheduler {

	EventListener floorListener;
	EventNotifier floorReqNotifier;

	EventListener elevListener;
	EventNotifier floorNotifier;

	EventListener elevEnteredListener;
	EventNotifier elevEnteredNotifier;

	Queue<ElevatorMessage> queue = new LinkedList<ElevatorMessage>();

	public Scheduler() {
		floorListener = new EventListener(23, "FLOOR REQUEST LISTENER");
		floorReqNotifier = new EventNotifier(28, "ELEVATOR REQUEST NOTIFIER");

		elevListener = new EventListener(24, "ELEVATOR LISTENER");
		floorNotifier = new EventNotifier(42424, "SCHEDULER ELEVATOR NOTIFIER");

		elevEnteredListener = new EventListener(25, "ELEVATOR OCCUPANCY LISTENER");
		elevEnteredNotifier = new EventNotifier(30, "ELEVATOR OCCUPANCY NOTIFIER");

	}

	public void startFloorListen() throws InterruptedException {
		System.out.println("SCHEDULER: Starting floor listener...");

		for (;;) {

			synchronized (this) {
				while (queue.size() >= 1) {
					wait();
				}
				ElevatorMessage msg = floorListener.waitForNotification();
				System.out.println("\nSCHEDULER: RECEIVED FLOOR REQUEST " + msg);
				queue.add(msg);
				notifyAll();
			}

		}
	}

	public void dequeue() throws InterruptedException {
		for (;;) {
			synchronized (this) {
				while (queue.isEmpty()) {
					wait();
				}
				ElevatorMessage msg = queue.remove();
				this.floorReqNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
				notifyAll();
			}
		}
	}

	public void startElevatorListen() {
		System.out.println("SCHEDULER: Starting elevator listener...");

		for (;;) {
			ElevatorMessage msg = elevListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED ELEVATOR NOTIFICATION " + msg);
			this.floorNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			if (msg.getMovingTo() == -1) {
			}

		}
	}

	public void startElevEnterListen() {
		System.out.println("SCHEDULER: Starting elevator occupancy listener...");

		for (;;) {
			ElevatorMessage msg = elevEnteredListener.waitForNotification();
			System.out.println("\nSCHEDULER: RECEIVED OCCUPANCT NOTIFICATION " + msg);
			this.elevEnteredNotifier.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());

		}
	}

	public void start() {
		Scheduler s = this;
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s.startFloorListen();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Thread t4 = new Thread(new Runnable() {
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
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevatorListen();
			}
		});

		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevEnterListen();
			}
		});

		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}

	public static void main(String[] args) {
		Scheduler s = new Scheduler();
		s.start();
	}

}
