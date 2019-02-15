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
		Logger.clearText("elevator.receive.testing");
		Logger.clearText("elevator.response.testing");
		Logger.clearText("floor.receive.testing");
		Logger.clearText("floor.send.testing");
		Logger.clearText("scheduler.receive.testing");
		Logger.clearText("scheduler.forward.testing");
		Logger.clearText("scheduler.send.testing");
	}
	
	
	
	
}
