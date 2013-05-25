package gen;

import rmi.*;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainEntry {
		
	/**
	 * 
	 * @param args [0] server id(0-based) [1]: start record id  
	 */
	public static void main(String args[])
	{
		if (args.length != 2) {
			System.out.println("param error");
			return;
		}
		int localServerID = Integer.parseInt(args[0]);
		ServerConfig serverConfig = new ServerConfig(localServerID);
//		int localServerID = serverConfig.getServerID();
		
		long startRecordID = Long.parseLong(args[1]);
		int serverNum = serverConfig.getServerNum();
		
		NetworkGenerator networkGenerator = new NetworkGenerator();
		networkGenerator.generateNode(startRecordID);
		System.out.println("Node Generated!");
		
		//Set-up Server		
		NetworkServerRunnable networkServerTh = new NetworkServerRunnable(networkGenerator, serverConfig.getLocalServerInfo()._serverAddress);
		Thread networkServer = new Thread(networkServerTh); 
		networkServer.start();
		
		System.out.println("Local Server Started!");
		
		
		//check Barrier server to see if all Node generator finished
		BarrierClientExample.BarrierClientListen(serverConfig.getBarrierInfo()._serverAddress, localServerID, Constants.GEN_NODE_BARRIER_LABEL);
		System.out.println("GEN_NODE_BARRIER_LABEL pass!");
		
		//Call get method and compute edges
		//compute edges with local nodes
		networkGenerator.generateSelfEdges();
		//use multi-thread to get nodes from other servers and generateEdge
		NetworkClientRunnable [] networkClientRunnable = new NetworkClientRunnable[serverNum];
		
		List<Thread> networkClient = new ArrayList<Thread>();
		for(int i=0; i<serverNum; i++){
			if(i!=localServerID){
				networkClientRunnable[i] = new NetworkClientRunnable(networkGenerator, serverConfig.getServerInfoArray()[i]._serverAddress); 
				Thread thread = new Thread(networkClientRunnable[i]);
				thread.start();
				networkClient.add(thread);
			}
		}
		
		//Wait for all generateEdge threads finish 
		for (Thread thread : networkClient) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Edges Generated!");
		
		//Put results to server
		BarrierClientExample.putResult(serverConfig.getBarrierInfo()._serverAddress, 
				networkGenerator.getNodeResult(), networkGenerator.getEdgeResult(), Constants.PUT_RESULT_BLOCK_SIZE);
		BarrierClientExample.finishPutResult(serverConfig.getBarrierInfo()._serverAddress, localServerID);
		System.out.println("Put finish!");
				
		
	}
}
