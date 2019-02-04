package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Elevator.ElevatorMessage;
import Elevator.ElevatorNotifier;
import Floor.NotificationListener;

public class ElevatorDispatcher extends ElevatorNotifier {

	
	protected void sendMessage(ElevatorMessage msg) {
		try {
			DatagramSocket sendSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), ElevatorMessage.SIZE, localhost, NotificationListener.PORT);
			System.out.println("ELEVATOR DISPATCHER");
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
