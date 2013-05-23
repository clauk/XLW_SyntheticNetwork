package rmi;

import gen.NetworkGenerator;
import java.rmi.Naming;

public class NetworkServerThread implements Runnable{
	
	private NetworkGenerator _networkGenerator;
	String _serverIP;
	public NetworkServerThread(NetworkGenerator network_Generator, String server_IP) {
		_networkGenerator = network_Generator;
		_serverIP = server_IP;
	}
	
	public void run() {
		try {
			IServer server = new NetworkServerImpl(_networkGenerator);
			Naming.rebind(_serverIP, server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
