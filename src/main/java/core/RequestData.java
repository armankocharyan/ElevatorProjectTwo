package core;

public class RequestData {
	
	private String time;
	private int floorNumber;
	private int floorToGo;
	private boolean requestUp;
	
	public RequestData(String time, int floorNumber, int floorToGo, boolean requestUp) {
		this.time = time;
		this.floorNumber = floorNumber;
		this.floorToGo = floorToGo;
		this.requestUp = requestUp;
	}
	
	//getters
	public String getRequestTime() {
		return this.time;
	}
	
	public int getFloorNumber() {
		return this.floorNumber;
	}
	
	public int getfloorToGo() {
		return this.floorToGo;
	}
	
	public boolean goingUp() {
		return requestUp;
	}
	
	public String toString() {
		
		return "Time: [" + time + "] Floor Number: [" + floorNumber + "] Floor to Go: [" + floorToGo + "] Going UP: [" + Boolean.toString(requestUp) + "]";
		
	}
}
