package rmi;

import gen.NetworkGenerator;
import gen.ServerInfo;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkServerRunnable implements Runnable{
	
	private NetworkGenerator _networkGenerator;
	private String _serverAddress;
    private int _port;
	
	public NetworkServerRunnable(NetworkGenerator network_Generator, ServerInfo server_info) {
		_networkGenerator = network_Generator;
		_serverAddress = server_info._serverAddress;
        _port = Integer.parseInt(server_info._serverPort);
	}
	
	public void run() {
		try {
            // start registry
            Registry registry = LocateRegistry.createRegistry(_port);
            System.out.println("created registry at port: " + _port + " server info: " + _serverAddress);

			IServer server = new NetworkServerImpl(_networkGenerator);
			System.out.println("rebind to: " + _serverAddress);
			Naming.rebind(_serverAddress, server);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(_serverAddress);
			System.out.println("Bugs!");
		}
	}
}
