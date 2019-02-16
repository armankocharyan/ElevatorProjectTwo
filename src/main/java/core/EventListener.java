package core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * EventListener opens a socket on a given port and listens until it 
 * receives a message. When put in an infinite loop, it acts as a simple
 * server.
 *
 */
public class EventListener {
	
	// -- INSTANCE VARIABLES -- //
	int PORT;
	String name = "";
	protected DatagramPacket recvPacket;
	protected DatagramSocket recvSocket;
	
	// -- CONSTRUCTOR -- //
	public EventListener(int PORT, String name) {
		this.PORT = PORT;
		try {
			recvSocket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Performs a blocking wait call until it receives a message on
	 * the instance's port. Should be called in a separate thread
	 * so as not to halt the program.
	 * 
	 * @return ElevatorMessage the message we received, packaged into
	 * 						   a convenient class.
	 */
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
