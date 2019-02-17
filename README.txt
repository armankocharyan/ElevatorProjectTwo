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


Parts worked on by each member:

Arman Kocharyan
- Worked on test cases, timing of elevator carts, created cucumber tests and the logger, Elevator message, which is the default message class for all UDP messages to and from out three subsystems

Kaitie Nelson
-Worked on floor subsystem arrival, and floor controller, sensors working, and implemented pickUpPerson, rideToFloor, the event listener which opens a socket on a given port and listens until message is received.

Andrew Dodge
-Worked on UML class diagram, startListen which starts a new thread/daemon that blocks and waits call

Mohamed Gahelrasoul
-Documentation, timing diagrams, worked on resetting the buttons, and elevator arrival

Roman Kishinevsky
- Requesting elevator up and down, with the up down buttons, small interactions with the elevator