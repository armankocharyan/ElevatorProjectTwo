package core;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPGetter {
	
	//This class statically prints the name and the IP of the computer

	public static void main(String[] args) throws UnknownHostException {

		String ipName = InetAddress.getLocalHost().getHostName();
		String ipHostAdd = InetAddress.getLocalHost().getHostAddress();
		
		
		System.out.println("IP Address: " + ipHostAdd + "\n");
		System.out.println("IP Name: " + ipName + "\n");
	}

}
