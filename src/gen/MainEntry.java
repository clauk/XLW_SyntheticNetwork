package gen;

import rmi.*;

import java.rmi.Naming;
import java.util.LinkedList;

public class MainEntry {
		
	public static void main(String args[])
	{
		ServerConfig serverConfig = new ServerConfig();
		int localServerID = serverConfig.getServerID();
		int serverNum = serverConfig.getServerNum();
		
		NetworkGenerator networkGenerator = new NetworkGenerator();
		networkGenerator.generateNode();
		LinkedList<Record> nodeList = networkGenerator.getNodeResult();
		
		//Set-up Server
		
		NetworkServerThread networkServerTh = new NetworkServerThread(networkGenerator, serverConfig.getLocalServerInfo()._serverAddress);
		Thread networkServer = new Thread(networkServerTh); 
		networkServer.start();
		System.out.println("Local Server Started!");
		
		
		//check Barrier server to see if all finished
		//BarrierClientExample.BarrierClientListen(serverConfig.getBarrierInfo()._serverAddress, localServerID);
		System.out.println("Barrier Connected!");
		
		//Call get method and compute edges
		//use multi-thread to generateEdge? 
		/*
		NetworkClientThread [] networkClientTh = new NetworkClientThread[serverNum];
		Thread [] networkClient = new Thread[serverNum];
		for(int i=0; i<serverNum; i++){
			if(i!=localServerID){
				networkClientTh[i] = new NetworkClientThread(networkGenerator, serverConfig.getServerInfoArray()[i]._serverAddress); 
				networkClient[i] = new Thread(networkClientTh[i]);
				networkClient[i].start();
			}
		}
		*/		
		System.out.println("finish!");
		
		
		
	}
}
