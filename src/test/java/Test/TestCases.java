package Test;
import junit.framework.TestCase;

import Elevator.ElevatorController;
import Floor.FloorController;
import Scheduler.Scheduler;

import File.ReadFile;
import Logger.Logger;

public class TestCases extends TestCase{
	
	
	
	private String [] arguments = null;
	
	
	public void startTest() throws InterruptedException {
		Logger.clearAllTextFiles();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() 
            { 
				Scheduler.main(arguments);
				
            } 
		});
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() 
            { 
				ElevatorController.main(arguments);
				
            } 
		});
		
		
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() 
            { 
				FloorController.main(arguments);
				
            } 
		});
		
		t1.start();
		Thread.sleep(1000);
		t2.start();
		Thread.sleep(1000);
		t3.start();
		Thread.sleep(30000);

	}
	
	
	//Testing Hand Identifying Methods
	public void testIsPairCase1() {
		
		
	}

}
