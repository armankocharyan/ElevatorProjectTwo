package Floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Elevator.ElevatorMessage;

public class FloorController {
	
	public static final int PORT = 62442;
	
	int numFloors;
	Floor[] floors;
	
	NotificationListener listener;
	
	public FloorController(int numFloors) {
		this.numFloors = numFloors;
		
		floors = new Floor[numFloors];
		
		for(int i=0; i<numFloors; i++) {
			floors[i] = new Floor(i, (i == numFloors-1), (i==0));
		}
		
		for(Floor f : floors) {
			f.start();
		}
		
		
		listener = new NotificationListener();
		/*
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FloorMessage msg = new FloorMessage(1, 0, 3);
		byte[] data = msg.getBytes();
		DatagramPacket pkg;
		try {
			pkg = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), floors[2].getPort());
			try {
				DatagramSocket send = new DatagramSocket();
				send.send(pkg);
				send.close();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
	}
	public void startElevatorListen() {
		System.out.println("FLOOR CONTROLLER: Starting elevator listener...");
		
		for(;;) {
			ElevatorMessage msg = listener.waitForNotification();
			System.out.println("\nFLOOR CONTROLLER: RECEIVED ELEVATOR NOTIFICATION " + msg);
			byte[] data = msg.getBytes();
			DatagramPacket pkg;
			try {
				pkg = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), floors[msg.getCurrentFloor()].getPort());
				try {
					DatagramSocket send = new DatagramSocket();
					send.send(pkg);
					send.close();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void start() {
		FloorController s = this;
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				s.startElevatorListen();
			}
		});
		
		
		t2.start();
	}
	
	public static void main(String[] args) {
		FloorController c = new FloorController(3);
		c.start();
	}
	
}
