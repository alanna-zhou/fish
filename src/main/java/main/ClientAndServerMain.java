package main;

import client.Main;
import server.Server;

/**
 * The Main class is where the critter world simulation can be launched and run.
 *
 */
public class ClientAndServerMain {

	public static void main(String[] args) {
		if (args.length == 0) {
			Main clientMain = new Main();
			clientMain.main(args); 
		} else if (args.length == 4) {
			int portNum = 8080; 
			String readPass;
			String writePass;
			String adminPass;
			try {
				portNum = Integer.parseInt(args[0]);

			} catch (NumberFormatException e) {
				System.out.println("Not a valid port number. Supplying default 8080.");

			}
			readPass = args[1];
			writePass = args[2];
			adminPass = args[3];

			Server server = new Server();
			server.run(portNum, readPass, writePass, adminPass);

		}

	}


}
