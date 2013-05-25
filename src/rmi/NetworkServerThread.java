package rmi;

import gen.NetworkGenerator;
import java.rmi.Naming;

public class NetworkServerThread implements Runnable{
	
	private NetworkGenerator _networkGenerator;
	private String _serverAddress;
	
	public NetworkServerThread(NetworkGenerator network_Generator, String server_Address) {
		_networkGenerator = network_Generator;
		_serverAddress = server_Address;
	}
	
	public void run() {
		try {
			IServer server = new NetworkServerImpl(_networkGenerator);
			Naming.rebind(_serverAddress, server);
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(_serverAddress);
			System.out.println("Bugs!");
		}
	}
}
