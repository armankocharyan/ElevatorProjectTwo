package core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class EventListener {

	protected DatagramPacket recvPacket;
	protected DatagramSocket recvSocket;
	
	int PORT;
	
	public static String name = "EVENT LISTENER";
	
	public EventListener(int PORT, String name) {
		this.PORT = PORT;
		try {
			recvSocket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ElevatorMessage waitForNotification() {
		ElevatorMessage msg;
		byte[] data = new byte[ElevatorMessage.SIZE];
		
		recvPacket = new DatagramPacket(data, data.length);
		System.out.println(name+ ": Waiting for a notification...");
		try {
			recvSocket.receive(recvPacket);
			System.out.println(name + ": RECEIVED NOTIFICATION");
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}
		msg = new ElevatorMessage(recvPacket.getData());
		return msg;
	}
}
