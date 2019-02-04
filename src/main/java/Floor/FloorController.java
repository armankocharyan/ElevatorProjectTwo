package Floor;

public class FloorController {
	
	public static final int PORT = 62442;
	
	int numFloors;
	Floor[] floors;
	
	public FloorController(int numFloors) {
		this.numFloors = numFloors;
		
		floors = new Floor[numFloors];
		
		for(int i=0; i<numFloors; i++) {
			floors[i] = new Floor(i, (i == numFloors-1), (i==0));
		}
		
		for(Floor f : floors) {
			f.start();
		}
	}
	
	public static void main(String[] args) {
		FloorController c = new FloorController(3);
	}
	
}
