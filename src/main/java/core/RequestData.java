package core;

public class RequestData {
	
	//global variables
	private int fault;
	private int floorNumber;
	private int floorToGo;
	private boolean requestUp;
	
	//constructor 
	public RequestData(int fault, int floorNumber, int floorToGo, boolean requestUp) {
		this.fault = fault;
		this.floorNumber = floorNumber;
		this.floorToGo = floorToGo;
		this.requestUp = requestUp;
	}
	
	//getters
	public int getFault() {
		return this.fault;
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
		
		return "Fault: [" + fault + "] Floor Number: [" + floorNumber + "] Floor to Go: [" + floorToGo + "] Going UP: [" + Boolean.toString(requestUp) + "]";
		
	}
}
