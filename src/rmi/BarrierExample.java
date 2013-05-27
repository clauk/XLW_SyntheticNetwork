package rmi;

import gen.ServerConfig;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BarrierExample {

	public BarrierExample(int n) {
		try {
			BarrierImpl server = new BarrierImpl();
			server.SetServerNum(n);
			ServerConfig serverConfig = new ServerConfig(0);
			
			int rmiPort = Integer.parseInt(serverConfig.getBarrierInfo()._serverPort);
			Registry registry = LocateRegistry.createRegistry(rmiPort);
			System.out.println("created registry at port: " + rmiPort);
			
			String bindAddr = serverConfig.getBarrierInfo()._serverAddress;
			System.out.println("barrier try to bind to: " + bindAddr);
			
			Naming.rebind(bindAddr, server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length > 0)
			new BarrierExample(Integer.parseInt(args[0]));
		else
			new BarrierExample(1);
	}
}
