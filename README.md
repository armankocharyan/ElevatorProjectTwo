ELEVATOR PROJECT
GROUP NUMBER: 5
LAB SECTION: 5

To run the code:
-	Open eclipse ide and import project as a maven project
-	Open the “gui directory
1) Right click “MainView.java” file and click “run”

To run on multiple computers:
open eclipse ide and import project as a maven project on both machines
- open the subsystems directory
1) in the core package run the IPGetter.java file on both machines
2) in the scheduler package open the Scheduler.java file on machine 1, set the public static final String ADDRESS = ""; to the IP address of machine 2
	add it inside the ""
3) open the Floor.java file from floor package and Elevator.java file from the elevator package, and add machine 1's IP address to the public static final String ADDRESS = ""; variable in those files
4) run Scheduler.java on machine 1
5) run ElevatorController.java followed by FloorController.java on machine 2

To run the tests 
- run CucumberRunner.java as a junit test


Description: 

Door.java
	The class which controls the doors of the elevator.
	
Elevator.java
	Represents a single elevator car connected to our ElevatorController. This class tracks where our car is
		at any point in time and notifies our scheduler when it has arrived at a floor. 
		
ElevatorController.java	
	This class controls the elevators and receives messages from the scheduler. It acts as our server class.
	
Motor.java
	Currently empty class, will be used in the future.
	

File package
------------
ReadFile.java
	This class holds static methods that will parse the input file.
	
	
Floor package
-------------
Floor.java
	Represents a single floor connected to our FloorController. This class tracks when a car is
		requested or has arrived at a floor and notifies our scheduler.
	
FloorController.java
		This class controls the floors and receives messages from the scheduler. It acts as our client class.

		
Logger package	
--------------
Logger.java	
	This class contains static methods that are called to log data for testing reasons
	

Scheduler package
-----------------
Scheduler.java
	Processes the messages between the floors and the elevators, sending the specific requests and messages to their
		proper destinations based on what it has received. This class acts as our intermediateHost class.

		
Breakdown of Responsibilities
-----------------------------
Arman Kocharyan
	Worked on exceptions and testing 
	
Katie Nelson
	Worked on exceptions and testing
	
Andrew Dodge
	Worked on timing and diagrams
	
Mohamed Gahelrasoul
	Worked on timing and diagrams
	
Roman Kishinevsky
	Worked on timing and diagrams
