package Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;


public class Logger {
    // THIS CLASS HAS STATIC METHODS THAT ARE CALLED TO LOG DATA FOR TESTING REASON
	
	
    //outputs rawString to file name output
	public static void write(String rawString, String fileName){
        try {
            FileWriter fw = new FileWriter(fileName,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter outFile = new PrintWriter(bw, true);
            outFile.println(rawString);
            outFile.flush();
            outFile.close();

        } catch(Exception e) {
            System.err.println("Could not write to file, Exception: " + e.getMessage());
        }
    }
	//clears all text in file name
	public static void clearText (String fileName){
        try {
            FileWriter fw = new FileWriter(fileName,false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter outFile = new PrintWriter(bw, false);
            outFile.flush();
            outFile.close();

        } catch(Exception e) {
            System.err.println("Could not write to file, Exception: " + e.getMessage());
        }
    }
	
	//clears all the text from the logs
	public static void clearAllTextFiles() {
		Logger.clearText("TestLogs/elevator.testing");
		Logger.clearText("TestLogs/scheduler.testing");
		Logger.clearText("TestLogs/floor.testing");
	}
	
	
	public static void testFunction(int time) {
		
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		                System.out.println("Excecuting");
		            }
		        }, 
		        time
		);
		
	}
	
	public static void testFunction2(int time) throws InterruptedException {
		
		Thread.sleep(5000);
		
		
		System.out.println("");
		
	}
	
	

	
	public static void main(String[] args) {

		testFunction(15000);
		
		System.out.println("Roman is very handsome");
		
		
	}
	
}
