package gen;

import rmi.*;

import java.rmi.Naming;
import java.util.LinkedList;

public class MainEntry {
		
	public static void main(String args[])
	{
		ServerConfig serverConfig = new ServerConfig();
		
		NetworkGenerator networkGenerator = new NetworkGenerator();
		networkGenerator.generateNode();
		LinkedList<Record> nodeList = networkGenerator.getNodeResult();
		
		//Set-up Server
		NetworkServerThread networkServerTh = new NetworkServerThread(networkGenerator, serverConfig.getLocalServerInfo()._serverAddress);
		Thread t1 = new Thread(networkServerTh); 
		t1.start();
		
		//check Barrier server to see if all finished
		BarrierClientExample.BarrierClientListen(serverConfig.getBarrierInfo()._serverAddress, serverConfig.getServerID());
		
		//Call get method and compute edges
		//use multi-thread to generateEdge? 
		int [] cursor = new int[serverConfig.getServerNum()];
		
		
		
	}
}
