package Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;


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

	public static void clearAllLogFiles() {

		Logger.clearText("Logs/button.log");
		Logger.clearText("Logs/door.log");
		Logger.clearText("Logs/lamp.log");
		Logger.clearText("Logs/scheduler.log");
		Logger.clearText("Logs/elevator.log");

	}
	public static void main(String[] args) {
		
		LinkedList<Integer> list = new LinkedList<Integer>();
		
		list.add(13);
		list.add(56);
		list.add(23);
		list.add(12);
		list.add(78);
		Collections.sort(list);
		Collections.reverse(list);
		System.out.println(list);
		

	}

}
