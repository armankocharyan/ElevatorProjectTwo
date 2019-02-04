package Elevator;

public class ElevatorController {

	Elevator[] elevators = null;
	int numElevators = 0;
	int numFloors;
	
	public ElevatorController(int numElevators, int numFloors) {
		this.numElevators = numElevators;
		this.numFloors = numFloors;
		elevators = new Elevator[numElevators];
		for(int i=0; i<numElevators; i++) {
			elevators[i] = new Elevator(i, numFloors);
		}
		
		elevators[0].arriveAtFloor(1);
		elevators[0].announceFloor(-1);
	}
	
	public static void main(String[] args) {
		ElevatorController c = new ElevatorController(1, 3);
	}
	
}
