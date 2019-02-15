package core;

import java.util.ArrayList;

public class ElevatorMessage {

	public static final int MAX_ARGS = 96; // max allowed arguments to the message
	public static final int SIZE = 100; // message bytestring length

	static byte SEP = '*';

	public enum MessageType {
		ELEV_REQUEST('0'), PASSENGER_ENTER('1'), ELEV_PICKUP('2'), ELEV_ARRIVAL('3');

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
			}
			return ret;
		}
	}

	MessageType type = null;
	int id = 0;
	ArrayList<Integer> data = new ArrayList<Integer>();
	byte[] msg = new byte[SIZE];

	/**
	 * Creates an ElevatorMessage from parameters
	 * 
	 * @param type       the message type
	 * @param id         the id (floor number or car number) of the object sending
	 *                   the message
	 * @param parameters an arbitrarily long list of message data
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
	 * Constructs a message from a byte array
	 * 
	 * @param msg the byte array we are decoding
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
	
	public MessageType getType() {
		return this.type;
	}

	public int getDirection() {
		if (type != MessageType.ELEV_REQUEST && type != MessageType.ELEV_PICKUP && type != MessageType.ELEV_ARRIVAL ) {
			return -1;
		}

		return data.get(0);
	}

	public int getId() {
		return this.id;
	}
	
	public int getFloor() {
		if (type != MessageType.ELEV_PICKUP && type != MessageType.ELEV_ARRIVAL ) {
			return -1;
		}
		return this.data.get(1);
	}

	public int getRequestedFloor() {
		if (type != MessageType.PASSENGER_ENTER) {
			return -1;
		}
		return data.get(0);
	}
	
	public int getCarNum() {
		if (type != MessageType.PASSENGER_ENTER) {
			return -1;
		}
		return data.get(1);
	}

	public int[] getRequestedFloors() {
		if (type != MessageType.PASSENGER_ENTER) {
			return new int[] { -1 };
		}

		int[] ret = new int[data.size()];

		int i = 0;
		for (Integer x : data) {
			ret[i] = x;
			i++;
		}
		return ret;
	}

	public byte[] getBytes() {
		return msg;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nMessage Type: " + this.type);
		sb.append("\nSent from: " + this.id);
		sb.append("\nWith args: " + this.data.toString());
		return sb.toString();
	}

	/*
	 * /* A message datatype to serialize/deserialize our byte string datagrams
	 * public static final int SIZE = 9; public static final byte SEP = '*';
	 * 
	 * int direction = 0; // 1 -> UP, 2 -> DOWN int currFloor; int movingTo; int
	 * car; // the elevator car sending/receiving the message
	 * 
	 * byte[] msg;
	 * 
	 * public ElevatorMessage(int direction, int currFloor, int movingTo) { // data
	 * constructor this.direction = direction; this.movingTo = movingTo;
	 * this.currFloor = currFloor;
	 * 
	 * // TODO : add car to constructor this.car = 1;
	 * 
	 * msg = new byte[9]; msg[0] = SEP; msg[1] = (byte)this.direction; msg[2] = SEP;
	 * msg[3] = (byte)this.currFloor; msg[4] = SEP; msg[5] = (byte)this.movingTo;
	 * msg[6] = SEP; msg[7] = (byte)this.car; msg[8] = SEP; }
	 * 
	 * 
	 * public ElevatorMessage(byte[] msg) { // byte string constructor this.msg =
	 * msg;
	 * 
	 * 
	 * this.direction = (int)msg[1]; this.currFloor = (int)msg[3]; this.movingTo =
	 * (int)msg[5]; this.car = 1; }
	 * 
	 * public boolean isUp() { return direction == 1; }
	 * 
	 * public boolean isDown() { return direction == 2; }
	 * 
	 * public int getCurrentFloor() { return currFloor; }
	 * 
	 * public int getDirection() { return direction; }
	 * 
	 * public int getMovingTo() { return movingTo; }
	 * 
	 * public int getCar() { return this.car; }
	 * 
	 * public byte[] getBytes() { return msg; }
	 * 
	 * public String toString() { StringBuilder sb = new StringBuilder();
	 * sb.append("\nDirection: " ); if(direction == 1) { sb.append("UP"); }
	 * if(direction == 2) { sb.append("DOWN"); }
	 * 
	 * sb.append("\nOn Floor: " + currFloor); if (movingTo > 0)
	 * sb.append("\nGoing To: " + movingTo); return sb.toString(); }
	 * 
	 */
}
