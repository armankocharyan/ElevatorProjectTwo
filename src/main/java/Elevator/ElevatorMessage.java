package Elevator;

public class ElevatorMessage {
	public static final int SIZE = 7;
	public static final byte SEP = '*';
	
	int direction = 0; //1 -> UP, 2 -> DOWN
	int currFloor;
	int movingTo;
	
	byte[] msg;
	
	public  ElevatorMessage(int direction, int currFloor, int movingTo) {
		this.direction = direction;
		this.movingTo = movingTo;
		this.currFloor = currFloor;
		
		msg = new byte[7];
		msg[0] = SEP;
		msg[1] = (byte)this.direction;
		msg[2] = SEP;
		msg[3] = (byte)this.currFloor;
		msg[4] = SEP;
		msg[5] = (byte)this.movingTo;
		msg[6] = SEP;
	}
	
	public ElevatorMessage(byte[] msg) {
		this.msg = msg;
		
		this.direction = (int)msg[1];
		this.currFloor = (int)msg[3];
		this.movingTo = (int)msg[5];
	}
	
	public boolean isUp() {
		return direction == 1;
	}
	
	public boolean isDown() {
		return direction == 2;
	}
	
	public int getCurrentFloor() {
		return currFloor;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getMovingTo() {
		return movingTo;
	}
	
	public byte[] getBytes() {
		return msg;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nDirection: " );
		if(direction == 1) {
			sb.append("UP");
		}
		if(direction == 2) {
			sb.append("DOWN");
		}
		
		sb.append("\nOn Floor: " + currFloor);
		if (movingTo > 0) sb.append("\nGoing To: " + movingTo);
		return sb.toString();
	}
}