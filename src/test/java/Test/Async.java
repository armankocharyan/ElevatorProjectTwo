package Test;

import Elevator.ElevatorController;
import Floor.FloorController;
import Scheduler.Scheduler;

public class Async{
	Thread sched;
	Scheduler s;
	
	
	RuntimeException exc;
	
	public Async() {
		s = new Scheduler(2,8);
		
		sched = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s.start();
					
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					exc = e;
				}
			}
		});
	}
	
	public void start() {
		sched.start();
	}
	
	public void test() throws InterruptedException{
		sched.join();
		if (exc != null) throw exc;
	}
}