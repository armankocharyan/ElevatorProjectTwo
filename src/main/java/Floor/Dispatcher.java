package Floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Scheduler.FloorRequestListener;

public class Dispatcher {
	
	DatagramPacket sendPacket;
	DatagramSocket sendSocket;
	InetAddress localhost;
	
	public Dispatcher() {
		try {
			this.localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public boolean sendUpRequest(int currFloor, int reqFloor) {
		boolean send = false;
		FloorMessage msg = new FloorMessage(1, currFloor, reqFloor);
		sendMessage(msg);
		return send;
	}
	
	public boolean sendDownRequest(int currFloor, int reqFloor) {
		boolean send = false;
		FloorMessage msg = new FloorMessage(2, currFloor, reqFloor);
		sendMessage(msg);
		return send;
	}
	
	void sendMessage(FloorMessage msg) {
		try {
			DatagramSocket sendSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.SIZE, localhost, FloorRequestListener.PORT);
			System.out.println("FLOOR DISPATCHER");
			System.out.println("SENDING FLOOR BUTTON REQUEST" + msg);
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
