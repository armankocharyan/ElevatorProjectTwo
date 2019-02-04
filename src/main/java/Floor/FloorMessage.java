package Floor;


public class FloorMessage {
	
	public static final int SIZE = 16;
	public static final byte SEP = '*';
	
	int direction = 0; //1 -> UP, 2 -> DOWN
	int floor = 0;
	int currFloor;
	
	byte[] msg;
	
	public FloorMessage(int direction, int currFloor, int floor) {
		this.direction = direction;
		this.floor = floor;
		this.currFloor = currFloor;
		
		msg = new byte[7];
		msg[0] = SEP;
		msg[1] = (byte)this.direction;
		msg[2] = SEP;
		msg[3] = (byte)this.currFloor;
		msg[4] = SEP;
		msg[5] = (byte)this.floor;
		msg[6] = SEP;
	}
	
	public FloorMessage(byte[] msg) {
		this.msg = msg;
		
		this.direction = (int)msg[1];
		this.currFloor = (int)msg[3];
		this.floor = (int)msg[5];
	}
	
	public boolean isUp() {
		return direction == 1;
	}
	
	public boolean isDown() {
		return direction == 2;
	}
	
	public int getFloorNum() {
		return floor;
	}
	
	public int getRequestingFloor() {
		return currFloor;
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
		
		sb.append("\nFloor Requesting: " + currFloor +"\nFloor Requested: " + floor);
		return sb.toString();
	}
	
	
}
