package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Floor.FloorMessage;

public class FloorRequestListener {

	public static final int PORT = 23;
	
	DatagramPacket recvPacket;
	DatagramSocket recvSocket;
	
	public FloorRequestListener() {
		try {
			recvSocket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FloorMessage waitForFloorRequest() {
		FloorMessage msg;
		byte[] data = new byte[FloorMessage.SIZE];
		
		recvPacket = new DatagramPacket(data, data.length);
		System.out.println("FLOOR REQUEST LISTENER: Waiting for a request...");
		try {
			recvSocket.receive(recvPacket);
			System.out.println("FLOOR REQUEST LISTENER: RECEIVED REQUEST");
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}
		msg = new FloorMessage(recvPacket.getData());
		return msg;
	}
	
	
}
