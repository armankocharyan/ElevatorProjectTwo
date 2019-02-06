package Floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;

public class FloorController {
	
	public static final int PORT = 62442;
	
	int numFloors;
	Floor[] floors;
	
	EventListener listener;
	
	public FloorController(int numFloors) {
		this.numFloors = numFloors;
		
		floors = new Floor[numFloors];
		
		for(int i=0; i<numFloors; i++) {
			floors[i] = new Floor(i, (i == numFloors-1), (i==0));
		}
		
		for(Floor f : floors) {
			f.start();
		}
		
		
		listener = new EventListener(42424, "FLOOR ELEVATOR LISTENER");
		
	}
	public void startElevatorListen() {
		System.out.println("FLOOR CONTROLLER: Starting elevator listener...");
		
		for(;;) {
			ElevatorMessage msg = listener.waitForNotification();
			System.out.println("\nFLOOR CONTROLLER: RECEIVED ELEVATOR NOTIFICATION " + msg);
			
			EventNotifier notif = new EventNotifier(floors[msg.getCurrentFloor()].getPort(), "ARRIVAL NOTIFIER");
			notif.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			
			if(msg.getMovingTo() != -1) {
				notif = new EventNotifier(25, "OCCUPANCY NOTIFIER");
				notif.sendNotif(msg.getDirection(), msg.getCurrentFloor(), msg.getMovingTo());
			}
			
			
		}
	}
	
	public void start() throws InterruptedException {
		FloorController s = this;
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevatorListen();
			}
		});
		
		
		t2.start();
		
		Thread.sleep(5000);
		
		floors[0].reqUp(4);
		Thread.sleep(30);
		floors[2].reqUp(3);
		
	}
	
	public static void main(String[] args) {
		FloorController c = new FloorController(5);
		try {
			c.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
