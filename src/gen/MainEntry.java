package gen;

import rmi.*;

import java.rmi.Naming;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class MainEntry {
		
	public static void main(String args[])
	{
		ServerConfig serverConfig = new ServerConfig();
		int localServerID = serverConfig.getServerID();
		int serverNum = serverConfig.getServerNum();
		
		NetworkGenerator networkGenerator = new NetworkGenerator();
		networkGenerator.generateNode();
		System.out.println("Node Generated!");
		
		//Set-up Server		
		NetworkServerThread networkServerTh = new NetworkServerThread(networkGenerator, serverConfig.getLocalServerInfo()._serverAddress);
		Thread networkServer = new Thread(networkServerTh); 
		networkServer.start();
		
		System.out.println("Local Server Started!");
		
		
		//check Barrier server to see if all Node generator finished
		BarrierClientExample.BarrierClientListen(serverConfig.getBarrierInfo()._serverAddress, localServerID, Constants.GEN_NODE_BARRIER_LABEL);
		System.out.println("Barrier Connected!");
		
		//Call get method and compute edges
		//compute edges with local nodes
		networkGenerator.generateSelfEdges();
		//use multi-thread to get nodes from other servers and generateEdge
		NetworkClientThread [] networkClientTh = new NetworkClientThread[serverNum];
		Thread [] networkClient = new Thread[serverNum];
		for(int i=0; i<serverNum; i++){
			if(i!=localServerID){
				networkClientTh[i] = new NetworkClientThread(networkGenerator, serverConfig.getServerInfoArray()[i]._serverAddress); 
				networkClient[i] = new Thread(networkClientTh[i]);
				networkClient[i].start();
			}
		}
		
		//Wait for all generateEdge threads finish 
		for(int i=0; i<serverNum; i++){
			try {
				networkClient[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		System.out.println("Edges Generated!");
		
		//Put results to server
		BarrierClientExample.putResult(serverConfig.getBarrierInfo()._serverAddress, 
				networkGenerator.getNodeResult(), networkGenerator.getEdgeResult(), Constants.PUT_RESULT_BLOCK_SIZE);
		BarrierClientExample.finishPutResult(serverConfig.getBarrierInfo()._serverAddress, localServerID);
		System.out.println("Edges Generated!");
				
		
	}
}
