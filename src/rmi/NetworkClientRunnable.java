package rmi;

import gen.*;

import java.rmi.Naming;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


public class NetworkClientRunnable  implements Runnable{
	private NetworkGenerator _networkGenerator;
	private String _serverAddress;
	
	public NetworkClientRunnable(NetworkGenerator networkGenerator, String serverAddress){
		_networkGenerator = networkGenerator;
		_serverAddress = serverAddress;
	}
	
	public void run() {
		try {
			NetworkClient networkClient = new NetworkClient(_serverAddress);
			LinkedList<Record> nodeList = new LinkedList<Record>(_networkGenerator.getNodeResult());
			
			int nodeNum = _networkGenerator.getNodeNum();
			int cursorPos = 0;
			int fetchLen = Constants.FETCH_RECORD_BLOCK_SIZE;
			long totalTime = 0, startTime, endTime, edgeTime=0;
			while(cursorPos < nodeNum){
				startTime = System.currentTimeMillis();
				Record [] records = networkClient.getRecord(cursorPos, fetchLen);
				endTime = System.currentTimeMillis();
				totalTime += (endTime - startTime);
				startTime = System.currentTimeMillis();
				_networkGenerator.generateEdges(records, nodeList);
				endTime = System.currentTimeMillis();
				edgeTime += (endTime - startTime); 
				cursorPos += fetchLen;
			}
			
			System.out.println("Total Time on getRecord: "+totalTime +" ms");
			System.out.println("Total Time on generateEdges: "+edgeTime +" ms");
			System.out.println("Total Node Num: " +nodeNum);
			System.out.println("Pagesize: " +fetchLen);
			System.out.println("Total Operation Num: " +(nodeNum+fetchLen-1)/fetchLen);
			
			/*
			//TODO
			//just for check, remove this
			Map<Long, LinkedList<Long>> edgeListMap = _networkGenerator.getEdgeResult();
			Map<Long, LinkedList<Long>> orderMap = new TreeMap<Long, LinkedList<Long>>();
			for (Map.Entry entry : edgeListMap.entrySet()) {
				orderMap.put((Long)entry.getKey(), (LinkedList<Long>)entry.getValue());
			}
			Iterator<Long> nodeIterator = orderMap.keySet().iterator();
			while (nodeIterator.hasNext()) {
				Long recordID = nodeIterator.next();
				LinkedList<Long> edges = edgeListMap.get(recordID);
			    System.out.print(recordID+": ");
			    Iterator<Long> edgeIterator = edges.iterator();
			    while(edgeIterator.hasNext()){
			    	System.out.print(edgeIterator.next()+", ");
			    }
			    System.out.println();
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
