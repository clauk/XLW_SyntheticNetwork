package rmi;

import java.rmi.Naming;

public class BarrierClientExample {

	public static void main(String[] args) {
		try {
			BServer server = (BServer) Naming.lookup("//127.0.0.1:1099/Server");

			/* Set serverID with argument */
			int serverID;
			if (args.length > 0)
				serverID = Integer.parseInt(args[0]);
			else
				serverID = 1;

			while (true) {
				System.out.println("into loop");
				try 
				{ 
					Thread.currentThread().sleep(2000); 
				} 
				catch(Exception e){
					System.out.println("Sleep Exception");
				}

				int result = server.CheckBarrierStatus(serverID);
				if (result == 1) {
					System.out.println("GOGOGO");
					break;
				}
				else
					System.out.println("Client #" + String.valueOf(serverID) + " is Waiting...");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
