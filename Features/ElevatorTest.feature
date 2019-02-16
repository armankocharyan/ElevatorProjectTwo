#Feature file for the test code located in the directory src/test/java
#For each step of the scenario there is a written test
#To run the tests right click on CucumberRunner.java file in the src/test/java folder and run it as a junit


Feature: Testing the elevator subsystem

  Scenario: Checking the data log in the Scheduler and Elevator subsystems
    Given all the subsystems are running concurrently 
    Then check to see if there is a floor request from the input file
    Then check to see if the scheduler is listening
    Then check to see if the elevator has picked up a person
    Then check to see if the elevator is taking the person to the correct floor
    Then check to see if the there was an arrival in the floor