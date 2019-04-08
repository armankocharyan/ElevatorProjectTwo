package Test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import Elevator.ElevatorController;
import Floor.FloorController;
import Scheduler.Scheduler;

public class ElevatorTimingTest {
	
	
	static Async tester;
	static FloorController fCtrl;
	static ElevatorController eCtrl;
	
	
	@BeforeClass
	public static void runBeforeClass() {
		tester = new Async();
		fCtrl = new FloorController();
		eCtrl = new ElevatorController();
	}
	
	


}
