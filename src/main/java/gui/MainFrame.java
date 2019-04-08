package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Elevator.ElevatorController;
import Floor.FloorController;
import Scheduler.Scheduler;

import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JDesktopPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private final String [] args = null; //used as a parameter to call main methods
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame("MainFrame");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//runs the systems as three different subsystems
	public void runSystem(ElevatorController e, FloorController f, Scheduler s)  {
		// start the thread that listens for notifications from the scheduler
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() 
            { 
				s.start();
				
            } 
		});
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() 
            { 
				e.start();
            } 
		});
		
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() 
            { 
				f.start();
				f.readInput();
            } 
		});
		
		
		t1.start();
        t2.start();
        t3.start();
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(String title) {
		
		
		
		
		
		super(title);
		this.setSize(1000, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelTop = new JPanel();
		getContentPane().add(panelTop, BorderLayout.NORTH);
		
		JLabel topPanelLbl = new JLabel("Elevator System");
		panelTop.add(topPanelLbl);
		
		JPanel panelDown = new JPanel();
		getContentPane().add(panelDown, BorderLayout.SOUTH);
		
		
		JPanel panelLeft = new JPanel();
		getContentPane().add(panelLeft, BorderLayout.WEST);
		panelLeft.setLayout(new MigLayout("", "[66px][][][][][][][]", "[16px][][][][][][][][][][][][][][][][][][][][][][][][]"));
		
		JLabel elev1Lbl = new JLabel("Elevator0");
		panelLeft.add(elev1Lbl, "cell 0 1,alignx left,aligny top");
		
		JLabel lampElev1Lbl = new JLabel("Lamps");
		panelLeft.add(lampElev1Lbl, "cell 0 2");
		
		JLabel ActiveElev1Lbl = new JLabel("Active");
		panelLeft.add(ActiveElev1Lbl, "cell 2 2");
		
		JLabel motorElev1Lbl = new JLabel("Motor");
		panelLeft.add(motorElev1Lbl, "cell 4 2");
		
		JLabel doorsElev1Lbl = new JLabel("Doors");
		panelLeft.add(doorsElev1Lbl, "cell 6 2");
		
		JRadioButton lampElev1UP = new JRadioButton("UP");
		lampElev1UP.setEnabled(false);
		panelLeft.add(lampElev1UP, "flowy,cell 0 3");
		
		JRadioButton elev1Active = new JRadioButton("ON");
		elev1Active.setEnabled(false);
		panelLeft.add(elev1Active, "cell 2 3");
		
		JRadioButton motorElev1On = new JRadioButton("ON");
		motorElev1On.setEnabled(false);
		panelLeft.add(motorElev1On, "cell 4 3");
		
		JRadioButton elev1DoorsOpen = new JRadioButton("OPEN");
		elev1DoorsOpen.setEnabled(false);
		panelLeft.add(elev1DoorsOpen, "cell 6 3");
		
		JRadioButton lampElev1DOWN = new JRadioButton("Down");
		lampElev1DOWN.setEnabled(false);
		panelLeft.add(lampElev1DOWN, "cell 0 4");
		
		JRadioButton elev1DoorsClosed = new JRadioButton("CLOSED");
		elev1DoorsClosed.setEnabled(false);
		panelLeft.add(elev1DoorsClosed, "cell 6 4");
		
		JLabel currentFloorElev1Lbl = new JLabel("Current Floor:");
		panelLeft.add(currentFloorElev1Lbl, "cell 0 5");
		
		JLabel elev1CurrentFloorVal = new JLabel("0");
		panelLeft.add(elev1CurrentFloorVal, "cell 1 5");
		
		JLabel elev2Lbl = new JLabel("Elevator1");
		panelLeft.add(elev2Lbl, "cell 0 7");
		
		JLabel lampElev2Lbl = new JLabel("Lamps");
		panelLeft.add(lampElev2Lbl, "cell 0 8");
		
		JLabel ActiveElev2Lbl = new JLabel("Active");
		panelLeft.add(ActiveElev2Lbl, "cell 2 8");
		
		JLabel motorElev2Lbl = new JLabel("Motor");
		panelLeft.add(motorElev2Lbl, "cell 4 8");
		
		JLabel doorsElev2Lbl = new JLabel("Doors");
		panelLeft.add(doorsElev2Lbl, "cell 6 8");
		
		JRadioButton lampElev2UP = new JRadioButton("UP");
		lampElev2UP.setEnabled(false);
		panelLeft.add(lampElev2UP, "cell 0 9");
		
		JRadioButton elev2Active = new JRadioButton("ON");
		elev2Active.setEnabled(false);
		panelLeft.add(elev2Active, "cell 2 9");
		
		JRadioButton motorElev2On = new JRadioButton("ON");
		motorElev2On.setEnabled(false);
		panelLeft.add(motorElev2On, "cell 4 9");
		
		JRadioButton elev2DoorsOpen = new JRadioButton("OPEN");
		elev2DoorsOpen.setEnabled(false);
		panelLeft.add(elev2DoorsOpen, "cell 6 9");
		
		JRadioButton lampElev2DOWN = new JRadioButton("Down");
		lampElev2DOWN.setEnabled(false);
		panelLeft.add(lampElev2DOWN, "cell 0 10");
		
		JRadioButton elev2DoorsClosed = new JRadioButton("CLOSED");
		elev2DoorsClosed.setEnabled(false);
		panelLeft.add(elev2DoorsClosed, "cell 6 10");
		
		JLabel currentFloorElev2Lbl = new JLabel("Current Floor:");
		panelLeft.add(currentFloorElev2Lbl, "cell 0 11");
		
		JLabel elev2CurrentFloorVal = new JLabel("0");
		panelLeft.add(elev2CurrentFloorVal, "cell 1 11");
		
		JLabel elev3Lbl = new JLabel("Elevator2");
		panelLeft.add(elev3Lbl, "cell 0 13");
		
		JLabel lampElev3Lbl = new JLabel("Lamps");
		panelLeft.add(lampElev3Lbl, "cell 0 14");
		
		JLabel ActiveElev3Lbl = new JLabel("Active");
		panelLeft.add(ActiveElev3Lbl, "cell 2 14");
		
		JLabel motorElev3Lbl = new JLabel("Motor");
		panelLeft.add(motorElev3Lbl, "cell 4 14");
		
		JLabel doorsElev3Lbl = new JLabel("Doors");
		panelLeft.add(doorsElev3Lbl, "cell 6 14");
		
		JRadioButton lampElev3UP = new JRadioButton("UP");
		lampElev3UP.setEnabled(false);
		panelLeft.add(lampElev3UP, "cell 0 15");
		
		JRadioButton elev3Active = new JRadioButton("ON");
		elev3Active.setEnabled(false);
		panelLeft.add(elev3Active, "cell 2 15");
		
		JRadioButton motorElev3On = new JRadioButton("ON");
		motorElev3On.setEnabled(false);
		panelLeft.add(motorElev3On, "cell 4 15");
		
		JRadioButton elev3DoorsOpen = new JRadioButton("OPEN");
		elev3DoorsOpen.setEnabled(false);
		panelLeft.add(elev3DoorsOpen, "cell 6 15");
		
		JRadioButton lampElev3DOWN = new JRadioButton("Down");
		lampElev3DOWN.setEnabled(false);
		panelLeft.add(lampElev3DOWN, "cell 0 16");
		
		JRadioButton elev3DoorsClosed = new JRadioButton("CLOSED");
		elev3DoorsClosed.setEnabled(false);
		panelLeft.add(elev3DoorsClosed, "cell 6 16");
		
		JLabel currentFloorElev3Lbl = new JLabel("Current Floor:");
		panelLeft.add(currentFloorElev3Lbl, "cell 0 17");
		
		JLabel elev3CurrentFloorVal = new JLabel("0");
		panelLeft.add(elev3CurrentFloorVal, "cell 1 17");
		
		JLabel elev4Lbl = new JLabel("Elevator3");
		panelLeft.add(elev4Lbl, "cell 0 19");
		
		JLabel lampElev4Lbl = new JLabel("Lamps");
		panelLeft.add(lampElev4Lbl, "cell 0 20");
		
		JLabel ActiveElev4Lbl = new JLabel("Active");
		panelLeft.add(ActiveElev4Lbl, "cell 2 20");
		
		JLabel motorElev4Lbl = new JLabel("Motor");
		panelLeft.add(motorElev4Lbl, "cell 4 20");
		
		JLabel doorsElev4Lbl = new JLabel("Doors");
		panelLeft.add(doorsElev4Lbl, "cell 6 20");
		
		JRadioButton lampElev4UP = new JRadioButton("UP");
		lampElev4UP.setEnabled(false);
		panelLeft.add(lampElev4UP, "cell 0 21");
		
		JRadioButton elev4Active = new JRadioButton("ON");
		elev4Active.setEnabled(false);
		panelLeft.add(elev4Active, "cell 2 21");
		
		JRadioButton motorElev4On = new JRadioButton("ON");
		motorElev4On.setEnabled(false);
		panelLeft.add(motorElev4On, "cell 4 21");
		
		JRadioButton elev4DoorsOpen = new JRadioButton("OPEN");
		elev4DoorsOpen.setEnabled(false);
		panelLeft.add(elev4DoorsOpen, "cell 6 21");
		
		JRadioButton lampElev4DOWN = new JRadioButton("Down");
		lampElev4DOWN.setEnabled(false);
		panelLeft.add(lampElev4DOWN, "cell 0 22");
		
		JRadioButton elev4DoorsClosed = new JRadioButton("CLOSED");
		elev4DoorsClosed.setEnabled(false);
		panelLeft.add(elev4DoorsClosed, "cell 6 22");
		
		JLabel currentFloorElev4Lbl = new JLabel("Current Floor:");
		panelLeft.add(currentFloorElev4Lbl, "cell 0 23");
		
		JLabel elev4CurrentFloorVal = new JLabel("0");
		panelLeft.add(elev4CurrentFloorVal, "cell 1 23");
		
		JPanel panelRight = new JPanel();
		getContentPane().add(panelRight, BorderLayout.EAST);
		panelRight.setLayout(new MigLayout("", "[66px,grow]", "[16px][grow]"));
		
		JLabel inputFileLbl = new JLabel("Data Input");
		panelRight.add(inputFileLbl, "cell 0 0,alignx left,aligny top");
		
		JTextArea inputDataTextArea = new JTextArea();
		inputDataTextArea.setEditable(false);
		panelRight.add(inputDataTextArea, "cell 0 1,grow");
		
		JPanel panelCenter = new JPanel();
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(null);
		
		JLabel logLbl = new JLabel("Scheduler");
		logLbl.setBounds(191, 27, 85, 16);
		panelCenter.add(logLbl);
		
		JTextArea logTextBox = new JTextArea();
		logTextBox.setEditable(false);
		logTextBox.setBounds(22, 46, 483, 518);
		panelCenter.add(logTextBox);
		this.setVisible(true);
		
		JScrollPane sp = new JScrollPane(logTextBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(sp);
		
		//passing in variables to our systems
		JRadioButton openDoors [] = {elev1DoorsOpen, elev2DoorsOpen, elev3DoorsOpen, elev4DoorsOpen};
		JRadioButton closedDoors [] = {elev1DoorsClosed, elev2DoorsClosed, elev3DoorsClosed, elev4DoorsClosed};
		JRadioButton activeLamps [] = {elev1Active, elev2Active, elev3Active, elev4Active};
		JRadioButton motors [] = {motorElev1On, motorElev2On, motorElev3On, motorElev4On};
		JRadioButton UPLamps [] = {lampElev1UP, lampElev2UP, lampElev3UP, lampElev4UP};
		JRadioButton DOWNLamps [] = {lampElev1DOWN, lampElev2DOWN, lampElev3DOWN, lampElev4DOWN};
		JLabel currentFloorLabels [] = {elev1CurrentFloorVal, elev2CurrentFloorVal, elev3CurrentFloorVal, elev4CurrentFloorVal};
		
		//Systems
		ElevatorController e = new ElevatorController(openDoors, closedDoors, activeLamps, motors, UPLamps, DOWNLamps, currentFloorLabels);
		Scheduler s = new Scheduler(logTextBox);
		FloorController f = new FloorController();
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startButton.setEnabled(false);
				runSystem(e, f, s);
			}
		});
		panelDown.add(startButton);
	}
}
