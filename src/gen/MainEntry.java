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
		
		long startTime;
		long endTime;
		long genNodeTime;
		long genLocalEdgeTime;
		long genRemoteEdgeTime;
		long putResultTime;
		long overallStartTime;
		long overallEndTime;
		long overallTime;
		
		overallStartTime = System.currentTimeMillis();
		
		int localServerID = Integer.parseInt(args[0]);
		ServerConfig serverConfig = new ServerConfig(localServerID);
		
		long startRecordID = Long.parseLong(args[1]);
		int serverNum = serverConfig.getServerNum();
		
		NetworkGenerator networkGenerator = new NetworkGenerator(localServerID, startRecordID);
		startTime = System.currentTimeMillis();
		networkGenerator.generateNode();
		endTime = System.currentTimeMillis();
		genNodeTime = (endTime - startTime)/1000;
		System.out.println("Node Generated!");
		
		//Set-up Server		
		NetworkServerRunnable networkServerTh = new NetworkServerRunnable(networkGenerator, serverConfig.getLocalServerInfo());
		Thread networkServer = new Thread(networkServerTh); 
		networkServer.start();
		
		System.out.println("Local Server Started!");
		
		
		//check Barrier server to see if all Node generator finished
		BarrierClientExample.BarrierClientListen(serverConfig.getBarrierInfo()._serverAddress, localServerID, Constants.GEN_NODE_BARRIER_LABEL);
		System.out.println("GEN_NODE_BARRIER_LABEL pass!");
		
		//Call get method and compute edges
		//compute edges with local nodes
		startTime = System.currentTimeMillis();
		networkGenerator.generateSelfEdges();
		endTime = System.currentTimeMillis();
		genLocalEdgeTime = (endTime - startTime)/1000;
		System.out.println("Local Edge Generated!");
		
		//use multi-thread to get nodes from other servers and generateEdge
		startTime = System.currentTimeMillis();
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
		endTime = System.currentTimeMillis();
		genRemoteEdgeTime = (endTime - startTime)/1000;
		System.out.println("Remote Edge Generated!");
		
		//Put results to server
		startTime = System.currentTimeMillis();
		BarrierClientExample.putResult(serverConfig.getBarrierInfo()._serverAddress, 
				networkGenerator.getNodeResult(), networkGenerator.getEdgeResult(), Constants.PUT_RESULT_BLOCK_SIZE);
		BarrierClientExample.BarrierClientListen(serverConfig.getBarrierInfo()._serverAddress, localServerID, Constants.FINISH_LABEL);
		endTime = System.currentTimeMillis();
		putResultTime = (endTime - startTime)/1000;
		System.out.println("Put Result finish!");

		overallEndTime = System.currentTimeMillis();
		overallTime = (overallEndTime - overallStartTime)/1000;

		System.out.println("==============================================");
		System.out.println("Generate Node Time: " + genNodeTime);
		System.out.println("Generate Local Edge Time: " + genLocalEdgeTime);
		System.out.println("Generate Remote Edge Time: " + genRemoteEdgeTime);
		System.out.println("Put Result Time: " + putResultTime);
		System.out.println("Server Overall Running Time: " + overallTime);
				
		System.exit(0);
	}
}
