package core;

public class Button {

	boolean pressed = false;
	boolean active = false;
	
	public Button(boolean active) {
		this.active = active;
		this.pressed = false;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isPressed() {
		return this.pressed;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
}
