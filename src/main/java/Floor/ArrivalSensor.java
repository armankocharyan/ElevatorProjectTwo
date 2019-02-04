package Floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Elevator.ElevatorMessage;

public class ArrivalSensor{
	public static int NEXT_PORT = 62443;
	int port;
	
	DatagramPacket recvPacket;
	DatagramSocket recvSocket;

	public ArrivalSensor() {
		this.port = NEXT_PORT++;
		try {
			recvSocket = new DatagramSocket(this.port);
			System.out.println("ARRIVAL SENSOR PORT: " + port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public int getPort() {
		return port;
	}
	
	public ElevatorMessage waitForElevator() {
		ElevatorMessage msg;
		byte[] data = new byte[ElevatorMessage.SIZE];
		
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
		msg = new ElevatorMessage(recvPacket.getData());
		return msg;
	}


}
