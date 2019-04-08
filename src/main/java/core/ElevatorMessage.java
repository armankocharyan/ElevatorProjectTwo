package core;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * ElevatorMessage is the default message class for all UDP messages to and from our
 * three subsystems (Floors, Elevators, and the Scheduler). Supports up to 96
 * int parameters as extra information and conversion between byte strings and 
 * class instances.
 */
public class ElevatorMessage {
	
	
	public enum MessageType {
		ELEV_REQUEST('0'), PASSENGER_ENTER('1'), ELEV_PICKUP('2'), ELEV_ARRIVAL('3'),
		STOP('4'), REQ('5'), DOORS('6'), ARRIVAL('7'), CONT('8'), EMPTY('9');

		byte code;

		MessageType(char code) {
			this.code = (byte) code;
		}

		public byte getCode() {
			return code;
		}
		

		public static MessageType typeFromCode(byte code) {
			MessageType ret = null;
			switch (code) {
			case '0':
				ret = ELEV_REQUEST;
				break;
			case '1':
				ret = PASSENGER_ENTER;
				break;
			case '2':
				ret = ELEV_PICKUP;
				break;
			case '3':
				ret = ELEV_ARRIVAL;
				break;
			case '4':
				ret = STOP;
				break;
			case '5':
				ret = REQ;
				break;
			case '6':
				ret = DOORS;
				break;
			case '7':
				ret = ARRIVAL;
				break;
			case '8':
				ret = CONT;
				break;
			case '9':
				ret = EMPTY;
				break;
			}
			return ret;
		}
	}
	
	// -- STATIC VARIABLES -- //
	public static final int MAX_ARGS = 96; 
	public static final int SIZE = 100; 
	static byte SEP = '*';

	// -- INSTANCE VARIABLES -- //
	MessageType type = null;
	int id = 0;
	ArrayList<Integer> data = new ArrayList<Integer>();
	byte[] msg = new byte[SIZE];
	
	public InetAddress addr = null;
	public int PORT;

	/**
	 * Creates an ElevatorMessage from parameters.
	 * 
	 * @param type       one of ELEV_REQUEST, PASSENGER_ENTER, ELEV_PICKUP, ELEV_ARRIVAL.
	 * @param id         the id (floor number or car number) of the object sending
	 *                   the message.
	 * @param parameters an arbitrarily long list of message data (direction, 
	 * 					 floor requested, etc.)
	 */
	public ElevatorMessage(MessageType type, int id, int... args) {
		if (args.length > MAX_ARGS) {
			throw new IllegalArgumentException();
		}

		this.type = type;
		this.id = id;
		this.data.clear();
		for (int x : args) {
			data.add(new Integer(x));
		}

		createByteString();
	}

	/**
	 * Constructs a message from a byte array.
	 * 
	 * @param msg this is the byte array we have received and need to decode.
	 */
	public ElevatorMessage(byte[] msg) {
		this.msg = msg;

		this.type = MessageType.typeFromCode(msg[0]);
		this.id = (int) msg[2];

		data.clear();
		int i = 4;
		while (msg[i] != SEP) {
			data.add((int) msg[i]);
			i++;
		}
	}
	
	/**
	 * Constructs a byte array representing this instance with a max size of 100.
	 */
	void createByteString() {
		int i = 0;

		msg[i++] = this.type.getCode();
		msg[i++] = SEP;
		msg[i++] = (byte) this.id;
		msg[i++] = SEP;

		for (int arg : data) {
			msg[i++] = (byte) arg;
		}

		while (i < SIZE) {
			msg[i++] = SEP;
		}
	}
	
	/**
	 * Returns the type of message
	 * @return MessageType 
	 */
	public MessageType getType() {
		return this.type;
	}

	/**
	 * Returns either the direction requested (if type == ELEV_REQUEST) or 
	 * the current elevator car's direction.
	 * Supported message types include ELEV_REQUEST, ELEV_PICKUP, or ELEV_ARRIVAL.
	 * 
	 * 
	 * @return int -1 -> wrong message type, 1 -> UP, 2 -> DOWN
	 */
	public int getDirection() {
		if (type != MessageType.ELEV_REQUEST && type != MessageType.ELEV_PICKUP && type != MessageType.ELEV_ARRIVAL ) {
			return -1;
		}

		return data.get(0);
	}

	/**
	 * Returns the number of the floor or elevator car sending the message
	 * 
	 * @return int id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns the floor the elevator has stopped on or -1 if the messagetype 
	 * does not support floors.
	 * Supported message types include ELEV_PICKUP and ELEV_ARRIVAL.
	 * 
	 * 
	 * @return int elevator's current floor.
	 */
	public int getFloor() {
		if (type != MessageType.ELEV_PICKUP && type != MessageType.ELEV_ARRIVAL ) {
			return -1;
		}
		return this.data.get(1);
	}

	/**
	 * Returns the floor requested when the elevator has picked up 
	 * a passenger or -1 if the message type does not support requested floors.
	 * Supported message types include PASSENGER_ENTER.
	 * 
	 * @return int floor number.
	 */
	public int getRequestedFloor() {
		if (type != MessageType.PASSENGER_ENTER) {
			return -1;
		}
		return data.get(1);
	}
	
	/**
	 * Returns the car the passenger has entered or -1 if the message does not 
	 * support car numbers.
	 * Supported message types include PASSENGER_ENTER.
	 * 
	 * @return int carNum
	 */
	public int getCarNum() {
		if (type != MessageType.PASSENGER_ENTER) {
			return -1;
		}
		return data.get(0);
	}

	/**
	 * If a passenger has requested multiple floors, returns all of them.
	 * Supported message types include PASSENGER_ENTER.
	 * 
	 * @return int[] requestedFloors
	 */
	public int[] getRequestedFloors() {
		if (type != MessageType.PASSENGER_ENTER) {
			return new int[] { -1 };
		}

		int[] ret = new int[data.size()];

		int i = 1;
		for (Integer x : data) {
			ret[i] = x;
			i++;
		}
		return ret;
	}

	/**
	 * Returns the array of bytes representing this instance with a max size of 100 
	 * 
	 * @return byte[] msg
	 */
	public byte[] getBytes() {
		return msg;
	}
	
	public ArrayList<Integer> getData() {
		return data;
	}

	
	/**
	 * Neatly outputs the class variables in a string
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nMessage Type: " + this.type);
		sb.append("\nSent from: " + this.id);
		sb.append("\nWith args: " + this.data.toString());
		return sb.toString();
	}

	
}
