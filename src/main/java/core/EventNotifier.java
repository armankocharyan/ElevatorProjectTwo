package core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EventNotifier {
	protected DatagramPacket sendPacket;
	protected DatagramSocket sendSocket;
	protected InetAddress localhost;
	int PORT;
	String name = "EVENT NOTIFIER";
	
	public EventNotifier(int PORT, String name) {
		this.PORT = PORT;
		this.name = name;
		try {
			this.localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void sendNotif(int dir, int currFloor, int movingTo) {
		ElevatorMessage msg = new ElevatorMessage(dir, currFloor, movingTo);
		sendMessage(msg);
	}
	
	
	protected void sendMessage(ElevatorMessage msg) {
		try {
			DatagramSocket sendSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), ElevatorMessage.SIZE, localhost, this.PORT);
			System.out.print(name);
			System.out.println(": SENDING EVENT INFORMATION" + msg);
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sendSocket.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
