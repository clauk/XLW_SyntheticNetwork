package rmi;

import gen.NetworkGenerator;
import java.rmi.Naming;

public class NetworkServerRunnable implements Runnable{
	
	private NetworkGenerator _networkGenerator;
	private String _serverAddress;
	
	public NetworkServerRunnable(NetworkGenerator network_Generator, String server_Address) {
		_networkGenerator = network_Generator;
		_serverAddress = server_Address;
	}
	
	public void run() {
		try {
			IServer server = new NetworkServerImpl(_networkGenerator);
			System.out.println("rebind to: " + _serverAddress);
			Naming.rebind(_serverAddress, server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
