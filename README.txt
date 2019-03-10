GROUP NUMBER: 5
LAB SECTION: 5

To run the code:
open eclipse ide and import project as a maven project
- open the subsystems directory
1) right click elevator and run as java program
2) right click scheduler and click run as a java program
3) right click floor and click run as a java program

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