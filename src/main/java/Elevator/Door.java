package Elevator;

public class Door {

	boolean isOpen = false;
	
	public Door(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public void open() {
		this.isOpen = true;
	}
	
	public void close() {
		this.isOpen = false;
	}
	
	public boolean isOpen() {
		return this.isOpen();
	}
}
