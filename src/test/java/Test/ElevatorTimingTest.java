package Test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import Elevator.ElevatorController;
import Floor.FloorController;
import Scheduler.Scheduler;

public class ElevatorTimingTest {
	
	static Scheduler sched;
	static FloorController fCtrl;
	static ElevatorController eCtrl;
	
	@BeforeClass
	public static void runBeforeClass() {
		sched = new Scheduler(2);
		eCtrl = new ElevatorController(2,8, 3000);
		fCtrl = new FloorController(8);
	}
	
	
	@Test
	public void timeTest() {
		sched.start();
		eCtrl.start();
		fCtrl.start();
		
		
		
	}

}
