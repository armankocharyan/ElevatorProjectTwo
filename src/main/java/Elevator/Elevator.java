package Elevator;

import core.Constants;
import core.ElevatorMessage;
import core.EventNotifier;
import core.Constants.DIR;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.PriorityQueue;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import Logger.Logger;


/**
 * 
 * Represents one elevator CAR
 *
 */
public class Elevator {
	
	// ----- STATIC VARIABLES ----- //
	public static final String LOG_FILE_NAME = "TestLogs/elevator.testing";
	public static final String ADDRESS = ""; //Change this to the address of the scheduler PC. Leave it blank ("") to run locally
	public static final String elevatorTestLogFileName = "TestLogs/elevator.testing";
	
	// ----- MEMBER VARIABLES ----- //
	
	public DIR dir = DIR.NONE;
	PriorityQueue<Integer> up = new PriorityQueue<Integer>(Constants.NUM_FLOORS);
	PriorityQueue<Integer> down = new PriorityQueue<Integer>(Constants.NUM_FLOORS, Collections.reverseOrder());
	Integer currFloor = 0;
	int carNum = 0;
	Calendar cal;
	SimpleDateFormat time;
	String fileName = "Logs/elevator.log";
	
	// ----- GUI VARIABLES ----- //
	JRadioButton active;
	JRadioButton motorOn;
	JRadioButton UPLamp;
	JRadioButton DOWNLamp;
	JRadioButton openDoor;
	JRadioButton closeDoor;
	JLabel currentFloor;
	
	public Door door;
	
	BlockingEventNotifier notif = new BlockingEventNotifier(Constants.SCHED_PORT, "ELEVATOR");
	
	public Elevator(int carNum) {
		this.door =  new Door(false);
		this.carNum = carNum;
		if(notif == null) {
			notif = new BlockingEventNotifier(Constants.SCHED_PORT, "ELEVATOR");
		}
		this.time = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	public Elevator(int carNum, JRadioButton active, JRadioButton motorOn,  JRadioButton UPLamp, 
			JRadioButton DOWNLamp, JRadioButton openDoor, JRadioButton closeDoor, JLabel currentFloor) {
		
		
		this.carNum = carNum;
		
		//setting the gui
		
		this.active = active;
		this.active.setSelected(true);;
		this.motorOn = motorOn;
		this.motorOn.setSelected(true);
		this.UPLamp = UPLamp;
		this.DOWNLamp = DOWNLamp;
		this.openDoor = openDoor;
		this.closeDoor = closeDoor;
		this.currentFloor = currentFloor;
		
		this.door =  new Door(false, openDoor, closeDoor);
		
		if(notif == null) {
			notif = new BlockingEventNotifier(Constants.SCHED_PORT, "ELEVATOR");
		}
		this.time = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	public void receiveFault(int floor) {
		up.clear();
		down.clear();
		this.dir = DIR.DEAD;
		this.motorOn.setSelected(false);
		this.active.setSelected(false);
		System.out.println("\nELEVATOR " + carNum+" HAS FAULTED ON FLOOR " + floor + " AND IS OUT OF COMMISSION");
		cal = Calendar.getInstance();
		Logger.write("\nELEVATOR " + carNum+" HAS FAULTED ON FLOOR " + floor + " AND IS OUT OF COMMISSION " + time.format(cal.getTime()), fileName); 
		ElevatorMessage msg = new ElevatorMessage(ElevatorMessage.MessageType.FAULT, carNum, floor);
		EventNotifier notif = new EventNotifier(Constants.SCHED_PORT,"ELEVATOR FAULT NOTIFICATION");
		notif.sendMessage(msg, Constants.SCHEDULER_ADDR);
		
	}
	
	public void receiveRequest(int floor, DIR direction) {
		if(floor == -1) return;
		System.out.println("\nELEVATOR " + carNum+"  RECEIVED REQUEST GOING " + direction + " ON FLOOR " + floor);
		cal = Calendar.getInstance();
		Logger.write("\nELEVATOR " + carNum+"  RECEIVED REQUEST GOING " + direction + " ON FLOOR " + floor + " " + time.format(cal.getTime()), fileName); 
		if (floor > currFloor) {
			this.dir = DIR.UP;
			UPLamp.setSelected(true); //gui update
			DOWNLamp.setSelected(false); //gui update
		}
		else if (floor < currFloor) {
			this.dir = DIR.DOWN;
			DOWNLamp.setSelected(true); //gui update
			UPLamp.setSelected(false); //gui update
		}
		else {
			UPLamp.setSelected(true); //gui update
			DOWNLamp.setSelected(false); //gui update
			this.dir = direction;
			ElevatorMessage msg = new ElevatorMessage(ElevatorMessage.MessageType.ARRIVAL, carNum, dir.getCode(), currFloor);
			int code = notif.sendMessage(msg, ADDRESS);
			if(dir == DIR.UP) up.add(code);
			else down.add(code);
			moveFloor();
		}
		if (direction == DIR.UP && floor != currFloor) {
			up.add(floor);
			moveFloor();
		}
			
		else if (direction == DIR.DOWN && floor != currFloor) {
			down.add(floor);
			moveFloor();
		}
			
		
		
	}
	
	void moveFloor() {
		System.out.println("\n");
		cal = Calendar.getInstance();
		Logger.write("\n", fileName); 
		if(dir == DIR.NONE) {
			ElevatorMessage msg = new ElevatorMessage(ElevatorMessage.MessageType.EMPTY, carNum);
			EventNotifier notif = new EventNotifier(Constants.SCHED_PORT,"ELEVATOR EMPTY NOTIFICATION");
			notif.sendMessage(msg, Constants.SCHEDULER_ADDR);
		}
		else {
			// doors close if not already closed
			System.out.println("ELEVATOR " + carNum+"  LEAVING FLOOR " + currFloor);
			cal = Calendar.getInstance();
			Logger.write("ELEVATOR " + carNum+"  LEAVING FLOOR " + currFloor + " " +  time.format(cal.getTime()) + "\n", fileName); 
			Elevator c = this;
			new java.util.Timer().schedule(
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	c.move();
			            }
			        }, Constants.MS_PER_FLOOR
				);
		}
		
	}
	
	public void move() {
		
		if(dir == DIR.UP) {
			if(currFloor == (Constants.NUM_FLOORS-1)) {
				// shouldn't be here
				System.out.println("ELEV" + carNum+" ERROR: TRYING TO GO UP ON HIGHEST FLOOR");
				cal = Calendar.getInstance();
				Logger.write("ELEV" + carNum+" ERROR: TRYING TO GO UP ON HIGHEST FLOOR" + " " +  time.format(cal.getTime()) + "\n", fileName); 
				return;
			}
			
			currFloor += 1;
			currentFloor.setText(currFloor.toString()); //updating gui
			System.out.println("ELEVATOR " + carNum+ " ON FLOOR "+ currFloor);
			cal = Calendar.getInstance();
			Logger.write("ELEVATOR " + carNum+ " ON FLOOR "+ currFloor + " " +  time.format(cal.getTime()) + "\n", fileName); 
			
			if (up.isEmpty() && !down.isEmpty() && down.peek() <= currFloor) {
				dir = DIR.DOWN;
				UPLamp.setSelected(false); //gui update
				DOWNLamp.setSelected(true); //gui update
			}
		}
		else if(dir == DIR.DOWN) {
			if (currFloor == 0) {
				// shouldn't be here
				System.out.println("ELEV ERROR: TRYING TO GO DOWN ON LOWEST FLOOR");
				cal = Calendar.getInstance();
				Logger.write("ELEV ERROR: TRYING TO GO DOWN ON LOWEST FLOOR" + " " +  time.format(cal.getTime()) + "\n", fileName); 
				return;
			}
			currFloor -= 1;
			currentFloor.setText(currFloor.toString()); //updating gui
			System.out.println("ELEVATOR" + carNum+" ON FLOOR "+ currFloor);
			cal = Calendar.getInstance();
			Logger.write("ELEVATOR" + carNum+" ON FLOOR "+ currFloor + " " +  time.format(cal.getTime()) + "\n", fileName); 
			if (down.isEmpty() && !up.isEmpty() && up.peek() >= currFloor) {
				dir = DIR.UP;
				UPLamp.setSelected(true); //gui update
				DOWNLamp.setSelected(false); //gui update
			}

		}
		else {
			// shouldnt be here
			System.out.println("ELEV ERROR: TRYING TO MOVE IN DIR NONE");
			cal = Calendar.getInstance();
			Logger.write("ELEV ERROR: TRYING TO MOVE IN DIR NONE" + " " +  time.format(cal.getTime()) + "\n", fileName); 
					
		}
		
		
		
		boolean stop = false;
		
		ElevatorMessage msg = new ElevatorMessage(ElevatorMessage.MessageType.ARRIVAL, carNum, dir.getCode(), currFloor);
		
		int code = notif.sendMessage(msg, ADDRESS);
		if(msg.getId() != carNum) code = -1;
		
		if (code >= 0) {
			if (dir == DIR.UP) {
				if(!up.contains(code)) {
					System.out.println("ELEVATOR " + carNum+ ": ADDED NEW REQUEST UP TO FLOOR " + code);
					cal = Calendar.getInstance();
					Logger.write("ELEVATOR " + carNum+ ": ADDED NEW REQUEST UP TO FLOOR " + code + " " +  time.format(cal.getTime()) + "\n", fileName); 
							
					up.add(code);
				}
				if(!up.contains(currFloor)) {
					up.add(currFloor);
				}
				
			}
			else if (dir == dir.DOWN){
				if (!down.contains(code)) {
					System.out.println("ELEVATOR "+ carNum+": ADDED NEW REQUEST DOWN TO FLOOR " + code);

					cal = Calendar.getInstance();
					Logger.write("ELEVATOR "+ carNum+": ADDED NEW REQUEST DOWN TO FLOOR " + code + " " +  time.format(cal.getTime()) + "\n", fileName); 
					
					down.add(code);
				}
				if(!down.contains(currFloor)) {
					down.add(currFloor);
				}
				
			}
		}
		else if (code == -2) {
			System.out.println("ELEVATOR: BLOCKING NOPTIFIER DIDNT WORK");
			cal = Calendar.getInstance();
			Logger.write("ELEVATOR: BLOCKING NOPTIFIER DIDNT WORK" + " " +  time.format(cal.getTime()) + "\n", fileName); 
			
			
		}
		else if (code == -1) {
			System.out.println("ELEVATOR "+ carNum+": NO NEW REQUEST ON FLOOR " + currFloor);
			cal = Calendar.getInstance();
			Logger.write("ELEVATOR "+ carNum+": NO NEW REQUEST ON FLOOR " + currFloor + " " +  time.format(cal.getTime()) + "\n", fileName); 
		}
		else if (code == -3) {
			System.out.println("ELEVATOR: UNKNOWN MESSAGE " + currFloor);
			cal = Calendar.getInstance();
			Logger.write("ELEVATOR: UNKNOWN MESSAGE " + currFloor + " " +  time.format(cal.getTime()) + "\n", fileName); 

		}
		
		System.out.println("ELEVATOR " + carNum+" UP: " + up);
		System.out.println("ELEVATOR "+ carNum+ " DOWN: " + down);

		cal = Calendar.getInstance();
		Logger.write("ELEVATOR " + carNum+" UP: " + up +  time.format(cal.getTime()) + "\n", fileName); 
		cal = Calendar.getInstance();
		Logger.write("ELEVATOR "+ carNum+ " DOWN: " + down +  time.format(cal.getTime()) + "\n", fileName); 
		
		if(dir == DIR.UP) {
			if(up.peek() == currFloor) {
				stop = true;
				up.remove();
			}
			
			if(up.isEmpty()) {
				if(!down.isEmpty()) {
					if (down.peek() <= currFloor) {
						dir = DIR.DOWN;
						UPLamp.setSelected(false); //gui update
						DOWNLamp.setSelected(true); //gui update
					}
				}
				else {
					dir = DIR.NONE;
					UPLamp.setSelected(false); //gui update
					DOWNLamp.setSelected(false); //gui update
				}
			}
		}
		else if (dir == DIR.DOWN) {
			if(down.peek() == currFloor) {
				stop = true;
				down.remove();
			}
			if(down.isEmpty()) {
				if (!up.isEmpty()) {
					if(up.peek() >= currFloor) {
						dir = DIR.UP;
						UPLamp.setSelected(true); //gui update
						DOWNLamp.setSelected(false); //gui update
					}
				}
				else {
					dir = DIR.NONE;
					UPLamp.setSelected(false); //gui update
					DOWNLamp.setSelected(false); //gui update
					
				}
			}
		}
		
		if (stop) {
			System.out.println("ELEVATOR " + carNum+": DOORS OPENING ");
			cal = Calendar.getInstance();
			Logger.write("ELEVATOR " + carNum+": DOORS OPENING " + " " + time.format(cal.getTime()) + "\n", fileName); 
			
			door.open();
			ElevatorMessage newMsg = new ElevatorMessage(ElevatorMessage.MessageType.DOORS, carNum, currFloor);
			EventNotifier notif = new EventNotifier(Constants.SCHED_PORT,"ELEVATOR DOORS OPEN NOTIFICATION");
			notif.sendMessage(newMsg, Constants.SCHEDULER_ADDR);
			// ANNOUNCE OPEN DOORS
			Elevator c = this;
			new java.util.Timer().schedule(
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	c.door.close();
			            	System.out.println("ELEVATOR "+ carNum+": DOORS CLOSING");
			            	cal = Calendar.getInstance();
			    			Logger.write("ELEVATOR "+ carNum+": DOORS CLOSING" + " " +  time.format(cal.getTime()) + "\n", fileName); 
			            	c.moveFloor();
			            }
			        }, Constants.MS_FOR_DOOR
				);
			
		}
		
		else{
			moveFloor();
		}
		
	}
	
}

class BlockingEventNotifier{
	// -- INSTANCE VARIABLES -- //
		protected InetAddress localhost;
		int PORT;
		String name = "";
		
		// -- CONSTRUCTOR -- //
		public BlockingEventNotifier(int PORT, String name) {
			this.PORT = PORT;
			this.name = name;
			try {
				this.localhost = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
			
		/**
		 * Opens a new and temporary socket on any available port to
		 * send a datagram to the port stored in this class. This does
		 * not block or wait for a response, simply sends a notification.
		 * 
		 * @param msg the ElevatorMessage instance we want to send
		 */
		public int sendMessage(ElevatorMessage msg, String address) {
			ElevatorMessage ret = null;
			try {
				// Constructs and opens a new packet/socket 
				
				DatagramSocket sendSocket = new DatagramSocket();
				
				
				//inter-computer communication
				DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), ElevatorMessage.SIZE, localhost, this.PORT);
				
				if(!address.equals("")) {
					
					InetAddress targetAddress = null;
					try {
						targetAddress = InetAddress.getByName(address);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					sendPacket = new DatagramPacket(msg.getBytes(), ElevatorMessage.SIZE, targetAddress, this.PORT);
				}
					
				
				System.out.print(name);
				System.out.println(": SENDING EVENT INFORMATION" + msg);
				try {
					// send the packet
					sendSocket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				byte[] data = new byte[ElevatorMessage.SIZE];
				
				DatagramPacket recv = new DatagramPacket(data, data.length);
				sendSocket.receive(recv);
				System.out.println(name + ": RECEIVED NOTIFICATION");
				ret = new ElevatorMessage(recv.getData());
				
				// close the socket and return
				sendSocket.close();
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(ret == null) {
				return -2;
			}
			else {
				if (ret.getType() == ElevatorMessage.MessageType.STOP) return ret.getData().get(0);
				else if (ret.getType() == ElevatorMessage.MessageType.CONT) return -1;
				else return -3;
			}
		}
}
