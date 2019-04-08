package Scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JTextArea;

import core.Constants;
import core.ElevatorMessage;
import core.EventListener;
import core.EventNotifier;

public class Scheduler {

	public static final String ADDRESS = "";

	EventListener listener;
	EventNotifier elevatorNotifier;
	
	JTextArea textBox;

	private Calendar cal;
	private SimpleDateFormat time;
	
	LinkedList<ArrayList<Integer>> queue = new LinkedList<ArrayList<Integer>>();
	HashMap<Integer, Integer> reqUp = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> reqDown = new HashMap<Integer, Integer>();
	 
	HashMap<Integer, Integer> processedUp = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> processedDown = new HashMap<Integer, Integer>();
	
	int processing = 0;

	public Scheduler() {
		listener = new EventListener(Constants.SCHED_PORT, "SCHEDULER LISTENER");
		elevatorNotifier = new EventNotifier(Constants.ELEV_PORT, "SCHEDULER ELEVATOR NOTIFIER");
		this.time = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	public Scheduler(JTextArea textBox) {
		listener = new EventListener(Constants.SCHED_PORT, "SCHEDULER LISTENER");
		elevatorNotifier = new EventNotifier(Constants.ELEV_PORT, "SCHEDULER ELEVATOR NOTIFIER");
		this.time = new SimpleDateFormat("HH:mm:ss.SSS");
		
		this.textBox = textBox;
	}

	public void startListen() throws InterruptedException {
		

		System.out.println("SCHEDULER: Starting listener...");
		cal = Calendar.getInstance();
		textBox.append("SCHEDULER: Starting listener..."  + time.format(cal.getTime()) + "\n");

		for (;;) {
			ElevatorMessage msg = listener.waitForNotification();
			switch (msg.getType()) {
			case DOORS:
				cal = Calendar.getInstance();
				//textBox.append("\nCAR " + msg.getId() + " STOPPED ON FLOOR " + msg.getData().get(0) + " "+ time.format(cal.getTime()) + "\n");
				break;
			case EMPTY:
				System.out.println("\nEMPTY ELEVATOR" + msg);
				cal = Calendar.getInstance();
				textBox.append("\nCAR " + msg.getId() + " IS EMPTY " + time.format(cal.getTime()) + "\n");
				
				synchronized (this) {
					processing -= 1;

					// let the waiting threads know that we are free to process another thread
					notifyAll();
					
				}
				break;
			case REQ:
				System.out.println("\nNEW ELEVATOR REQUEST " + msg);
				cal = Calendar.getInstance();
				textBox.append("\nNEW ELEVATOR REQUEST FROM FLOOR " + msg.getId() + " GOING " + Constants.DIR.fromCode(msg.getData().get(0)) +" "+ time.format(cal.getTime()) + "\n");
				int from = msg.getId();
				int to = msg.getData().get(1);
				int fault = msg.getData().get(2);
				
				Constants.DIR direction = Constants.DIR.fromCode(msg.getData().get(0));
				
				synchronized(this) {
					ArrayList<Integer> gg = new ArrayList<Integer>();
					gg.add(from);
					gg.add(to);
					gg.add(direction.getCode());
					gg.add(fault);
					queue.add(gg);
					notifyAll();
				}

				break;
			case ARRIVAL:
				int car = msg.getId();
				Constants.DIR dir = Constants.DIR.fromCode(msg.getData().get(0));
				int currFloor = msg.getData().get(1);

				
				
				
				EventNotifier notif = new EventNotifier(msg.PORT, "RETURN MSG");
				boolean stop = false;
				int floor = -1;
				
				
				
				if (dir == Constants.DIR.UP) {
					
					if(reqUp.containsKey(currFloor)) {
						stop = true;
						floor = reqUp.get(currFloor);
						reqUp.remove(currFloor);
					}
					
					if(!stop) {
						for(int i=0; i<queue.size(); i++ ) {
							ArrayList<Integer> curr = queue.get(i);
							if ((curr.get(0) == currFloor) && (curr.get(2) == 1) && (curr.get(3) != 1)) {
								synchronized(this) {
									queue.remove(i);
								}
								stop = true;
								floor = curr.get(1);
								
								break;
							}
						}
					}
				}else {
					
					if(reqDown.containsKey(currFloor)) {
						stop = true;
						floor = reqDown.get(currFloor);
						reqDown.remove(currFloor);
					}
					
					if(!stop) {
						for(int i=0; i<queue.size(); i++ ) {
							ArrayList<Integer> curr = queue.get(i);
							if ((curr.get(0) == currFloor) && (curr.get(2) == 2) && (curr.get(3) != 1)) {
								synchronized(this) {
									queue.remove(i);
								}
								stop = true;
								floor = curr.get(1);
								break;
							}
						}
					}
				}
					
				

				ElevatorMessage newMsg;
				if (stop && floor != -1) {
					cal = Calendar.getInstance();
					System.out.println("\nCAR " + car+ " STOPPED ON FLOOR " + currFloor + " ON ITS WAY TO " + floor+" "+ time.format(cal.getTime()) + "\n");
					textBox.append("\nCAR " + car+ " STOPPED ON FLOOR " + currFloor + " ON ITS WAY TO " + floor+" "+ time.format(cal.getTime()) + "\n");
					newMsg = new ElevatorMessage(ElevatorMessage.MessageType.STOP, car, floor);
				} else {
					newMsg = new ElevatorMessage(ElevatorMessage.MessageType.CONT, car, dir.getCode());
				}
				cal = Calendar.getInstance();
				System.out.println("\nCAR " + car + " PASSING FLOOR " + currFloor + " GOING " + dir);
				textBox.append("\nCAR " + car + " PASSING FLOOR " + currFloor + " GOING " + dir + " " + time.format(cal.getTime()) + "\n");
				notif.sendMessage(newMsg, "");
				break;
			case FAULT:
				System.out.println("\nCAR " + msg.getId()+" HAS FAULTED ON FLOOR " + msg.getData().get(0) + " AND IS OUT OF COMMISSION");
				cal = Calendar.getInstance();

				textBox.append("\nCAR " + msg.getId()+" HAS FAULTED ON FLOOR " + msg.getData().get(0) + " AND IS OUT OF COMMISSION"+" "+ time.format(cal.getTime()) + "\n");
				break;
			default:
				break;
			}
		}
	}

	public synchronized void dequeue() throws InterruptedException {
		// THIS ACTS AS A CONSUMER
		for (;;) {
			while(queue.isEmpty() || processing >= Constants.NUM_CARS) {
				wait();
			}
			
			
			
			// now we are processing one request, so one car is occupied
			processing += 1;
			
			ArrayList<Integer> msg = queue.removeFirst();
			int from = msg.get(0);
			Constants.DIR dir = Constants.DIR.fromCode(msg.get(2));
			int to = msg.get(1);
			int fault = msg.get(3);

			if (dir == Constants.DIR.UP) {
				synchronized (this) {
					reqUp.put(from, to);
				}
			} else {
				synchronized (this) {
					reqDown.put(from, to);
				}
			}
			
			if(fault > 0) {
				cal = Calendar.getInstance();
				System.out.println("\nSCHEDULER: SENDING FAULT FROM FLOOR " + from);
				textBox.append("\nSCHEDULER: SENDING FAULT FROM FLOOR " + from+" "+ time.format(cal.getTime()) + "\n");
				ElevatorMessage newMsg = new ElevatorMessage(ElevatorMessage.MessageType.FAULT, from);
				elevatorNotifier.sendMessage(newMsg, Constants.ELEV_ADDR);
			}
			else {
				cal = Calendar.getInstance();
				System.out.println("\nASKING FOR ELEVATOR FROM FLOOR " + from +" GOING " + dir + " TO FLOOR " + to+" "+ time.format(cal.getTime()) + "\n");
				textBox.append("\nASKING FOR ELEVATOR FROM FLOOR " + from +" GOING " + dir + " TO FLOOR " + to+" "+ time.format(cal.getTime()) + "\n");

				ElevatorMessage newMsg = new ElevatorMessage(ElevatorMessage.MessageType.REQ, from, dir.getCode());
				elevatorNotifier.sendMessage(newMsg, Constants.ELEV_ADDR);
			}
			
		}

	}

	public void start() {
		
		//Logger.clearAllLogFiles();
		Scheduler s = this;

		// producer
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s.startListen();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					s.dequeue();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		t2.start();

		t1.start();
	}

	public void sendReq() {
		/*
		 * System.out.println("Sending Message"); ElevatorMessage msg = new
		 * ElevatorMessage(ElevatorMessage.MessageType.REQ, 4,
		 * Constants.DIR.UP.getCode()); elevatorNotifier.sendMessage(msg, "");
		 * reqUp.put(4, 6); System.out.println("msg: " + msg.toString()); reqUp.put(2,
		 * 7);
		 */

	}

	public static void main(String[] args) {
		Scheduler s = new Scheduler();
		s.start();

	}
}
