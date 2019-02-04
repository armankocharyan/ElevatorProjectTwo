package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Scheduler.ElevatorListener;

public class ElevatorNotifier {
	protected DatagramPacket sendPacket;
	protected DatagramSocket sendSocket;
	protected InetAddress localhost;
	
	public ElevatorNotifier() {
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
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), ElevatorMessage.SIZE, localhost, ElevatorListener.PORT);
			System.out.println("ELEVATOR NOTIFIER");
			System.out.println("SENDING ELEVATOR ARRIVED NOTIFICATION" + msg);
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
