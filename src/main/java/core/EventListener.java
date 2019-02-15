package core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class EventListener {
	/* This class will block & wait for a notification on port PORT */

	protected DatagramPacket recvPacket;
	protected DatagramSocket recvSocket;
	
	int PORT;
	
	public static String name = "EVENT LISTENER";
	
	public EventListener(int PORT, String name) {
		this.PORT = PORT;
		try {
			recvSocket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public ElevatorMessage waitForNotification() {
		// waits on the receiving socket for a message
		
		ElevatorMessage msg;
		byte[] data = new byte[ElevatorMessage.SIZE];
		
		recvPacket = new DatagramPacket(data, data.length);
		System.out.println(name+ ": Waiting for a notification...");
		
		try {
			// blocking wait call
			recvSocket.receive(recvPacket);
			System.out.println(name + ": RECEIVED NOTIFICATION");
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}
		
		// turn this message into an instance of ElevatorMessage & return it to the class
		msg = new ElevatorMessage(recvPacket.getData());
		return msg;
	}
}
