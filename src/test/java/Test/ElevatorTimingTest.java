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
		fCtrl = new FloorController(8);
		eCtrl = new ElevatorController(2,8, 7000);
	}
	
	
	@Test(expected = RuntimeException.class)
	public void timeTest() {
		
		tester.start();
		fCtrl.start();
		eCtrl.start();
		fCtrl.requestFloor(7, 1);
		
		while (fCtrl.isWaiting()) {
			try {
				try {
					tester.test();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Thread.sleep(500);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
