package core;

public class Constants {
	
	public static final int NUM_FLOORS = 22;
	public static final int NUM_CARS = 4;
	public static final int ELEV_PORT = 69;
	public static final int FLOOR_PORT = 62442;
	public static final int SCHED_PORT = 24;
	public static final int MS_PER_FLOOR = 3000;
	public static final int MS_FOR_DOOR = 1000;
	public static final String FLOOR_ADDR = "";
	public static final String SCHEDULER_ADDR = "";
	public static final String ELEV_ADDR = "";
	
	public enum DIR { 
		NONE(0),
		UP(1), 
		DOWN(2),
		DEAD(3);
		
		int code;
		
		DIR(int num) {
			this.code = num;
		}
		
		public int getCode() {
			return this.code;
		}
		
		public static DIR fromCode(int num) {
			DIR ret;
			switch(num) {
				case 1: ret = UP; break;
				case 2: ret = DOWN; break;
				case 3: ret = DEAD; break;
				default: ret = NONE; break;
			}
			return ret;
		}
		
		};
		
		

}
