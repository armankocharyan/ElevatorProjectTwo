package Elevator;

import javax.swing.JRadioButton;

public class Motor {
	
	JRadioButton motorOn;
	JRadioButton active;
	
	public Motor(JRadioButton on, JRadioButton active) {
		this.motorOn = on;
		this.active = active;
		
	}
	
	public void setMotorOn() {
		this.motorOn.setSelected(true);
		this.active.setSelected(true);
	}
	
	public void setMotorOff() {
		this.motorOn.setSelected(false);
		this.active.setSelected(true);
	}

	// TODO : implement motor class to control our timing 
}
