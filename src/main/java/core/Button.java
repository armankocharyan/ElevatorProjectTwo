package core;

public class Button {

	boolean pressed = false;
	boolean active = true;
	String name = "";
	
	public Button(String s, boolean active) {
		this.active = active;
		this.pressed = false;
		this.name = s;
	}
	
	public void setPressed(boolean pressed) {
		
		this.pressed = pressed;
		if (pressed) System.out.println(name+" PRESSED.");
		else System.out.println(name+" UNPRESSED.");
	}
	
	public void setActive(boolean active) {
		this.active = active;
		if (pressed) System.out.println(name+" active.");
		else System.out.println(name+" inactive.");
	}
	
	public boolean isPressed() {
		return this.pressed;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
}
