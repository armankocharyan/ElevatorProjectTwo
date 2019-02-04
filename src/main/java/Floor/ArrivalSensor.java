package Floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ArrivalSensor{
	int port;
	
	DatagramPacket recvPacket;
	DatagramSocket recvSocket;

	public ArrivalSensor() {
		try {
			recvSocket = new DatagramSocket();
			this.port = recvSocket.getPort();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public int getPort() {
		return port;
	}
	
	public FloorMessage waitForElevator() {
		FloorMessage msg;
		byte[] data = new byte[FloorMessage.SIZE];
		
		recvPacket = new DatagramPacket(data, data.length);
		System.out.println("ARRIVAL SENSOR: waiting for elevator...");
		try {
			recvSocket.receive(recvPacket);
			System.out.println("ARRIVAL SENSOR: ELEVATOR ARRIVED");
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
