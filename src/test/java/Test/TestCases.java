package Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import Elevator.ElevatorController;
import Floor.FloorController;
import Scheduler.Scheduler;
import core.RequestData;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import File.ReadFile;
import Logger.Logger;

public class TestCases{

	private final String [] args = null; //used as a parameter to call main methods
	private ArrayList<RequestData> inputData;
	
	//this method executes the subsystems concurrently
	@Given("^all the subsystems are running concurrently$")
	public void runningSubsystems() throws InterruptedException{
		
		ArrayList<String> schedulerTestLog = ReadFile.parseInput(Scheduler.schedulerTestLogFileName);
	    boolean itIsThere = false;
	    for(int i = 0; i < schedulerTestLog.size(); i++) {
	    	if (schedulerTestLog.get(i).contains("Starting listener")){
	    		itIsThere  = true;
	    	}
	    }
	    
	   assertEquals(itIsThere , true);
	    
	}
	
	@Then("^check to see if there is a floor request from the input file$")
	public void check_to_see_if_there_is_a_floor_request_from_the_input_file() throws Throwable {
		ArrayList<String> floorTestLog = ReadFile.parseInput(Floor.Floor.floorTestLogFileName);
	    boolean itIsThere = false;
	    for(int i = 0; i < floorTestLog.size(); i++) {
	    	if (floorTestLog.get(i).contains("REQUESTED AN ELEVATOR")){
	    		itIsThere  = true;
	    	}
	    }
	    
	   assertEquals(itIsThere , true);
	}
	
	@Then("^check to see if the scheduler is listening$")
	public void check_to_see_if_the_scheduler_is_listening() throws Throwable {
		ArrayList<String> schedulerTestLog = ReadFile.parseInput(Scheduler.schedulerTestLogFileName);
	    boolean itIsThere = false;
	    for(int i = 0; i < schedulerTestLog.size(); i++) {
	    	if (schedulerTestLog.get(i).contains("SCHEDULER: Starting listener")){
	    		itIsThere  = true;
	    	}
	    }
	    
	   assertEquals(itIsThere , true);
	}
	
	@Then("^check to see if the elevator has picked up a person$")
	public void check_to_see_if_the_elevator_has_picked_up_a_person() throws Throwable {
		
	    ArrayList<String> elevatorTestLog = ReadFile.parseInput(Elevator.Elevator.elevatorTestLogFileName);
	    boolean itIsThere = false;
	    for(int i = 0; i < elevatorTestLog.size(); i++) {
	    	if (elevatorTestLog.get(i).contains("RECEIVED REQUEST GOING" )){
	    		itIsThere  = true;
	    	}
	    }
	    
	   assertEquals(itIsThere , true);
	}
	
	@Then("^check to see if the elevator is taking the person to the correct floor$")
	public void check_to_see_if_the_elevator_is_taking_the_person_to_the_correct_floor() throws Throwable {
		
		ArrayList<String> elevatorTestLog = ReadFile.parseInput(Elevator.Elevator.elevatorTestLogFileName);
	    boolean itIsThere = false;
	    for(int i = 0; i < elevatorTestLog.size(); i++) {
	    	if (elevatorTestLog.get(i).contains("ON FLOOR")){
	    		itIsThere  = true;
	    	}
	    }
	    
	   assertEquals(itIsThere , true);
	}


}
